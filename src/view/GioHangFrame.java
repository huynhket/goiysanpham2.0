package view;
 
import dao.GioHangDAO;
import model.GioHang;
import javax.swing.*;
import java.awt.*;
import java.util.List;
 
public class GioHangFrame extends JFrame {
 
    private int maNguoiDung;
    private JPanel cardPanel;
    private JLabel lblTongTien;
    private Color shopeeOrange = new Color(238, 77, 45);
 
    public GioHangFrame(int maND) {
        this.maNguoiDung = maND;
 
        setTitle("Giỏ Hàng Của Tôi");
        setSize(500, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 245, 245));
        setLayout(new BorderLayout());
 
        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(225, 225, 225)));
        header.setPreferredSize(new Dimension(0, 60));
 
        JLabel title = new JLabel("  Giỏ Hàng", JLabel.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(shopeeOrange);
        header.add(title, BorderLayout.WEST);
 
        add(header, BorderLayout.NORTH);
 
        // --- CONTENT (Product List) ---
        cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(new Color(245, 245, 245));
 
        JScrollPane scrollPane = new JScrollPane(cardPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
 
        // --- BOTTOM BAR ---
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBackground(Color.WHITE);
        bottomBar.setPreferredSize(new Dimension(0, 80));
        bottomBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(225, 225, 225)));
 
        JPanel summary = new JPanel(new GridLayout(2, 1));
        summary.setOpaque(false);
        summary.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
 
        JLabel lblText = new JLabel("Tổng cộng:");
        lblText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        lblTongTien = new JLabel("0 đ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTongTien.setForeground(shopeeOrange);
 
        summary.add(lblText);
        summary.add(lblTongTien);
 
        JButton btnThanhToan = new JButton("Mua Hàng");
        btnThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnThanhToan.setBackground(shopeeOrange);
        btnThanhToan.setForeground(Color.WHITE);
        btnThanhToan.setPreferredSize(new Dimension(150, 0));
        btnThanhToan.setFocusPainted(false);
        btnThanhToan.setBorderPainted(false);
        btnThanhToan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnThanhToan.addActionListener(e -> {
            new ThanhToanFrame(maND);
            dispose();
        });
 
        bottomBar.add(summary, BorderLayout.CENTER);
        bottomBar.add(btnThanhToan, BorderLayout.EAST);
 
        add(bottomBar, BorderLayout.SOUTH);
 
        load();
        setVisible(true);
    }
 
    private void load() {
        cardPanel.removeAll();
        List<GioHang> ds = GioHangDAO.lay(maNguoiDung);
        double tong = 0;
 
        if (ds.isEmpty()) {
            hienTrong();
        } else {
            for (GioHang g : ds) {
                cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                cardPanel.add(taoItemCard(g));
                tong += g.getThanhTien();
            }
            cardPanel.add(Box.createVerticalGlue());
        }
 
        lblTongTien.setText(String.format("%,.0f đ", tong));
        cardPanel.revalidate();
        cardPanel.repaint();
    }
 
    private void hienTrong() {
        JPanel empty = new JPanel(new GridBagLayout());
        empty.setBackground(Color.WHITE);
        JLabel lbl = new JLabel("Giỏ hàng của bạn đang trống");
        lbl.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lbl.setForeground(Color.GRAY);
        empty.add(lbl);
        cardPanel.add(empty);
    }
 
    private JPanel taoItemCard(GioHang g) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(240, 240, 240)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
 
        // Info
        JPanel info = new JPanel(new GridBagLayout());
        info.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
 
        JLabel lblTen = new JLabel(g.getTenSanPham());
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 15));
        
        JLabel lblDL = new JLabel("Phân loại: " + g.getDungLuong());
        lblDL.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDL.setForeground(Color.GRAY);
 
        JLabel lblGia = new JLabel(String.format("%,.0f đ", g.getGia()));
        lblGia.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblGia.setForeground(shopeeOrange);
 
        gbc.gridy = 0; info.add(lblTen, gbc);
        gbc.gridy = 1; info.add(lblDL, gbc);
        gbc.gridy = 2; info.add(lblGia, gbc);
 
        // Control
        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20));
        ctrl.setOpaque(false);
        
        JLabel lblSL = new JLabel("x" + g.getSoLuong());
        lblSL.setFont(new Font("Segoe UI", Font.BOLD, 16));
        ctrl.add(lblSL);
 
        card.add(info, BorderLayout.CENTER);
        card.add(ctrl, BorderLayout.EAST);
 
        return card;
    }
}
