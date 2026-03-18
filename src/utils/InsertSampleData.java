package utils;

import dao.SanPhamDAO;
import dao.DungLuongDAO;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class InsertSampleData {
    public static void main(String[] args) {
        System.out.println("Bắt đầu thêm dữ liệu mẫu...");
        
        try {
            // ==== PHONES (Danh mục 1) ====
            // iPhone 17 Series
            SanPhamDAO.them("iPhone 17", 22000000, 50, 1, "Mẫu iPhone mới nhất với thiết kế đột phá.", "/src/images/iphone17.png", new ArrayList<>());
            SanPhamDAO.them("iPhone 17 Pro", 28000000, 30, 1, "Hiệu năng đỉnh cao với chip A19 Pro.", "/src/images/iphone17pro.webp", new ArrayList<>());
            SanPhamDAO.them("iPhone 17 Pro Max", 34000000, 40, 1, "Màn hình lớn, pin trâu, camera zoom quang học 5x.", "/src/images/iphone17promax.png", new ArrayList<>());
            
            // iPhone 16 Series
            SanPhamDAO.them("iPhone 16", 19000000, 20, 1, "Thiết kế hiện đại, nhiều màu sắc trẻ trung.", "/src/images/iphone16.png", new ArrayList<>());
            SanPhamDAO.them("iPhone 16 Pro Max", 29000000, 25, 1, "Tuyệt tác thiết kế titan mỏng nhẹ.", "/src/images/iphone16promax.png", new ArrayList<>());
            
            // iPhone 15 & 12
            SanPhamDAO.them("iPhone 15", 16000000, 15, 1, "Camera 48MP sắc nét, Dynamic Island tiện lợi.", "/src/images/iphone15.png", new ArrayList<>());
            SanPhamDAO.them("iPhone 15 Pro Max", 25000000, 20, 1, "Sức mạnh từ chip A17 Pro.", "/src/images/iphone15promax.png", new ArrayList<>());
            SanPhamDAO.them("iPhone 12", 9000000, 10, 1, "Lựa chọn tiết kiệm với hiệu năng vẫn rất tốt.", "/src/images/iphone12.png", new ArrayList<>());

            // Samsung Series
            SanPhamDAO.them("Samsung Galaxy S25 Ultra", 33000000, 30, 1, "Đỉnh cao công nghệ Android với bút S-Pen.", "/src/images/samsungs25ultra.png", new ArrayList<>());
            SanPhamDAO.them("Samsung Galaxy S25", 22000000, 20, 1, "Thiết kế phẳng, tinh tế, Galaxy AI đỉnh cao.", "/src/images/samsungs25.png", new ArrayList<>());
            SanPhamDAO.them("Samsung Galaxy S24", 17000000, 15, 1, "Nhỏ gọn, mạnh mẽ với nhiều tính năng AI.", "/src/images/samsungs24.png", new ArrayList<>());
            SanPhamDAO.them("Samsung Galaxy S23", 12000000, 10, 1, "Mẫu flagship cũ nhưng vẫn rất đáng mua.", "/src/images/samsungs23.jpg", new ArrayList<>());
            SanPhamDAO.them("Samsung Galaxy Z Fold 7", 40000000, 15, 1, "Màn hình gập thế hệ mới, đa nhiệm tuyệt vời.", "/src/images/Samsungxfold7.png", new ArrayList<>());
            SanPhamDAO.them("Samsung Galaxy Z Flip 7", 25000000, 20, 1, "Thiết kế gập dạng vỏ sò thời trang.", "/src/images/samsungzflip7.png", new ArrayList<>());

            // ==== LAPTOPS (Danh mục 2) ====
            SanPhamDAO.them("MacBook Pro M4", 45000000, 15, 2, "Chiếc MacBook Pro mạnh nhất hiện nay.", "/src/images/macbookprom4.png", new ArrayList<>());
            SanPhamDAO.them("MacBook Air M4", 30000000, 20, 2, "Siêu mỏng nhẹ, hiệu năng mạnh mẽ với chip M4.", "/src/images/macbookm4.png", new ArrayList<>());
            SanPhamDAO.them("MacBook Air M3", 25000000, 25, 2, "Cân bằng hoàn hảo giữa thông số và giá thành.", "/src/images/macbookm3.png", new ArrayList<>());
            SanPhamDAO.them("MacBook Air M2", 20000000, 30, 2, "Sự lựa chọn quốc dân cho sinh viên, dân văn phòng.", "/src/images/macbookm2.png", new ArrayList<>());
            SanPhamDAO.them("MacBook Air M5 (Tin đồn)", 35000000, 5, 2, "Mẫu concept MacBook cho tương lai.", "/src/images/macbookm5.png", new ArrayList<>());

            // ==== ACCESSORIES (Danh mục 3) ====
            SanPhamDAO.them("Chuột Gaming không dây", 1500000, 50, 3, "Độ trễ siêu thấp, LED RGB đẹp mắt.", "/src/images/chuotgaming.png", new ArrayList<>());
            SanPhamDAO.them("Bàn phím cơ Gaming", 2500000, 40, 3, "Gõ cực sướng với switch cơ học cao cấp.", "/src/images/banphimgaming.png", new ArrayList<>());
            SanPhamDAO.them("Tai nghe Gaming chụp tai", 1800000, 30, 3, "Âm thanh vòm 7.1, mic chống ồn.", "/src/images/tainghegaming.png", new ArrayList<>());
            SanPhamDAO.them("Tai nghe Bluetooth True Wireless", 1200000, 60, 3, "Chống ồn chủ động ANC, pin lâu.", "/src/images/tainghebluetooth.png", new ArrayList<>());
            SanPhamDAO.them("Củ sạc nhanh 65W", 500000, 100, 3, "Hỗ trợ sạc nhiều thiết bị cùng lúc.", "/src/images/cusac.png", new ArrayList<>());
            SanPhamDAO.them("Dây cáp sạc 2m", 200000, 200, 3, "Dây dù chống đứt siêu bền.", "/src/images/daysac.png", new ArrayList<>());

            System.out.println("Thêm sản phẩm thành công!");
            
            // Xóa biến thể cũ và tạo biến thể mới cho một vài sản phẩm (nếu thích)
            // Lấy san phẩm cuối để minh họa:
            // DungLuongDAO.them(maSanPham, dungLuong, phuPhi, soLuong, hinhAnh);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
