package view;

import dao.SanPhamDAO;
import dao.NguoiDungDAO;
import model.SanPham;
import model.NguoiDung;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

public class TrangQuanTriFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public TrangQuanTriFrame(NguoiDung nd) {
        // Apply Theme
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {}

        setTitle("Quản Trị Hệ Thống - Admin");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- SIDEBAR ---
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("ADMIN PANEL");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        sidebar.add(lblTitle);

        sidebar.add(createAdminNavBtn("📦 Sản Phẩm", true));
        sidebar.add(createAdminNavBtn("🛒 Đơn hàng", false, e -> new QuanLyDonHangFrame().setVisible(true)));
        sidebar.add(createAdminNavBtn("👥 Người dùng", false, e -> new QuanLyNguoiDungFrame().setVisible(true)));
        sidebar.add(createAdminNavBtn("📊 Thống kê", false, e -> showThongKe()));

        add(sidebar, BorderLayout.WEST);

        // --- MAIN CONTENT ---
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);

        // Header trong Main
        JPanel mainHeader = new JPanel(new BorderLayout());
        mainHeader.setBackground(Color.WHITE);
        mainHeader.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel lblSub = new JLabel("Danh sách sản phẩm");
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 22));
        mainHeader.add(lblSub, BorderLayout.WEST);

        JPanel actionButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionButtons.setOpaque(false);
        
        JButton btnAdd = new JButton(" Thêm sản phẩm");
        btnAdd.setBackground(new Color(46, 204, 113));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> themSanPham());

        JButton btnReload = new JButton(" Làm mới");
        btnReload.addActionListener(e -> loadTable());

        actionButtons.add(btnAdd);
        actionButtons.add(btnReload);
        mainHeader.add(actionButtons, BorderLayout.EAST);

        mainContent.add(mainHeader, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(new String[]{"Mã", "Tên Sản Phẩm", "Giá Bán", "Kho", "Danh Mục"}, 0);
        table = new JTable(model);
        styleTable();
        loadTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
        mainContent.add(scroll, BorderLayout.CENTER);

        // Footer Actions
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        footer.setBackground(new Color(248, 249, 250));
        
        JButton btnVariants = new JButton("🔧 Quản lý biến thể");
        btnVariants.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int ma = (int) table.getValueAt(row, 0);
                String ten = (String) table.getValueAt(row, 1);
                new QuanLyDungLuongFrame(ma, ten).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm!");
            }
        });

        JButton btnDelete = new JButton("🗑 Xóa sản phẩm");
        btnDelete.setForeground(new Color(192, 57, 43));
        btnDelete.addActionListener(e -> xoaSanPham());

        footer.add(btnVariants);
        footer.add(btnDelete);
        mainContent.add(footer, BorderLayout.SOUTH);

        add(mainContent, BorderLayout.CENTER);
    }

    private JButton createAdminNavBtn(String text, boolean active) {
        return createAdminNavBtn(text, active, null);
    }

    private JButton createAdminNavBtn(String text, boolean active, java.awt.event.ActionListener l) {
        JButton b = new JButton(text);
        b.setMaximumSize(new Dimension(240, 50));
        b.setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 15));
        b.setForeground(active ? Color.WHITE : new Color(189, 195, 199));
        b.setBackground(active ? new Color(52, 152, 219) : new Color(44, 62, 80));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (l != null) b.addActionListener(l);
        return b;
    }

    private void styleTable() {
        table.setRowHeight(35);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        
        table.setSelectionBackground(new Color(235, 245, 255));
        table.setSelectionForeground(Color.BLACK);
    }

    private void loadTable() {
        model.setRowCount(0);
        List<SanPham> ds = SanPhamDAO.layTatCa();
        for (SanPham sp : ds) {
            model.addRow(new Object[]{
                sp.getMaSanPham(),
                sp.getTenSanPham(),
                String.format("%,.0f đ", sp.getGia()),
                sp.getSoLuong(),
                sp.getMaDanhMuc()
            });
        }
    }

    private void showThongKe() {
        List<SanPham> sps = SanPhamDAO.layTatCa();
        List<NguoiDung> nds = NguoiDungDAO.layTatCa();
        int totalProducts = sps.size();
        int totalUsers = nds.size();
        double totalValue = sps.stream().mapToDouble(s -> s.getGia() * s.getSoLuong()).sum();

        String msg = String.format(
            "📊 THỐNG KÊ HỆ THỐNG\n\n" +
            "📦 Tổng số sản phẩm: %d\n" +
            "👥 Tổng số người dùng: %d\n" +
            "💰 Giá trị kho hàng ước tính: %,.0f đ",
            totalProducts, totalUsers, totalValue
        );
        JOptionPane.showMessageDialog(this, msg, "Thống kê", JOptionPane.INFORMATION_MESSAGE);
    }

    private void xoaSanPham() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int ma = (int) model.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this, "Xác nhận xóa sản phẩm này?", "Cảnh báo", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            SanPhamDAO.xoa(ma);
            loadTable();
        }
    }

    private void themSanPham() {
        JDialog dialog = new JDialog(this, "Thêm sản phẩm mới", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtTen = new JTextField();
        JTextField txtGia = new JTextField();
        JTextField txtSoLuong = new JTextField();
        JTextField txtDanhMuc = new JTextField();
        JTextField txtMoTa = new JTextField();
        JTextField txtHinhAnh = new JTextField();
        JTextField txtDanhSachHinhAnh = new JTextField();

        // Drag and drop helper
        addDropSupport(txtHinhAnh);
        addDropSupport(txtDanhSachHinhAnh);

        formPanel.add(new JLabel("Tên sản phẩm:"));
        formPanel.add(txtTen);
        formPanel.add(new JLabel("Giá:"));
        formPanel.add(txtGia);
        formPanel.add(new JLabel("Số lượng:"));
        formPanel.add(txtSoLuong);
        formPanel.add(new JLabel("Mã danh mục:"));
        formPanel.add(txtDanhMuc);
        formPanel.add(new JLabel("Mô tả:"));
        formPanel.add(txtMoTa);
        
        JLabel lblAnhMain = new JLabel("Ảnh chính:");
        formPanel.add(lblAnhMain);
        JPanel pnlAnhMain = new JPanel(new BorderLayout(5, 0));
        pnlAnhMain.add(txtHinhAnh, BorderLayout.CENTER);
        JButton btnChonAnhMain = new JButton("Chọn");
        btnChonAnhMain.addActionListener(e -> chonAnh(dialog, txtHinhAnh, false));
        pnlAnhMain.add(btnChonAnhMain, BorderLayout.EAST);
        formPanel.add(pnlAnhMain);
        
        JLabel lblAnhPhu = new JLabel("Ảnh phụ:");
        formPanel.add(lblAnhPhu);
        JPanel pnlAnhPhu = new JPanel(new BorderLayout(5, 0));
        pnlAnhPhu.add(txtDanhSachHinhAnh, BorderLayout.CENTER);
        JButton btnChonAnhPhu = new JButton("Chọn");
        btnChonAnhPhu.addActionListener(e -> chonAnh(dialog, txtDanhSachHinhAnh, true));
        pnlAnhPhu.add(btnChonAnhPhu, BorderLayout.EAST);
        formPanel.add(pnlAnhPhu);

        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");

        btnSave.addActionListener(e -> {
            try {
                String ten = txtTen.getText().trim();
                double gia = Double.parseDouble(txtGia.getText().trim());
                int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
                int maDanhMuc = Integer.parseInt(txtDanhMuc.getText().trim());
                String moTa = txtMoTa.getText().trim();
                String hinhAnh = sanitizePath(txtHinhAnh.getText().trim());
                String dsHinhAnhStr = txtDanhSachHinhAnh.getText().trim();

                java.util.List<String> dsHinhAnh = new java.util.ArrayList<>();
                if (!dsHinhAnhStr.isEmpty()) {
                    for (String s : dsHinhAnhStr.split(",")) {
                        dsHinhAnh.add(sanitizePath(s.trim()));
                    }
                }

                if (ten.isEmpty()) throw new Exception("Tên không được trống");

                SanPhamDAO.them(ten, gia, soLuong, maDanhMuc, moTa, hinhAnh, dsHinhAnh);
                loadTable();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi nhập liệu: " + ex.getMessage());
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void chonAnh(JDialog parent, JTextField textField, boolean multi) {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(multi);
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "png", "gif", "jpeg"));
        if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            if (multi) {
                File[] files = chooser.getSelectedFiles();
                StringBuilder sb = new StringBuilder();
                String currentText = textField.getText().trim();
                if (!currentText.isEmpty()) {
                    sb.append(currentText).append(", ");
                }
                for (int i = 0; i < files.length; i++) {
                    sb.append(files[i].getAbsolutePath());
                    if (i < files.length - 1) sb.append(", ");
                }
                textField.setText(sb.toString());
            } else {
                textField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        }
    }

    private void addDropSupport(JTextField textField) {
        textField.setToolTipText("Bạn có thể kéo thả file ảnh vào đây");
        new DropTarget(textField, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent event) {
                try {
                    event.acceptDrop(DnDConstants.ACTION_COPY);
                    java.util.List<File> droppedFiles = (java.util.List<File>) event.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    if (droppedFiles != null && !droppedFiles.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        String currentText = textField.getText().trim();
                        if (!currentText.isEmpty()) {
                            sb.append(currentText).append(", ");
                        }
                        for (int i = 0; i < droppedFiles.size(); i++) {
                            sb.append(droppedFiles.get(i).getAbsolutePath());
                            if (i < droppedFiles.size() - 1) sb.append(", ");
                        }
                        textField.setText(sb.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String sanitizePath(String fullPath) {
        // Chỉ lấy đường dẫn tương đối (thư mục images/...)
        // Nếu ng dùng kéo thả từ hệ thống, cố gắng chuyển nó thành relative.
        // Tạm thời chỉ lấy tên file nếu nó được bỏ vào src/images
        int index = fullPath.indexOf("images");
        if (index != -1) {
            String relative = fullPath.substring(index).replace("\\", "/");
            return relative;
        }
        return fullPath; // fallback
    }
}
