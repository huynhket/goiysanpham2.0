package view;
 
import dao.LichSuMuaHangDAO;
import model.LichSuMuaHang;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
 
public class LichSuMuaHangFrame extends JFrame {
 
    private JPanel historyPanel;
    private Color shopeeOrange = new Color(238, 77, 45);
 
    public LichSuMuaHangFrame(int maNguoiDung) {
        setTitle("Lịch Sử Mua Hàng");
        setSize(550, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 245, 245));
        setLayout(new BorderLayout());
 
        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(225, 225, 225)));
        header.setPreferredSize(new Dimension(0, 60));
        
        JLabel title = new JLabel("  Đơn Mua", JLabel.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(shopeeOrange);
        header.add(title, BorderLayout.WEST);
        
        add(header, BorderLayout.NORTH);
 
        // --- TABS (Simulated) ---
        JPanel tabs = new JPanel(new GridLayout(1, 1));
        tabs.setBackground(Color.WHITE);
        tabs.setPreferredSize(new Dimension(0, 50));
        JLabel tabAll = new JLabel("Tất cả", JLabel.CENTER);
        tabAll.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabAll.setForeground(shopeeOrange);
        tabAll.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, shopeeOrange));
        tabs.add(tabAll);
        
        header.add(tabs, BorderLayout.SOUTH);
        header.setPreferredSize(new Dimension(0, 110));
 
        // --- HISTORY LIST ---
        historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        historyPanel.setBackground(new Color(245, 245, 245));
 
        JScrollPane scroll = new JScrollPane(historyPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
 
        loadData(maNguoiDung);
        setVisible(true);
    }
 
    private void loadData(int maNguoiDung) {
        historyPanel.removeAll();
        ArrayList<LichSuMuaHang> list = LichSuMuaHangDAO.getHistory(maNguoiDung);
 
        if (list.isEmpty()) {
            JPanel empty = new JPanel(new GridBagLayout());
            empty.setOpaque(false);
            empty.add(new JLabel("Chưa có đơn hàng nào"));
            historyPanel.add(empty);
        } else {
            for (LichSuMuaHang ls : list) {
                historyPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                historyPanel.add(taoDonHangCard(ls));
            }
            historyPanel.add(Box.createVerticalGlue());
        }
 
        historyPanel.revalidate();
        historyPanel.repaint();
    }
 
    private JPanel taoDonHangCard(LichSuMuaHang ls) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
 
        // Header card
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel lblShop = new JLabel("📦 Đơn hàng: #" + ls.getMaDonHang());
        lblShop.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel lblStatus = new JLabel(ls.getTrangThai());
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblStatus.setForeground(shopeeOrange);
        
        header.add(lblShop, BorderLayout.WEST);
        header.add(lblStatus, BorderLayout.EAST);
 
        // Body card
        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);
        body.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(245, 245, 245)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
 
        JLabel lblTen = new JLabel(ls.getTenSanPham());
        lblTen.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        
        JLabel lblQty = new JLabel("x" + ls.getSoLuong());
        lblQty.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblQty.setForeground(Color.GRAY);
        
        gbc.gridy = 0; body.add(lblTen, gbc);
        gbc.gridy = 1; body.add(lblQty, gbc);
 
        // Footer card
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        JLabel lblPriceTotal = new JLabel("Thành tiền: ");
        lblPriceTotal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        JLabel lblPrice = new JLabel(String.format("%,.0f đ", ls.getGia()));
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblPrice.setForeground(shopeeOrange);
        
        footer.add(lblPriceTotal);
        footer.add(lblPrice);
 
        card.add(header, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);
        card.add(footer, BorderLayout.SOUTH);
 
        return card;
    }
}