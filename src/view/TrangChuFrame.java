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

    private static final String FONT = "Segoe UI";
    private int maNguoiDung;
    private JPanel mainContent;
    private JPanel cardGrid;

    public TrangChuFrame(int maNguoiDung, String tenNguoiDung) {
        this.maNguoiDung = maNguoiDung;

        // Apply Theme
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {}

        // Set window icon (logo)
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/logo.png"));
            setIconImage(logoIcon.getImage());
        } catch (Exception e) {}

        runTrain();

        setTitle("SmartShop - G\u1ee3i \u00dd S\u1ea3n Ph\u1ea9m");
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- SIDEBAR / NAVIGATION ---
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(30, 39, 46));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(30, 39, 46)));

        // Brand label with logo icon
        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 30));
        brandPanel.setBackground(new Color(30, 39, 46));
        brandPanel.setMaximumSize(new Dimension(240, 90));

        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/logo.png"));
            Image img = logoIcon.getImage().getScaledInstance(36, 36, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(img));
            brandPanel.add(lblLogo);
        } catch (Exception e) {}

        JLabel lblBrand = new JLabel("SmartShop");
        lblBrand.setFont(new Font(FONT, Font.BOLD, 22));
        lblBrand.setForeground(Color.WHITE);
        brandPanel.add(lblBrand);

        sidebar.add(brandPanel);
        sidebar.add(createNavBtn("\uD83D\uDECD T\u1ea5t c\u1ea3 s\u1ea3n ph\u1ea9m", e -> hienTatCa()));
        sidebar.add(createNavBtn("\uD83D\uDCF1 \u0110i\u1ec7n tho\u1ea1i", e -> locDanhMuc(1)));
        sidebar.add(createNavBtn("\uD83D\uDCBB Laptop", e -> locDanhMuc(2)));
        sidebar.add(createNavBtn("\uD83C\uDFA7 Ph\u1ee5 ki\u1ec7n", e -> locDanhMuc(3)));
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(createNavBtn("\uD83D\uDCDC L\u1ecbch s\u1eed mua h\u00e0ng", e -> new LichSuMuaHangFrame(maNguoiDung).setVisible(true)));
        sidebar.add(createNavBtn("\uD83D\uDEAA \u0110\u0103ng xu\u1ea5t", e -> {
            dispose();
            new DangNhap().setVisible(true);
        }));
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        add(sidebar, BorderLayout.WEST);

        // --- TOP BAR ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setPreferredSize(new Dimension(0, 70));
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(236, 240, 241)));

        JPanel leftActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        leftActions.setOpaque(false);
        try {
            ImageIcon topLogoIcon = new ImageIcon(getClass().getResource("/images/logo.png"));
            Image topImg = topLogoIcon.getImage().getScaledInstance(36, 36, Image.SCALE_SMOOTH);
            JLabel lblTopLogo = new JLabel(new ImageIcon(topImg));
            leftActions.add(lblTopLogo);
        } catch (Exception e) {}
        JLabel lblTopTitle = new JLabel("SmartShop");
        lblTopTitle.setFont(new Font(FONT, Font.BOLD, 18));
        lblTopTitle.setForeground(new Color(44, 62, 80));
        leftActions.add(lblTopTitle);
        topBar.add(leftActions, BorderLayout.WEST);

        JLabel lblUser = new JLabel("Xin ch\u00e0o, " + tenNguoiDung + " ");
        lblUser.setFont(new Font(FONT, Font.ITALIC, 14));
        lblUser.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JButton btnCart = new JButton("<html><nobr><span style='font-family: \"Segoe UI Emoji\";'>\uD83D\uDED2</span> <span style='font-family: \"Segoe UI\";'>Gi\u1ecf h\u00e0ng</span></nobr></html>");
        btnCart.setPreferredSize(new Dimension(130, 40));
        btnCart.putClientProperty("JButton.buttonType", "roundRect");
        btnCart.setBackground(new Color(52, 152, 219));
        btnCart.setForeground(Color.WHITE);
        btnCart.setFont(new Font(FONT, Font.BOLD, 14));
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
        String formattedText = text;
        int spaceIdx = text.indexOf(' ');
        if (spaceIdx > 0 && text.length() > 2) {
            String emoji = text.substring(0, spaceIdx);
            String label = text.substring(spaceIdx + 1);
            formattedText = "<html><nobr><span style='font-family: \"Segoe UI Emoji\";'>" + emoji + "</span>&nbsp;<span style='font-family: \"Segoe UI\";'>" + label + "</span></nobr></html>";
        }
        JButton b = new JButton(formattedText);
        b.setMaximumSize(new Dimension(240, 50));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFont(new Font(FONT, Font.PLAIN, 15));
        b.setForeground(new Color(189, 195, 199));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        b.addActionListener(event);
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b.setContentAreaFilled(true);
                b.setBackground(new Color(72, 84, 96));
                b.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                b.setContentAreaFilled(false);
                b.setForeground(new Color(189, 195, 199));
            }
        });
        return b;
    }

    private void hienGoiY() {
        List<Integer> ids = AIRecommend.getRecommend(maNguoiDung);
        if (ids == null || ids.isEmpty()) {
            capNhatMain("\uD83D\uDECD C\u00f3 th\u1ec3 b\u1ea1n s\u1ebd th\u00edch", SanPhamDAO.layTatCa());
            return;
        }
        List<SanPham> dsGoiY = SanPhamDAO.layTheoDanhSachID(ids);
        if (dsGoiY == null || dsGoiY.isEmpty()) {
            capNhatMain("\uD83D\uDECD C\u00f3 th\u1ec3 b\u1ea1n s\u1ebd th\u00edch", SanPhamDAO.layTatCa());
        } else {
            capNhatMain("\uD83C\uDF1F G\u1ee3i \u00fd \u0111\u1eb7c bi\u1ec7t cho b\u1ea1n", dsGoiY);
        }
    }

    private void capNhatMain(String title, List<SanPham> ds) {
        mainContent.removeAll();

        String formattedTitle = title;
        int spaceIdx = title.indexOf(' ');
        if (spaceIdx > 0 && title.length() > 2) {
            String emoji = title.substring(0, spaceIdx);
            String label = title.substring(spaceIdx + 1);
            formattedTitle = "<html><nobr><span style='font-family: \"Segoe UI Emoji\";'>" + emoji + "</span> <span style='font-family: \"Segoe UI\";'>" + label + "</span></nobr></html>";
        }
        JLabel lblTitle = new JLabel(formattedTitle);
        lblTitle.setFont(new Font(FONT, Font.BOLD, 20));
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
            BorderFactory.createLineBorder(new Color(223, 228, 234), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblImg = new JLabel("", JLabel.CENTER);
        lblImg.setPreferredSize(new Dimension(150, 150));
        lblImg.setBackground(new Color(245, 246, 250));
        lblImg.setOpaque(true);
        try {
            java.net.URL imgUrl = getClass().getResource("/" + sp.getHinhAnh());
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image img = icon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
                lblImg.setIcon(new ImageIcon(img));
            } else {
                lblImg.setText("No Image");
                lblImg.setFont(new Font(FONT, Font.PLAIN, 13));
                lblImg.setForeground(new Color(150, 150, 150));
            }
        } catch (Exception e) {
            lblImg.setText("No Image");
            lblImg.setFont(new Font(FONT, Font.PLAIN, 13));
            lblImg.setForeground(new Color(150, 150, 150));
        }

        JLabel lblTen = new JLabel(sp.getTenSanPham());
        lblTen.setFont(new Font(FONT, Font.BOLD, 15));
        lblTen.setForeground(new Color(47, 53, 66));
        lblTen.setToolTipText(sp.getTenSanPham());

        JLabel lblGia = new JLabel(String.format("%,.0f đ", sp.getGia()));
        lblGia.setFont(new Font(FONT, Font.BOLD, 17));
        lblGia.setForeground(new Color(231, 76, 60));

        JButton btnXem = new JButton("Kh\u00e1m Ph\u00e1");
        btnXem.setBackground(new Color(52, 152, 219));
        btnXem.setForeground(Color.WHITE);
        btnXem.setFont(new Font(FONT, Font.BOLD, 13));
        btnXem.setFocusPainted(false);
        btnXem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXem.addActionListener(e -> {
            HanhViDAO.luuHanhVi(maNguoiDung, sp.getMaSanPham(), "view");
            new ChiTietSanPhamFrame(maNguoiDung, sp).setVisible(true);
        });

        JPanel info = new JPanel(new GridLayout(3, 1, 5, 8));
        info.setOpaque(false);
        info.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        info.add(lblTen);
        info.add(lblGia);
        info.add(btnXem);

        card.add(lblImg, BorderLayout.NORTH);
        card.add(info, BorderLayout.CENTER);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(52, 152, 219), 2, true),
                    BorderFactory.createEmptyBorder(14, 14, 14, 14)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(223, 228, 234), 1, true),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
            }
        });

        return card;
    }

    private void hienTatCa() {
        capNhatMain("\uD83D\uDECD T\u1ea5t c\u1ea3 s\u1ea3n ph\u1ea9m", SanPhamDAO.layTatCa());
    }

    private void locDanhMuc(int maDanhMuc) {
        String title = (maDanhMuc == 1) ? "\uD83D\uDCF1 \u0110i\u1ec7n tho\u1ea1i" : (maDanhMuc == 2) ? "\uD83D\uDCBB Laptop" : "\uD83C\uDFA7 Ph\u1ee5 ki\u1ec7n";
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
