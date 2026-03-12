package view;

import dao.OrderDAO;
import org.bson.Document;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class QuanLyDonHangFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public QuanLyDonHangFrame() {
        // Apply Theme
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {}

        setTitle("Quản Lý Đơn Hàng - Admin");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("Danh sách đơn hàng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.add(lblTitle, BorderLayout.WEST);

        JButton btnReload = new JButton(" Làm mới");
        btnReload.addActionListener(e -> loadData());
        header.add(btnReload, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // --- TABLE ---
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{
            "Mã Đơn", "Mã User", "Tổng Tiền", "Phương Thức", "Trạng Thái", "Ngày Mua"
        });
        table = new JTable(model);
        styleTable();
        loadData();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
        add(scroll, BorderLayout.CENTER);

        // --- FOOTER ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 15));
        footer.setBackground(new Color(248, 249, 250));
        
        JButton btnUpdateStatus = new JButton("🔄 Cập nhật trạng thái");
        btnUpdateStatus.setBackground(new Color(52, 152, 219));
        btnUpdateStatus.setForeground(Color.WHITE);
        btnUpdateStatus.addActionListener(e -> updateStatus());
        
        footer.add(btnUpdateStatus);
        add(footer, BorderLayout.SOUTH);
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
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        
        table.setSelectionBackground(new Color(235, 245, 255));
        table.setSelectionForeground(Color.BLACK);
    }

    private void loadData() {
        model.setRowCount(0);
        List<Document> orders = OrderDAO.layTatCa();
        for (Document d : orders) {
            model.addRow(new Object[]{
                d.getInteger("ma_don_hang"),
                d.getInteger("ma_nguoi_dung"),
                String.format("%,.0f đ", d.getDouble("tong_tien")),
                d.getString("phuong_thuc"),
                d.getString("trang_thai"),
                d.get("ngay_mua")
            });
        }
    }

    private void updateStatus() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn hàng!");
            return;
        }

        int maDon = (int) table.getValueAt(row, 0);
        String currentStatus = (String) table.getValueAt(row, 4);

        String[] options = {"Chờ xử lý", "Đang giao", "Hoàn thành", "Hủy"};
        String choice = (String) JOptionPane.showInputDialog(
                this, "Chọn trạng thái mới cho đơn #" + maDon + ":", "Cập nhật",
                JOptionPane.PLAIN_MESSAGE, null, options, currentStatus);

        if (choice != null) {
            OrderDAO.capNhatTrangThai(maDon, choice);
            loadData();
        }
    }
}
