package view;

import dao.ThanhToanDAO;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.text.NumberFormat;

public class QRDialog extends JDialog {

    private boolean confirmed = false;
    private int seconds = 60;
    private JLabel lblTimer;

    public boolean isConfirmed() {
        return confirmed;
    }

    public QRDialog(
            Frame parent,
            int maND,
            String diaChi,
            String phuongThuc,
            double soTien
    ) {
        super(parent, "Thanh toán MB Bank", true); // ⭐ modal

        setSize(420, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        Color primary = new Color(231, 76, 60);

        // ================= HEADER =================
        JLabel title = new JLabel("QUÉT QR THANH TOÁN", SwingConstants.CENTER);
        title.setOpaque(true);
        title.setBackground(primary);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setPreferredSize(new Dimension(100, 65));
        add(title, BorderLayout.NORTH);

        // ================= CENTER =================
        JPanel center = new JPanel();
        center.setBackground(Color.WHITE);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        // ===== FORMAT TIỀN =====
        String money = NumberFormat.getInstance().format(soTien);

        // ===== LINK VIETQR =====
        String vietQR =
                "https://img.vietqr.io/image/MB-0837379945-compact2.png"
                        + "?amount=" + (int) soTien
                        + "&addInfo=ThanhToanShopMini"
                        + "&accountName=HUYNH%20DOAN%20KET";

        try {
            ImageIcon icon = new ImageIcon(new URI(vietQR).toURL());
            Image img = icon.getImage().getScaledInstance(260, 260, Image.SCALE_SMOOTH);

            JLabel lblQR = new JLabel(new ImageIcon(img));
            lblQR.setAlignmentX(Component.CENTER_ALIGNMENT);

            center.add(Box.createVerticalStrut(20));
            center.add(lblQR);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // ===== INFO =====
        JLabel info = new JLabel(
                "<html><center>" +
                        "<b>MB BANK - 0837379945</b><br>" +
                        "Huỳnh Đoàn Kết<br><br>" +
                        "<span style='color:red;font-size:18px'>"
                        + money + " VNĐ</span>" +
                        "</center></html>"
        );

        info.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(Box.createVerticalStrut(10));
        center.add(info);

        // ===== TIMER =====
        lblTimer = new JLabel("Tự đóng sau: 60s");
        lblTimer.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTimer.setForeground(Color.GRAY);

        center.add(Box.createVerticalStrut(10));
        center.add(lblTimer);

        add(center, BorderLayout.CENTER);

        // ================= BUTTON =================
        JPanel pButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pButton.setBackground(Color.WHITE);
        pButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton btnOK = new JButton("Tôi đã thanh toán");
        btnOK.putClientProperty("JButton.buttonType", "roundRect");
        btnOK.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnOK.setBackground(primary);
        btnOK.setForeground(Color.WHITE);
        btnOK.setFocusPainted(false);
        btnOK.setPreferredSize(new Dimension(160, 40));
        btnOK.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnCancel = new JButton("Hủy");
        btnCancel.putClientProperty("JButton.buttonType", "roundRect");
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setBackground(new Color(189, 195, 199));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setPreferredSize(new Dimension(100, 40));
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // ⭐ CONFIRM → LƯU DB TẠI ĐÂY
        btnOK.addActionListener(e -> {

            ThanhToanDAO.thanhToan(maND, diaChi, phuongThuc);

            confirmed = true;

            JOptionPane.showMessageDialog(this, "Thanh toán thành công 🎉");

            dispose();
        });

        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        pButton.add(btnOK);
        pButton.add(btnCancel);

        add(pButton, BorderLayout.SOUTH);

        startCountdown();

        setVisible(true); // ⭐ bắt buộc
    }

    // ================= COUNTDOWN =================
    private void startCountdown() {

        Timer timer = new Timer(1000, e -> {
            seconds--;
            lblTimer.setText("Tự đóng sau: " + seconds + "s");

            if (seconds <= 0) {
                ((Timer) e.getSource()).stop();
                dispose();
            }
        });

        timer.start();
    }
}
