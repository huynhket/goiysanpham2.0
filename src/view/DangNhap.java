package view;

import javax.swing.*;
import java.awt.*;

public class DangNhap extends JFrame {

    private JTextField txtTenDangNhap;
    private JPasswordField txtMatKhau;
    private JButton btnDangNhap;
    private JButton btnDangKy;

    public DangNhap() {
        // Apply FlatLaf Theme
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.themes.FlatMacLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("G\u1ee3i \u00dd S\u1ea3n Ph\u1ea9m - \u0110\u0103ng Nh\u1eadp");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set window icon
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/logo.png"));
            setIconImage(logoIcon.getImage());
        } catch (Exception ignored) {}

        // --- LEFT PANEL (IMAGE) ---
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(245, 247, 250));
        leftPanel.setPreferredSize(new Dimension(400, 500));
        
        JLabel lblImg = new JLabel();
        lblImg.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            // Updated to the generated illustration path
            ImageIcon icon = new ImageIcon("C:/Users/KET/.gemini/antigravity/brain/1f249bcd-414e-4478-bb2f-c0bc89c2a8e6/login_illustration_1773392127188.png");
            Image img = icon.getImage().getScaledInstance(400, 500, Image.SCALE_SMOOTH);
            lblImg.setIcon(new ImageIcon(img));
        } catch (Exception e) {}
        leftPanel.add(lblImg, BorderLayout.CENTER);

        // --- RIGHT PANEL (LOGIN FORM) ---
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(60, 40, 60, 40));
        rightPanel.setBackground(Color.WHITE);

        // Logo/Title Area
        JLabel lblTitle = new JLabel("Welcome Back");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setForeground(new Color(41, 128, 185)); // Deep modern Blue

        JLabel lblSubTitle = new JLabel("\u0110\u0103ng nh\u1eadp \u0111\u1ec3 ti\u1ebfp t\u1ee5c");
        lblSubTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubTitle.setForeground(new Color(127, 140, 141));

        // Input Fields
        txtTenDangNhap = new JTextField();
        txtTenDangNhap.putClientProperty("JTextField.placeholderText", "    T\u00ean \u0111\u0103ng nh\u1eadp / Email");
        txtTenDangNhap.putClientProperty("JTextField.showClearButton", true);
        txtTenDangNhap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txtTenDangNhap.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        txtMatKhau = new JPasswordField();
        txtMatKhau.putClientProperty("JTextField.placeholderText", "    M\u1eadt kh\u1ea9u");
        txtMatKhau.putClientProperty("JTextField.showClearButton", true);
        txtMatKhau.putClientProperty("JPasswordField.showRevealButton", true);
        txtMatKhau.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txtMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Submit Button
        btnDangNhap = new JButton("\u0110\u0103ng Nh\u1eadp");
        btnDangNhap.setBackground(new Color(52, 152, 219));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFocusPainted(false);
        btnDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnDangNhap.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDangNhap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnDangNhap.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnDangKy = new JButton("Đăng Ký Tài Khoản Mới");
        btnDangKy.setBackground(new Color(245, 247, 250));
        btnDangKy.setForeground(new Color(41, 128, 185));
        btnDangKy.setFocusPainted(false);
        btnDangKy.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDangKy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDangKy.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnDangKy.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDangKy.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 1));

        // Event Handling
        btnDangNhap.addActionListener(e -> xuLyDangNhap());
        btnDangKy.addActionListener(e -> xuLyDangKy());

        // Adding components to layout
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(lblTitle);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        rightPanel.add(lblSubTitle);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        rightPanel.add(txtTenDangNhap);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(txtMatKhau);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        rightPanel.add(btnDangNhap);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        rightPanel.add(btnDangKy);
        rightPanel.add(Box.createVerticalGlue());

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    private void xuLyDangNhap() {
        String user = txtTenDangNhap.getText().trim();
        String pass = new String(txtMatKhau.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui l\u00f2ng nh\u1eadp \u0111\u1ea7y \u0111\u1ee7 th\u00f4ng tin!", "Th\u00f4ng b\u00e1o", JOptionPane.WARNING_MESSAGE);
            return;
        }

        model.NguoiDung nd = dao.NguoiDungDAO.dangNhap(user, pass);

        if (nd == null) {
            JOptionPane.showMessageDialog(this, "Sai t\u00e0i kho\u1ea3n ho\u1eb7c m\u1eadt kh\u1ea9u", "L\u1ed7i", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (nd.getVaiTro().equalsIgnoreCase("ADMIN")) {
            new TrangQuanTriFrame(nd).setVisible(true);
        } else {
            new TrangChuFrame(nd.getMaNguoiDung(), nd.getHoTen()).setVisible(true);
        }

        dispose();
    }

    private void xuLyDangKy() {
        JDialog dialog = new JDialog(this, "Đăng Ký Tài Khoản", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JTextField txtRegUser = new JTextField();
        JPasswordField txtRegPass = new JPasswordField();
        JTextField txtRegName = new JTextField();

        formPanel.add(new JLabel("Tên đăng nhập:"));
        formPanel.add(txtRegUser);
        formPanel.add(new JLabel("Mật khẩu:"));
        formPanel.add(txtRegPass);
        formPanel.add(new JLabel("Họ và tên:"));
        formPanel.add(txtRegName);

        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnSave = new JButton("Đăng Ký");
        btnSave.setBackground(new Color(41, 128, 185));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setFocusPainted(false);
        
        JButton btnCancel = new JButton("Hủy");
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        btnSave.addActionListener(e -> {
            String user = txtRegUser.getText().trim();
            String pass = new String(txtRegPass.getPassword());
            String name = txtRegName.getText().trim();

            if (user.isEmpty() || pass.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (dao.NguoiDungDAO.kienTraTonTai(user)) {
                JOptionPane.showMessageDialog(dialog, "Tên đăng nhập đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            dao.NguoiDungDAO.them(user, pass, name, "USER");
            JOptionPane.showMessageDialog(dialog, "Đăng ký thành công! Bạn có thể đăng nhập ngay.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            
            // Auto fill the login form
            txtTenDangNhap.setText(user);
            txtMatKhau.setText(pass);
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}
