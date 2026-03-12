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

        Color primary = new Color(0, 102, 204);

        // ================= HEADER =================
        JLabel title = new JLabel("QUÉT QR MB BANK", SwingConstants.CENTER);
        title.setOpaque(true);
        title.setBackground(primary);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setPreferredSize(new Dimension(100, 55));
        add(title, BorderLayout.NORTH);

        // ================= CENTER =================
        JPanel center = new JPanel();
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
        JPanel pButton = new JPanel();

        JButton btnOK = new JButton("Tôi đã thanh toán");
        JButton btnCancel = new JButton("Hủy");

        btnOK.setBackground(primary);
        btnOK.setForeground(Color.WHITE);

        btnCancel.setBackground(Color.GRAY);
        btnCancel.setForeground(Color.WHITE);

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
