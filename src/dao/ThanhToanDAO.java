package dao;

import com.mongodb.client.MongoCollection;
import model.GioHang;
import org.bson.Document;
import utils.DBConnection;

import java.util.*;

public class ThanhToanDAO {

    public static void thanhToan(int maND, String diaChi, String phuongThuc) {
        try {
            // 1. Tính tổng tiền
            double tongTien = GioHangDAO.tinhTongTien(maND);

            // 2. Lấy danh sách items để nhúng vào đơn hàng
            List<GioHang> cartItems = GioHangDAO.lay(maND);
            List<Document> orderItems = new ArrayList<>();
            for (GioHang gh : cartItems) {
                orderItems.add(new Document("product_name", gh.getTenSanPham())
                        .append("variant_name", gh.getDungLuong())
                        .append("price", gh.getGia())
                        .append("quantity", gh.getSoLuong()));
            }

            // 3. Tạo ID đơn hàng (Giả lập auto-increment)
            MongoCollection<Document> orderColl = DBConnection.getDatabase().getCollection("orders");
            Document last = orderColl.find().sort(new Document("ma_don_hang", -1)).first();
            int nextId = (last != null) ? last.getInteger("ma_don_hang", 0) + 1 : 1;

            // 4. Tạo Document đơn hàng
            Document orderDoc = new Document("ma_don_hang", nextId)
                    .append("ma_nguoi_dung", maND)
                    .append("ngay_mua", new Date())
                    .append("dia_chi", diaChi)
                    .append("tong_tien", tongTien)
                    .append("phuong_thuc", phuongThuc)
                    .append("trang_thai", "Chờ xử lý")
                    .append("items", orderItems);

            // 5. Lưu vào MongoDB
            orderColl.insertOne(orderDoc);

            // 6. Cập nhật tồn kho (Stock)
            for (GioHang gh : cartItems) {
                // Trừ số lượng trong variant
                DungLuongDAO.truSoLuong(gh.getMaDL());
                // Trừ số lượng tổng trong product
                SanPhamDAO.truSoLuong(gh.getMaSanPham());
            }

            // 7. Xoá giỏ hàng
            GioHangDAO.xoaGioHang(maND);

            System.out.println("Thanh toán thành công (MongoDB) - Mã đơn: " + nextId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
