package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import model.GioHang;
import org.bson.Document;
import utils.DBConnection;

import java.util.*;

public class GioHangDAO {

    private static MongoCollection<Document> getCollection() {
        return DBConnection.getDatabase().getCollection("carts");
    }

    // ================= THÊM GIỎ =================
    public static void them(int maND, int maSP, int maDL) {
        try {
            // Kiểm tra xem item đã có trong giỏ chưa
            Document filter = new Document("ma_nguoi_dung", maND)
                    .append("ma_san_pham", maSP)
                    .append("ma_dl", maDL);
            
            Document existing = getCollection().find(filter).first();
            
            if (existing != null) {
                getCollection().updateOne(filter, new Document("$inc", new Document("so_luong", 1)));
            } else {
                Document doc = new Document("ma_nguoi_dung", maND)
                        .append("ma_san_pham", maSP)
                        .append("ma_dl", maDL)
                        .append("so_luong", 1);
                getCollection().insertOne(doc);
            }
            System.out.println("Đã thêm giỏ hàng OK (MongoDB)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= LẤY GIỎ =================
    public static List<GioHang> lay(int maND) {
        List<GioHang> ds = new ArrayList<>();
        try {
            for (Document cartDoc : getCollection().find(Filters.eq("ma_nguoi_dung", maND))) {
                int maSP = cartDoc.getInteger("ma_san_pham");
                int maDL = cartDoc.getInteger("ma_dl");
                int quantity = cartDoc.getInteger("so_luong");

                // Lookup SanPham
                Document spDoc = DBConnection.getDatabase().getCollection("products")
                        .find(Filters.eq("ma_san_pham", maSP)).first();
                
                // Lookup Variant
                Document dlDoc = DBConnection.getDatabase().getCollection("product_variants")
                        .find(Filters.eq("ma_dl", maDL)).first();

                if (spDoc != null && dlDoc != null) {
                    GioHang g = new GioHang();
                    g.setMaSanPham(maSP);
                    g.setMaDL(maDL);
                    g.setTenSanPham(spDoc.getString("ten_san_pham"));
                    g.setDungLuong(dlDoc.getString("dung_luong"));
                    g.setGia(spDoc.getDouble("gia") + dlDoc.getDouble("phu_phi"));
                    g.setSoLuong(quantity);
                    ds.add(g);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public static double tinhTongTien(int maND) {
        double tong = 0;
        try {
            for (Document cartDoc : getCollection().find(Filters.eq("ma_nguoi_dung", maND))) {
                int maSP = cartDoc.getInteger("ma_san_pham");
                int maDL = cartDoc.getInteger("ma_dl");
                int quantity = cartDoc.getInteger("so_luong");

                Document spDoc = DBConnection.getDatabase().getCollection("products")
                        .find(Filters.eq("ma_san_pham", maSP)).first();
                Document dlDoc = DBConnection.getDatabase().getCollection("product_variants")
                        .find(Filters.eq("ma_dl", maDL)).first();

                if (spDoc != null && dlDoc != null) {
                    tong += (spDoc.getDouble("gia") + dlDoc.getDouble("phu_phi")) * quantity;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tong;
    }

    // ⭐ Trong MongoDB, chúng ta không dùng Transaction SQL ở đây vì cấu trúc document
    // Tuy nhiên để giữ tương thích, ta sẽ nhận MongoDatabase (thay vì Connection)
    public static void xoaGioHang(int maND) {
        try {
            getCollection().deleteMany(Filters.eq("ma_nguoi_dung", maND));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
