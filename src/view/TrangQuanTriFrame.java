package view;

import dao.SanPhamDAO;
import dao.NguoiDungDAO;
import model.SanPham;
import model.NguoiDung;
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
import dao.OrderDAO;
import org.bson.Document;

public class TrangQuanTriFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JPanel thongKeBody;

    public TrangQuanTriFrame(NguoiDung nd) {
        setTitle("Quản Trị Hệ Thống - Admin");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set window icon
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/logo.png"));
            setIconImage(logoIcon.getImage());
        } catch (Exception ignored) {}

        // --- SIDEBAR ---
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBackground(new Color(30, 39, 46));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("ADMIN PANEL");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        sidebar.add(lblTitle);

        // --- MAIN CONTENT (CARD LAYOUT) ---
        CardLayout cardLayout = new CardLayout();
        JPanel mainCardPanel = new JPanel(cardLayout);

        sidebar.add(createAdminNavBtn("📦 Sản Phẩm", true, e -> cardLayout.show(mainCardPanel, "SanPham")));
        sidebar.add(createAdminNavBtn("🛒 Đơn hàng", false, e -> cardLayout.show(mainCardPanel, "DonHang")));
        sidebar.add(createAdminNavBtn("👥 Người dùng", false, e -> cardLayout.show(mainCardPanel, "NguoiDung")));
        sidebar.add(createAdminNavBtn("📊 Thống kê", false, e -> {
            updateThongKeData();
            cardLayout.show(mainCardPanel, "ThongKe");
        }));
        
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(createAdminNavBtn("📝 Đăng ký tài khoản", false, e -> cardLayout.show(mainCardPanel, "NguoiDung")));
        sidebar.add(createAdminNavBtn("🚪 Đăng xuất", false, e -> {
            dispose();
            new DangNhap().setVisible(true);
        }));
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        add(sidebar, BorderLayout.WEST);

        // --- BẢNG SẢN PHẨM ---
        JPanel pnlSanPham = new JPanel(new BorderLayout());
        pnlSanPham.setBackground(new Color(248, 250, 252));

        // Header trong Main
        JPanel mainHeader = new JPanel(new BorderLayout());
        mainHeader.setBackground(Color.WHITE);
        mainHeader.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(223, 228, 234)),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        JLabel lblSub = new JLabel("Danh sách sản phẩm");
        lblSub.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        lblSub.setForeground(new Color(44, 62, 80));
        mainHeader.add(lblSub, BorderLayout.WEST);

        JPanel actionButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionButtons.setOpaque(false);
        
        JButton btnAdd = new JButton("<html><nobr><span style='font-family: \"Segoe UI Emoji\";'>\u2795</span> <span style='font-family: \"Segoe UI\";'>Th\u00eam s\u1ea3n ph\u1ea9m</span></nobr></html>");
        btnAdd.putClientProperty("JButton.buttonType", "roundRect");
        btnAdd.setBackground(new Color(52, 152, 219));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setFocusPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> themSanPham());

        JButton btnReload = new JButton("<html><nobr><span style='font-family: \"Segoe UI Emoji\";'>\uD83D\uDD04</span> <span style='font-family: \"Segoe UI\";'>L\u00e0m m\u1edbi</span></nobr></html>");
        btnReload.putClientProperty("JButton.buttonType", "roundRect");
        btnReload.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnReload.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReload.addActionListener(e -> loadTable());

        actionButtons.add(btnAdd);
        actionButtons.add(btnReload);
        mainHeader.add(actionButtons, BorderLayout.EAST);

        pnlSanPham.add(mainHeader, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(new String[]{"Mã", "Tên Sản Phẩm", "Giá Bán", "Kho", "Danh Mục"}, 0);
        table = new JTable(model);
        styleTable();
        loadTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
        pnlSanPham.add(scroll, BorderLayout.CENTER);

        // Footer Actions
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(223, 228, 234)));
        
        JButton btnVariants = new JButton("<html><nobr><span style='font-family: \"Segoe UI Emoji\";'>\uD83D\uDD27</span> <span style='font-family: \"Segoe UI\";'>Qu\u1ea3n l\u00fd bi\u1ebfn th\u1ec3</span></nobr></html>");
        btnVariants.putClientProperty("JButton.buttonType", "roundRect");
        btnVariants.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVariants.setCursor(new Cursor(Cursor.HAND_CURSOR));
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

        JButton btnDelete = new JButton("<html><nobr><span style='font-family: \"Segoe UI Emoji\";'>\uD83D\uDDD1</span> <span style='font-family: \"Segoe UI\";'>X\u00f3a s\u1ea3n ph\u1ea9m</span></nobr></html>");
        btnDelete.putClientProperty("JButton.buttonType", "roundRect");
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDelete.setForeground(new Color(231, 76, 60));
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.addActionListener(e -> xoaSanPham());

        footer.add(btnVariants);
        footer.add(btnDelete);
        pnlSanPham.add(footer, BorderLayout.SOUTH);

        mainCardPanel.add(pnlSanPham, "SanPham");
        mainCardPanel.add(new QuanLyDonHangFrame(), "DonHang");
        mainCardPanel.add(new QuanLyNguoiDungFrame(), "NguoiDung");

        // --- BẢNG THỐNG KÊ ---
        JPanel pnlThongKe = new JPanel(new BorderLayout());
        pnlThongKe.setBackground(new Color(248, 250, 252));
        
        JPanel thongKeHeader = new JPanel(new BorderLayout());
        thongKeHeader.setBackground(Color.WHITE);
        thongKeHeader.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(223, 228, 234)),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        JLabel lblThongKeTitle = new JLabel("Thống kê hệ thống");
        lblThongKeTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        lblThongKeTitle.setForeground(new Color(44, 62, 80));
        thongKeHeader.add(lblThongKeTitle, BorderLayout.WEST);
        
        JButton btnReloadThongKe = new JButton("<html><nobr><span style='font-family: \"Segoe UI Emoji\";'>\uD83D\uDD04</span> <span style='font-family: \"Segoe UI\";'>L\u00e0m m\u1edbi</span></nobr></html>");
        btnReloadThongKe.putClientProperty("JButton.buttonType", "roundRect");
        btnReloadThongKe.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnReloadThongKe.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReloadThongKe.addActionListener(e -> updateThongKeData());
        thongKeHeader.add(btnReloadThongKe, BorderLayout.EAST);
        
        pnlThongKe.add(thongKeHeader, BorderLayout.NORTH);

        thongKeBody = new JPanel();
        thongKeBody.setLayout(new BoxLayout(thongKeBody, BoxLayout.Y_AXIS));
        thongKeBody.setOpaque(false);
        
        JScrollPane scrollThongKe = new JScrollPane(thongKeBody);
        scrollThongKe.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlThongKe.add(scrollThongKe, BorderLayout.CENTER);
        
        mainCardPanel.add(pnlThongKe, "ThongKe");

        add(mainCardPanel, BorderLayout.CENTER);
    }

    private JButton createAdminNavBtn(String text, boolean active) {
        return createAdminNavBtn(text, active, null);
    }

    private JButton createAdminNavBtn(String text, boolean active, java.awt.event.ActionListener l) {
        String formattedText = text;
        int spaceIdx = text.indexOf(' ');
        if (spaceIdx > 0 && text.length() > 2) {
            String emoji = text.substring(0, spaceIdx);
            String label = text.substring(spaceIdx + 1);
            formattedText = "<html><nobr><span style='font-family: \"Segoe UI Emoji\";'>" + emoji + "</span>&nbsp;<span style='font-family: \"Segoe UI\";'>" + label + "</span></nobr></html>";
        }
        JButton b = new JButton(formattedText);
        b.setMaximumSize(new Dimension(240, 50));
        b.setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 16));
        b.setForeground(active ? Color.WHITE : new Color(189, 195, 199));
        b.setBackground(active ? new Color(52, 152, 219) : new Color(30, 39, 46));
        b.setContentAreaFilled(active);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (!active) {
            b.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    b.setContentAreaFilled(true);
                    b.setBackground(new Color(72, 84, 96));
                    b.setForeground(Color.WHITE);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    b.setContentAreaFilled(false);
                    b.setForeground(new Color(189, 195, 199));
                }
            });
        }
        if (l != null) b.addActionListener(l);
        return b;
    }

    private void styleTable() {
        table.setRowHeight(35);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.getTableHeader().setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
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

    private void updateThongKeData() {
        // --- 1. Tổng quát chung ---
        List<SanPham> sps = SanPhamDAO.layTatCa();
        List<NguoiDung> nds = NguoiDungDAO.layTatCa();
        int totalProducts = sps.size();
        int totalUsers = nds.size();
        double totalValue = sps.stream().mapToDouble(s -> s.getGia() * s.getSoLuong()).sum();

        // --- 2. Thống kê theo tháng này ---
        List<Document> ordersThisMonth = OrderDAO.layDonHangTrongThangNay();
        int spBanThangNay = 0;
        double doanhThuThangNay = 0;
        for (Document order : ordersThisMonth) {
            String status = order.getString("trang_thai");
            // Chỉ tính những đơn đã thanh toán hoặc hoàn thành / đang giao tuỳ logic đồ án, ở đây tính hết đơn trừ "Hủy"
            if (status != null && !status.equalsIgnoreCase("Hủy")) {
                doanhThuThangNay += order.getDouble("tong_tien");
                List<Document> items = order.getList("items", Document.class);
                if (items != null) {
                    for (Document item : items) {
                        spBanThangNay += item.getInteger("quantity", 0);
                    }
                }
            }
        }

        if (thongKeBody != null) {
            thongKeBody.removeAll();
            
            // Panel Tổng Quát
            JPanel pnlTongQuat = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
            pnlTongQuat.setOpaque(false);
            pnlTongQuat.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(223, 228, 234)), 
                "TỔNG QUAN HỆ THỐNG", 
                0, 0, 
                new Font("Segoe UI Emoji", Font.BOLD, 14), 
                new Color(44, 62, 80)
            ));
            
            pnlTongQuat.add(createStatCard("📦 Tổng số sản phẩm", String.valueOf(totalProducts), new Color(41, 128, 185)));
            pnlTongQuat.add(createStatCard("👥 Tổng số người dùng", String.valueOf(totalUsers), new Color(39, 174, 96)));
            pnlTongQuat.add(createStatCard("💰 Giá trị hiện có", String.format("%,.0f đ", totalValue), new Color(231, 76, 60)));
            
            // Panel Tháng Hiện Tại
            JPanel pnlThangNay = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
            pnlThangNay.setOpaque(false);
            pnlThangNay.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(223, 228, 234)), 
                "THỐNG KÊ THÁNG NÀY (Từ ngày 1 - cuối tháng)", 
                0, 0, 
                new Font("Segoe UI Emoji", Font.BOLD, 14), 
                new Color(230, 126, 34)
            ));
            
            pnlThangNay.add(createStatCard("🛒 SP Đã bán", String.valueOf(spBanThangNay) + " SP", new Color(142, 68, 173)));
            pnlThangNay.add(createStatCard("💵 Doanh thu", String.format("%,.0f đ", doanhThuThangNay), new Color(243, 156, 18)));
            pnlThangNay.add(createStatCard("📦 Đơn hàng mới", String.valueOf(ordersThisMonth.size()) + " Đơn", new Color(22, 160, 133)));

            thongKeBody.add(pnlTongQuat);
            thongKeBody.add(Box.createRigidArea(new Dimension(0, 20)));
            thongKeBody.add(pnlThangNay);
            thongKeBody.add(Box.createVerticalGlue());

            thongKeBody.revalidate();
            thongKeBody.repaint();
        }
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(250, 120));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(223, 228, 234)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        String formattedTitle = title;
        int spaceIdx = title.indexOf(' ');
        if (spaceIdx > 0 && title.length() > 2) {
            String emoji = title.substring(0, spaceIdx);
            String label = title.substring(spaceIdx + 1);
            formattedTitle = "<html><nobr><span style='font-family: \"Segoe UI Emoji\";'>" + emoji + "</span> <span style='font-family: \"Segoe UI\";'>" + label + "</span></nobr></html>";
        }
        JLabel lblTitle = new JLabel(formattedTitle);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(127, 140, 141));
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI Emoji", Font.BOLD, 28));
        lblValue.setForeground(color);
        
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
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
            @SuppressWarnings("unchecked")
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
