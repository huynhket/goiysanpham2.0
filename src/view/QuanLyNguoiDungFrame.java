package view;

import dao.NguoiDungDAO;
import model.NguoiDung;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class QuanLyNguoiDungFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public QuanLyNguoiDungFrame() {
        // Apply Theme
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {}

        setTitle("Quản Lý Người Dùng");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("Danh sách người dùng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.add(lblTitle, BorderLayout.WEST);

        JPanel actionButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionButtons.setOpaque(false);
        
        JButton btnAdd = new JButton(" Thêm người dùng");
        btnAdd.setBackground(new Color(46, 204, 113));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> themNguoiDung());

        JButton btnReload = new JButton(" Làm mới");
        btnReload.addActionListener(e -> loadTable());

        actionButtons.add(btnAdd);
        actionButtons.add(btnReload);
        header.add(actionButtons, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // --- TABLE ---
        model = new DefaultTableModel(new String[]{"Mã", "Tên Đăng Nhập", "Họ Tên", "Vai Trò"}, 0);
        table = new JTable(model);
        styleTable();
        loadTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
        add(scroll, BorderLayout.CENTER);

        // --- FOOTER ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 15));
        footer.setBackground(new Color(248, 249, 250));
        
        JButton btnDelete = new JButton("🗑 Xóa người dùng");
        btnDelete.setForeground(new Color(192, 57, 43));
        btnDelete.addActionListener(e -> xoaNguoiDung());

        footer.add(btnDelete);
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
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        
        table.setSelectionBackground(new Color(235, 245, 255));
        table.setSelectionForeground(Color.BLACK);
    }

    private void loadTable() {
        model.setRowCount(0);
        List<NguoiDung> ds = NguoiDungDAO.layTatCa();
        for (NguoiDung nd : ds) {
            model.addRow(new Object[]{
                nd.getMaNguoiDung(),
                nd.getTenDangNhap(),
                nd.getHoTen(),
                nd.getVaiTro()
            });
        }
    }

    private void themNguoiDung() {
        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        JTextField txtHoTen = new JTextField();
        String[] roles = {"ADMIN", "USER"};
        JComboBox<String> cboRole = new JComboBox<>(roles);

        Object[] message = {
            "Tên đăng nhập:", txtUser,
            "Mật khẩu:", txtPass,
            "Họ tên:", txtHoTen,
            "Vai trò:", cboRole
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Thêm người dùng mới", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword());
            String name = txtHoTen.getText().trim();
            String role = (String) cboRole.getSelectedItem();

            if (user.isEmpty() || pass.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            NguoiDungDAO.them(user, pass, name, role);
            loadTable();
        }
    }

    private void xoaNguoiDung() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lọng chọn người dùng cần xóa!");
            return;
        }
        int ma = (int) model.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this, "Xác nhận xóa người dùng này?", "Cảnh báo", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            NguoiDungDAO.xoa(ma);
            loadTable();
        }
    }
}
