package view;

import dao.DungLuongDAO;
import model.DungLuong;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

public class QuanLyDungLuongFrame extends JFrame {

    private int maSanPham;
    private JTable table;
    private DefaultTableModel model;

    public QuanLyDungLuongFrame(int maSP, String tenSP) {
        this.maSanPham = maSP;
        setTitle("Quản lý biến thể: " + tenSP);
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(223, 228, 234)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitle = new JLabel("Biến thể: " + tenSP);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(44, 62, 80));
        header.add(lblTitle, BorderLayout.WEST);

        JButton btnReload = new JButton("🔄 Làm mới");
        btnReload.putClientProperty("JButton.buttonType", "roundRect");
        btnReload.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnReload.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReload.addActionListener(e -> loadData());
        header.add(btnReload, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // --- TABLE ---
        model = new DefaultTableModel(new String[]{"Mã DL", "Dung lượng", "Phụ phí", "Số lượng"}, 0);
        table = new JTable(model);
        styleTable();
        loadData();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        add(scroll, BorderLayout.CENTER);

        // --- FOOTER ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(223, 228, 234)));
        
        JButton btnThem = new JButton("➕ Thêm biến thể");
        btnThem.putClientProperty("JButton.buttonType", "roundRect");
        btnThem.setBackground(new Color(52, 152, 219));
        btnThem.setForeground(Color.WHITE);
        btnThem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnThem.setFocusPainted(false);
        btnThem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton btnXoa = new JButton("🗑 Xóa");
        btnXoa.putClientProperty("JButton.buttonType", "roundRect");
        btnXoa.setForeground(new Color(231, 76, 60));
        btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnXoa.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        footer.add(btnThem);
        footer.add(btnXoa);
        add(footer, BorderLayout.SOUTH);

        btnThem.addActionListener(e -> them());
        btnXoa.addActionListener(e -> xoa());
    }

    private void styleTable() {
        table.setRowHeight(35);
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        
        table.setSelectionBackground(new Color(235, 245, 255));
        table.setSelectionForeground(Color.BLACK);
    }

    private void loadData() {
        model.setRowCount(0);
        List<DungLuong> ds = DungLuongDAO.layTheoSanPham(maSanPham);
        for (DungLuong dl : ds) {
            model.addRow(new Object[]{
                dl.getMaDL(), 
                dl.getDungLuong(), 
                String.format("%,.0f đ", dl.getPhuPhi()), 
                dl.getSoLuong()
            });
        }
    }

    private void them() {
        JDialog dialog = new JDialog(this, "Thêm biến thể mới", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtDL = new JTextField();
        JTextField txtPP = new JTextField();
        JTextField txtSL = new JTextField();
        JTextField txtHA = new JTextField();

        addDropSupport(txtHA);

        formPanel.add(new JLabel("Nhập dung lượng (vd: 128GB):"));
        formPanel.add(txtDL);
        formPanel.add(new JLabel("Phụ phí:"));
        formPanel.add(txtPP);
        formPanel.add(new JLabel("Số lượng kho:"));
        formPanel.add(txtSL);
        
        JLabel lblAnh = new JLabel("Đường dẫn ảnh:");
        formPanel.add(lblAnh);
        JPanel pnlAnh = new JPanel(new BorderLayout(5, 0));
        pnlAnh.add(txtHA, BorderLayout.CENTER);
        JButton btnChonAnh = new JButton("Chọn");
        btnChonAnh.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "png", "gif", "jpeg"));
            if (chooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                txtHA.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        pnlAnh.add(btnChonAnh, BorderLayout.EAST);
        formPanel.add(pnlAnh);

        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");

        btnSave.addActionListener(e -> {
            try {
                String dl = txtDL.getText().trim();
                double pp = Double.parseDouble(txtPP.getText().trim());
                int sl = Integer.parseInt(txtSL.getText().trim());
                String ha = sanitizePath(txtHA.getText().trim());
                
                if (dl.isEmpty()) throw new Exception("Dung lượng không được trống");

                DungLuongDAO.them(maSanPham, dl, pp, sl, ha);
                loadData();
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
                        textField.setText(droppedFiles.get(0).getAbsolutePath());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String sanitizePath(String fullPath) {
        int index = fullPath.indexOf("images");
        if (index != -1) {
            return fullPath.substring(index).replace("\\", "/");
        }
        return fullPath;
    }

    private void xoa() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn biến thể cần xóa!");
            return;
        }
        int maDL = (int) table.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this, "Xác nhận xóa biến thể này?", "Cảnh báo", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            DungLuongDAO.xoa(maDL);
            loadData();
        }
    }
}
