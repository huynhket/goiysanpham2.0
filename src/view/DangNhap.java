package view;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;

public class DangNhap extends JFrame {

    private JTextField txtTenDangNhap;
    private JPasswordField txtMatKhau;
    private JButton btnDangNhap;

    public DangNhap() {
        // Apply FlatLaf Theme
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Gợi Ý Sản Phẩm - Đăng Nhập");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Main Panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        mainPanel.setBackground(Color.WHITE);

        // Logo/Title Area
        JLabel lblTitle = new JLabel("WELCOME");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setForeground(new Color(52, 152, 219)); // Modern Blue

        JLabel lblSubTitle = new JLabel("Đăng nhập vào hệ thống");
        lblSubTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubTitle.setForeground(Color.GRAY);

        // Input Fields
        txtTenDangNhap = new JTextField();
        txtTenDangNhap.putClientProperty("JTextField.placeholderText", "Tên đăng nhập");
        txtTenDangNhap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        txtMatKhau = new JPasswordField();
        txtMatKhau.putClientProperty("JTextField.placeholderText", "Mật khẩu");
        txtMatKhau.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Submit Button
        btnDangNhap = new JButton("Đăng Nhập");
        btnDangNhap.setBackground(new Color(52, 152, 219));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFocusPainted(false);
        btnDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDangNhap.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDangNhap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnDangNhap.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Event Handling
        btnDangNhap.addActionListener(e -> xuLyDangNhap());

        // Adding components to layout
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(lblSubTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        mainPanel.add(new JLabel("Tên đăng nhập"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(txtTenDangNhap);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(new JLabel("Mật khẩu"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(txtMatKhau);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(btnDangNhap);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void xuLyDangNhap() {
        String user = txtTenDangNhap.getText().trim();
        String pass = new String(txtMatKhau.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        model.NguoiDung nd = dao.NguoiDungDAO.dangNhap(user, pass);

        if (nd == null) {
            JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (nd.getVaiTro().equalsIgnoreCase("ADMIN")) {
            new TrangQuanTriFrame(nd).setVisible(true);
        } else {
            new TrangChuFrame(nd.getMaNguoiDung(), nd.getHoTen()).setVisible(true);
        }

        dispose();
    }
}
