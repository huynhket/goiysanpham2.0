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
        setSize(820, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(25,0));
        getContentPane().setBackground(Color.WHITE);

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
        right.setBackground(Color.WHITE);
        right.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));


        // ===== TÊN =====
        JLabel ten = new JLabel(sp.getTenSanPham());
        ten.setFont(new Font("Arial", Font.BOLD, 24));


        // ===== GIÁ =====
        lblGia = new JLabel(String.format("%,.0f đ", sp.getGia()));
        lblGia.setFont(new Font("Arial", Font.BOLD, 22));
        lblGia.setForeground(Color.RED);


        // ===== TỒN KHO =====
        JLabel tonKho = new JLabel("Tồn kho: " + sp.getSoLuong());


        // ================= DUNG LƯỢNG =================
        JLabel lbDL = new JLabel("Dung lượng:");
        lbDL.setFont(new Font("Arial", Font.BOLD, 14));

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
        moTa.setFont(new Font("Arial", Font.PLAIN, 13));

        JScrollPane scroll = new JScrollPane(moTa);
        scroll.setPreferredSize(new Dimension(380,140));
        scroll.setBorder(BorderFactory.createTitledBorder("Mô tả sản phẩm"));


        // ================= NÚT =================

        JButton btnGio = new JButton("🛒 Thêm giỏ hàng");
        btnGio.setBackground(new Color(108,117,125));
        btnGio.setForeground(Color.WHITE);
        btnGio.setFocusPainted(false);

        JButton btnMua = new JButton("⚡ Đặt mua ngay");
        btnMua.setBackground(new Color(220,53,69));
        btnMua.setForeground(Color.WHITE);
        btnMua.setFocusPainted(false);


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

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Mua " + sp.getTenSanPham() + " - " + dl.getDungLuong() + " ?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
            );

            if(confirm == JOptionPane.YES_OPTION){

                HanhViDAO.luuHanhVi(maNguoiDung, sp.getMaSanPham(), "buy");

                boolean ok1 = SanPhamDAO.truSoLuong(sp.getMaSanPham());
                boolean ok2 = DungLuongDAO.truSoLuong(dl.getMaDL());

                if(ok1 && ok2){
                    JOptionPane.showMessageDialog(this,"Mua thành công 🎉");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,"Xin lỗi, sản phẩm này vừa hết hàng!");
                }
            }
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
