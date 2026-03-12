package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import model.SanPham;
import org.bson.Document;
import utils.DBConnection;

import java.util.*;

public class SanPhamDAO {

    private static MongoCollection<Document> getCollection() {
        return DBConnection.getDatabase().getCollection("products");
    }

    // =================================================
    // LẤY TẤT CẢ (MongoDB)
    // =================================================
    public static List<SanPham> layTatCa() {
        List<SanPham> ds = new ArrayList<>();
        try {
            for (Document doc : getCollection().find()) {
                ds.add(map(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public static void them(String ten, double gia, int soLuong, int maDanhMuc, String moTa, String hinhAnh, List<String> danhSachHinhAnh) {
        try {
            Document last = getCollection().find().sort(new Document("ma_san_pham", -1)).first();
            int nextId = (last != null) ? last.getInteger("ma_san_pham", 0) + 1 : 1;

            Document doc = new Document("ma_san_pham", nextId)
                    .append("ten_san_pham", ten)
                    .append("gia", gia)
                    .append("so_luong", soLuong)
                    .append("ma_danh_muc", maDanhMuc)
                    .append("mo_ta", moTa)
                    .append("hinh_anh", hinhAnh)
                    .append("danh_sach_hinh_anh", danhSachHinhAnh);
            
            getCollection().insertOne(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =================================================
    // ⭐ TÌM THEO ID (MongoDB)
    // =================================================
    public static SanPham timTheoId(int id) {
        try {
            Document doc = getCollection().find(Filters.eq("ma_san_pham", id)).first();
            if (doc != null) return map(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // =================================================
    // ⭐⭐ TỐI ƯU CHO AI – LẤY NHIỀU ID 1 LẦN
    // =================================================
    public static List<SanPham> layTheoDanhSachID(List<Integer> ids) {
        List<SanPham> ds = new ArrayList<>();
        if (ids == null || ids.isEmpty()) return ds;

        try {
            for (Document doc : getCollection().find(Filters.in("ma_san_pham", ids))) {
                ds.add(map(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    // =================================================
    // ⭐ TRỪ TỒN KHO
    // =================================================
    public static boolean truSoLuong(int maSanPham) {
        try {
            long modified = getCollection().updateOne(
                    Filters.and(Filters.eq("ma_san_pham", maSanPham), Filters.gt("so_luong", 0)),
                    new Document("$inc", new Document("so_luong", -1))
            ).getModifiedCount();
            
            return modified > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // =================================================
    // MAP Document -> SanPham
    // =================================================
    @SuppressWarnings("unchecked")
    private static SanPham map(Document doc) {
        SanPham sp = new SanPham();
        sp.setMaSanPham(doc.getInteger("ma_san_pham", 0));
        sp.setTenSanPham(doc.getString("ten_san_pham"));
        sp.setGia(doc.getDouble("gia"));
        sp.setSoLuong(doc.getInteger("so_luong", 0));
        sp.setMaDanhMuc(doc.getInteger("ma_danh_muc", 0));
        sp.setMoTa(doc.getString("mo_ta"));
        sp.setHinhAnh(doc.getString("hinh_anh"));
        sp.setDanhSachHinhAnh((List<String>) doc.get("danh_sach_hinh_anh"));
        return sp;
    }

    public static void xoa(int ma) {
        try {
            getCollection().deleteOne(Filters.eq("ma_san_pham", ma));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =================================================
    // LẤY THEO DANH MỤC
    // =================================================
    public static List<SanPham> layTheoDanhMuc(int maDanhMuc) {
        List<SanPham> ds = new ArrayList<>();
        try {
            for (Document doc : getCollection().find(Filters.eq("ma_danh_muc", maDanhMuc))) {
                ds.add(map(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }
}
