package view;
 
import dao.GioHangDAO;
import dao.ThanhToanDAO;
import model.GioHang;
import org.json.*;
import javax.swing.*;
import java.awt.*;
import java.util.Scanner;
import java.util.List;
 
public class ThanhToanFrame extends JFrame {
 
    private JComboBox<String> cboTinh = new JComboBox<>();
    private JComboBox<String> cboHuyen = new JComboBox<>();
    private JComboBox<String> cboXa = new JComboBox<>();
    private JTextField txtSoNha = new JTextField();
    private JRadioButton rdoCOD = new JRadioButton("Thanh toán khi nhận hàng (COD)");
    private JRadioButton rdoCK = new JRadioButton("Chuyển khoản ngân hàng");
    private JLabel lblTotal;
    private Color shopeeOrange = new Color(238, 77, 45);
    private int maND;
 
    public ThanhToanFrame(int maND) {
        this.maND = maND;
 
        setTitle("Thanh Toán");
        setSize(550, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 245, 245));
        setLayout(new BorderLayout());
 
        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(225, 225, 225)));
        header.setPreferredSize(new Dimension(0, 60));
        JLabel title = new JLabel("  Thanh Toán", JLabel.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(shopeeOrange);
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);
 
        // --- CENTER CONTENT ---
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(245, 245, 245));
 
        // 1. Địa chỉ
        content.add(taoSectionAddress());
        content.add(Box.createRigidArea(new Dimension(0, 10)));
 
        // 2. Sản phẩm
        content.add(taoSectionItems());
        content.add(Box.createRigidArea(new Dimension(0, 10)));
 
        // 3. Phương thức
        content.add(taoSectionPayment());
 
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
 
        // --- BOTTOM BAR ---
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Color.WHITE);
        bottom.setPreferredSize(new Dimension(0, 80));
        bottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(225, 225, 225)));
 
        double tong = GioHangDAO.tinhTongTien(maND);
        JPanel pricePanel = new JPanel(new GridLayout(2, 1));
        pricePanel.setOpaque(false);
        pricePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        
        JLabel lblTxt = new JLabel("Tổng thanh toán:");
        lblTxt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTotal = new JLabel(String.format("%,.0f đ", tong));
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotal.setForeground(shopeeOrange);
        
        pricePanel.add(lblTxt);
        pricePanel.add(lblTotal);
 
        JButton btnOrder = new JButton("Đặt Hàng");
        btnOrder.setBackground(shopeeOrange);
        btnOrder.setForeground(Color.WHITE);
        btnOrder.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnOrder.setPreferredSize(new Dimension(150, 0));
        btnOrder.setFocusPainted(false);
        btnOrder.setBorderPainted(false);
        btnOrder.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnOrder.addActionListener(e -> xửLýThanhToán(maND, tong));
 
        bottom.add(pricePanel, BorderLayout.CENTER);
        bottom.add(btnOrder, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);
 
        loadTinh();
        cboTinh.addActionListener(e -> loadHuyen(cboTinh.getSelectedIndex() + 1));
        cboHuyen.addActionListener(e -> loadXa(cboHuyen.getSelectedIndex() + 1));
 
        setVisible(true);
    }
 
    private JPanel taoSectionAddress() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
 
        JLabel lblIcon = new JLabel("📍 Địa chỉ nhận hàng ");
        lblIcon.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblIcon.setForeground(shopeeOrange);
        p.add(lblIcon, BorderLayout.NORTH);
 
        JPanel form = new JPanel(new GridLayout(4, 1, 5, 5));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        txtSoNha.putClientProperty("JTextField.placeholderText", "Số nhà, tên đường...");
        form.add(txtSoNha);
        form.add(cboTinh);
        form.add(cboHuyen);
        form.add(cboXa);
 
        p.add(form, BorderLayout.CENTER);
        return p;
    }
 
    private JPanel taoSectionItems() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
 
        JLabel lblHeader = new JLabel("Sản phẩm");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
        p.add(lblHeader);
        p.add(Box.createRigidArea(new Dimension(0, 10)));
 
        List<GioHang> ds = GioHangDAO.lay(maND);
        for (GioHang g : ds) {
            JPanel item = new JPanel(new BorderLayout());
            item.setOpaque(false);
            item.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            
            JLabel lblTen = new JLabel(g.getTenSanPham() + " (x" + g.getSoLuong() + ")");
            lblTen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            
            JLabel lblGia = new JLabel(String.format("%,.0f đ", g.getThanhTien()));
            lblGia.setFont(new Font("Segoe UI", Font.BOLD, 14));
            
            item.add(lblTen, BorderLayout.CENTER);
            item.add(lblGia, BorderLayout.EAST);
            p.add(item);
        }
 
        return p;
    }
 
    private JPanel taoSectionPayment() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
 
        JLabel lblHeader = new JLabel("Phương thức thanh toán");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
        p.add(lblHeader);
        p.add(Box.createRigidArea(new Dimension(0, 10)));
 
        ButtonGroup group = new ButtonGroup();
        group.add(rdoCOD);
        group.add(rdoCK);
        rdoCOD.setSelected(true);
        rdoCOD.setBackground(Color.WHITE);
        rdoCK.setBackground(Color.WHITE);
 
        p.add(rdoCOD);
        p.add(rdoCK);
 
        return p;
    }
 
    private void xửLýThanhToán(int maND, double tong) {
        String soNha = txtSoNha.getText().trim();
        if (soNha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập địa chỉ giao hàng!");
            return;
        }
 
        String diaChi = soNha + ", " + cboXa.getSelectedItem() + ", " + cboHuyen.getSelectedItem() + ", " + cboTinh.getSelectedItem();
        String phuongThuc = rdoCOD.isSelected() ? "COD" : "CHUYEN_KHOAN";
 
        if (phuongThuc.equals("COD")) {
            ThanhToanDAO.thanhToan(maND, diaChi, phuongThuc);
            JOptionPane.showMessageDialog(this, "Đặt hàng thành công! Đơn hàng đang được xử lý.");
            dispose();
        } else {
            QRDialog qr = new QRDialog(this, maND, diaChi, phuongThuc, tong);
            if (qr.isConfirmed()) {
                dispose();
            }
        }
    }
 
    private String getJson(String link) {
        try {
            java.net.URI uri = new java.net.URI(link);
            try (Scanner sc = new Scanner(uri.toURL().openStream())) {
                StringBuilder sb = new StringBuilder();
                while (sc.hasNext()) sb.append(sc.nextLine());
                return sb.toString();
            }
        } catch (Exception e) { return ""; }
    }
 
    private void loadTinh() {
        cboTinh.removeAllItems();
        try {
            String json = getJson("https://provinces.open-api.vn/api/p/");
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) cboTinh.addItem(arr.getJSONObject(i).getString("name"));
        } catch (Exception e) {}
    }
 
    private void loadHuyen(int code) {
        cboHuyen.removeAllItems();
        cboXa.removeAllItems();
        try {
            String json = getJson("https://provinces.open-api.vn/api/p/" + code + "?depth=2");
            JSONArray arr = new JSONObject(json).getJSONArray("districts");
            for (int i = 0; i < arr.length(); i++) cboHuyen.addItem(arr.getJSONObject(i).getString("name"));
        } catch (Exception e) {}
    }
 
    private void loadXa(int code) {
        cboXa.removeAllItems();
        try {
            String json = getJson("https://provinces.open-api.vn/api/d/" + code + "?depth=2");
            JSONArray arr = new JSONObject(json).getJSONArray("wards");
            for (int i = 0; i < arr.length(); i++) cboXa.addItem(arr.getJSONObject(i).getString("name"));
        } catch (Exception e) {}
    }
}
