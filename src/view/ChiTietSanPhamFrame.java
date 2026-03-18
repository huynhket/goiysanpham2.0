package view;

import dao.HanhViDAO;
import dao.SanPhamDAO;
import dao.DungLuongDAO;
import model.SanPham;
import model.DungLuong;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import dao.GioHangDAO;

public class ChiTietSanPhamFrame extends JFrame {

    private JLabel lblGia;

    public ChiTietSanPhamFrame(int maNguoiDung, SanPham sp){

        setTitle("Chi tiết sản phẩm");
        setSize(900, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(30,0));
        getContentPane().setBackground(new Color(248, 250, 252)); // Softer background

        // ================= LEFT (ẢNH) =================
        JPanel left = new JPanel(new BorderLayout());
        left.setPreferredSize(new Dimension(350,450));
        left.setBackground(Color.WHITE);

        JLabel lblImg = new JLabel("", JLabel.CENTER);
        setMainImage(lblImg, sp.getHinhAnh());
        left.add(lblImg, BorderLayout.CENTER);

        JPanel thumbPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        thumbPanel.setBackground(Color.WHITE);
        addThumbnail(thumbPanel, lblImg, sp.getHinhAnh());
        if (sp.getDanhSachHinhAnh() != null) {
            for (String imgPath : sp.getDanhSachHinhAnh()) {
                addThumbnail(thumbPanel, lblImg, imgPath);
            }
        }
        
        JScrollPane scrollThumb = new JScrollPane(thumbPanel);
        scrollThumb.setBorder(null);
        scrollThumb.setPreferredSize(new Dimension(350, 80));
        left.add(scrollThumb, BorderLayout.SOUTH);

        // ================= RIGHT (INFO) =================
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBackground(new Color(248, 250, 252));
        right.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 40));


        // ===== TÊN =====
        JLabel ten = new JLabel("<html><body style='width: 350px'>" + sp.getTenSanPham() + "</body></html>");
        ten.setFont(new Font("Segoe UI", Font.BOLD, 28));
        ten.setForeground(new Color(30, 39, 46));


        // ===== GIÁ =====
        lblGia = new JLabel(String.format("%,.0f đ", sp.getGia()));
        lblGia.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblGia.setForeground(new Color(231, 76, 60));


        // ===== TỒN KHO =====
        JLabel tonKho = new JLabel("Tồn kho: " + sp.getSoLuong() + " sản phẩm");
        tonKho.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tonKho.setForeground(new Color(127, 140, 141));


        // ================= DUNG LƯỢNG =================
        JLabel lbDL = new JLabel("Chọn Phiên Bản:");
        lbDL.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbDL.setForeground(new Color(44, 62, 80));

        JComboBox<DungLuong> cboDL = new JComboBox<>();
        cboDL.setMaximumSize(new Dimension(250,35));

        List<DungLuong> ds = DungLuongDAO.layTheoSanPham(sp.getMaSanPham());
        for(DungLuong d : ds){
            cboDL.addItem(d);
        }

        // đổi giá khi chọn
        cboDL.addActionListener(e -> {
            DungLuong dl = (DungLuong) cboDL.getSelectedItem();
            if(dl != null){
                double giaMoi = sp.getGia() + dl.getPhuPhi();
                lblGia.setText(String.format("%,.0f đ", giaMoi));
                if (dl.getHinhAnh() != null && !dl.getHinhAnh().trim().isEmpty()) {
                    setMainImage(lblImg, dl.getHinhAnh());
                } else {
                    setMainImage(lblImg, sp.getHinhAnh());
                }
            }
        });


        // ================= MÔ TẢ =================
        JTextArea moTa = new JTextArea(sp.getMoTa());
        moTa.setLineWrap(true);
        moTa.setWrapStyleWord(true);
        moTa.setEditable(false);
        moTa.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        moTa.setForeground(new Color(87, 101, 116));
        moTa.setBackground(new Color(248, 250, 252));

        JScrollPane scroll = new JScrollPane(moTa);
        scroll.setPreferredSize(new Dimension(420, 150));
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(223, 228, 234)), 
                "Mô tả sản phẩm", 
                0, 0, 
                new Font("Segoe UI", Font.BOLD, 14), 
                new Color(44, 62, 80)
        ));

        // ================= NÚT =================

        JButton btnGio = new JButton("🛒 Thêm giỏ hàng");
        btnGio.putClientProperty("JButton.buttonType", "roundRect");
        btnGio.setBackground(new Color(52, 152, 219));
        btnGio.setForeground(Color.WHITE);
        btnGio.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnGio.setFocusPainted(false);
        btnGio.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnMua = new JButton("⚡ Đặt mua ngay");
        btnMua.putClientProperty("JButton.buttonType", "roundRect");
        btnMua.setBackground(new Color(231, 76, 60));
        btnMua.setForeground(Color.WHITE);
        btnMua.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnMua.setFocusPainted(false);
        btnMua.setCursor(new Cursor(Cursor.HAND_CURSOR));


        // ===== GIỎ HÀNG =====
        btnGio.addActionListener(e -> {

            DungLuong dl = (DungLuong) cboDL.getSelectedItem();

            // ⭐ 1. lưu AI (giữ nguyên)
            HanhViDAO.luuHanhVi(maNguoiDung, sp.getMaSanPham(), "cart");

            // ⭐ 2. lưu giỏ hàng thật
            GioHangDAO.them(maNguoiDung, sp.getMaSanPham(), dl.getMaDL());

            JOptionPane.showMessageDialog(this,"Đã thêm vào giỏ hàng 🛒");
        });



        // ===== MUA =====
        btnMua.addActionListener(e -> {
            DungLuong dl = (DungLuong) cboDL.getSelectedItem();

            // Lưu hành vi và thêm vào giỏ hàng
            HanhViDAO.luuHanhVi(maNguoiDung, sp.getMaSanPham(), "cart");
            GioHangDAO.them(maNguoiDung, sp.getMaSanPham(), dl.getMaDL());

            // Mở trang Giỏ Hàng
            new GioHangFrame(maNguoiDung).setVisible(true);
            dispose();
        });


        // panel nút ngang
        JPanel panelBtn = new JPanel(new GridLayout(1,2,10,0));
        panelBtn.setMaximumSize(new Dimension(400,40));
        panelBtn.add(btnGio);
        panelBtn.add(btnMua);


        // ================= ADD =================
        right.add(ten);
        right.add(Box.createVerticalStrut(10));
        right.add(lblGia);
        right.add(Box.createVerticalStrut(5));
        right.add(tonKho);

        right.add(Box.createVerticalStrut(15));
        right.add(lbDL);
        right.add(cboDL);

        right.add(Box.createVerticalStrut(15));
        right.add(scroll);

        right.add(Box.createVerticalStrut(20));
        right.add(panelBtn);


        add(left, BorderLayout.WEST);
        add(right, BorderLayout.CENTER);

        setVisible(true);
    }

    private void setMainImage(JLabel lblImg, String path) {
        if (path == null || path.trim().isEmpty()) return;
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/" + path));
            Image img = icon.getImage().getScaledInstance(320, 360, Image.SCALE_SMOOTH);
            lblImg.setIcon(new ImageIcon(img));
            lblImg.setText("");
        } catch (Exception e) {
            lblImg.setIcon(null);
            lblImg.setText("No Image");
        }
    }

    private void addThumbnail(JPanel thumbPanel, JLabel lblImg, String path) {
        if (path == null || path.trim().isEmpty()) return;
        JLabel thumb = new JLabel();
        thumb.setPreferredSize(new Dimension(60, 60));
        thumb.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        thumb.setCursor(new Cursor(Cursor.HAND_CURSOR));
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/" + path));
            Image img = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            thumb.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            thumb.setText("X");
            thumb.setHorizontalAlignment(SwingConstants.CENTER);
        }
        thumb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                setMainImage(lblImg, path);
            }
        });
        thumbPanel.add(thumb);
    }
}
