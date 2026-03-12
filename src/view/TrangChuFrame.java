package view;

import dao.SanPhamDAO;
import dao.HanhViDAO;
import model.SanPham;
import ai.AIRecommend;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;

public class TrangChuFrame extends JFrame {

    private int maNguoiDung;
    private JPanel mainContent;
    private JPanel cardGrid;

    public TrangChuFrame(int maNguoiDung, String tenNguoiDung) {
        this.maNguoiDung = maNguoiDung;

        // Apply Theme
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {}

        runTrain();

        setTitle("SmartShop - Gợi Ý Sản Phẩm");
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- SIDEBAR / NAVIGATION ---
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(245, 246, 250));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 221, 225)));

        JLabel lblBrand = new JLabel("SmartShop");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblBrand.setForeground(new Color(44, 62, 80));
        lblBrand.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        sidebar.add(lblBrand);
        sidebar.add(createNavBtn("🛍 Tất cả sản phẩm", e -> hienTatCa()));
        sidebar.add(createNavBtn("📱 Điện thoại", e -> locDanhMuc(1)));
        sidebar.add(createNavBtn("💻 Laptop", e -> locDanhMuc(2)));
        sidebar.add(createNavBtn("🎧 Phụ kiện", e -> locDanhMuc(3)));
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(createNavBtn("📜 Lịch sử mua hàng", e -> new LichSuMuaHangFrame(maNguoiDung).setVisible(true)));
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        add(sidebar, BorderLayout.WEST);

        // --- TOP BAR ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setPreferredSize(new Dimension(0, 70));
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(236, 240, 241)));

        JLabel lblUser = new JLabel("Xin chào, " + tenNguoiDung + " ");
        lblUser.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblUser.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JButton btnCart = new JButton("🛒 Giỏ hàng");
        btnCart.putClientProperty("JButton.buttonType", "roundRect");
        btnCart.setBackground(new Color(52, 152, 219));
        btnCart.setForeground(Color.WHITE);
        btnCart.addActionListener(e -> new GioHangFrame(maNguoiDung).setVisible(true));

        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        rightActions.setOpaque(false);
        rightActions.add(lblUser);
        rightActions.add(btnCart);

        topBar.add(rightActions, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // --- MAIN CONTENT ---
        mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);

        hienGoiY();

        add(mainContent, BorderLayout.CENTER);
    }

    private JButton createNavBtn(String text, java.awt.event.ActionListener event) {
        JButton b = new JButton(text);
        b.setMaximumSize(new Dimension(200, 45));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.addActionListener(event);
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b.setContentAreaFilled(true);
                b.setBackground(new Color(230, 230, 230));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                b.setContentAreaFilled(false);
            }
        });
        return b;
    }

    private void hienGoiY() {
        List<Integer> ids = AIRecommend.getRecommend(maNguoiDung);
        List<SanPham> dsGoiY = SanPhamDAO.layTheoDanhSachID(ids);
        capNhatMain("🌟 Gợi ý đặc biệt cho bạn", dsGoiY);
    }

    private void capNhatMain(String title, List<SanPham> ds) {
        mainContent.removeAll();

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));
        mainContent.add(lblTitle, BorderLayout.NORTH);

        cardGrid = new JPanel(new GridLayout(0, 4, 20, 20));
        cardGrid.setBackground(Color.WHITE);
        cardGrid.setBorder(BorderFactory.createEmptyBorder(10, 25, 25, 25));

        for (SanPham sp : ds) {
            cardGrid.add(taoCard(sp));
        }

        JScrollPane scroll = new JScrollPane(cardGrid);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        mainContent.add(scroll, BorderLayout.CENTER);

        mainContent.revalidate();
        mainContent.repaint();
    }

    private JPanel taoCard(SanPham sp) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(236, 240, 241), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Image Placeholder/Loading
        JLabel lblImg = new JLabel("", JLabel.CENTER);
        lblImg.setPreferredSize(new Dimension(150, 130));
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/" + sp.getHinhAnh()));
            Image img = icon.getImage().getScaledInstance(140, 110, Image.SCALE_SMOOTH);
            lblImg.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblImg.setText("📦");
            lblImg.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        }

        JLabel lblTen = new JLabel(sp.getTenSanPham());
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTen.setForeground(new Color(44, 62, 80));

        JLabel lblGia = new JLabel(String.format("%,.0f đ", sp.getGia()));
        lblGia.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblGia.setForeground(new Color(231, 76, 60));

        JButton btnXem = new JButton("Chi tiết");
        btnXem.putClientProperty("JButton.buttonType", "roundRect");
        btnXem.addActionListener(e -> {
            HanhViDAO.luuHanhVi(maNguoiDung, sp.getMaSanPham(), "view");
            new ChiTietSanPhamFrame(maNguoiDung, sp).setVisible(true);
        });

        JPanel info = new JPanel(new GridLayout(3, 1, 5, 5));
        info.setOpaque(false);
        info.add(lblTen);
        info.add(lblGia);
        info.add(btnXem);

        card.add(lblImg, BorderLayout.NORTH);
        card.add(info, BorderLayout.CENTER);
        
        return card;
    }

    private void hienTatCa() {
        capNhatMain("🛍 Tất cả sản phẩm", SanPhamDAO.layTatCa());
    }

    private void locDanhMuc(int maDanhMuc) {
        String title = (maDanhMuc == 1) ? "📱 Điện thoại" : (maDanhMuc == 2) ? "💻 Laptop" : "🎧 Phụ kiện";
        capNhatMain(title, SanPhamDAO.layTheoDanhMuc(maDanhMuc));
    }

    private void runTrain() {
        try {
            ProcessBuilder pb = new ProcessBuilder("python", "train.py");
            pb.directory(new File("AI"));
            pb.start().waitFor();
        } catch (Exception e) {}
    }
}
