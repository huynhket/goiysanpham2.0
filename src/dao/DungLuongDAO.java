package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import model.DungLuong;
import org.bson.Document;
import utils.DBConnection;

import java.util.*;

public class DungLuongDAO {

    private static MongoCollection<Document> getCollection() {
        return DBConnection.getDatabase().getCollection("product_variants");
    }

    public static List<DungLuong> layTheoSanPham(int maSP) {
        List<DungLuong> list = new ArrayList<>();
        try {
            for (Document doc : getCollection().find(Filters.eq("ma_san_pham", maSP))) {
                list.add(map(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean truSoLuong(int maDL) {
        try {
            long modified = getCollection().updateOne(
                    Filters.and(Filters.eq("ma_dl", maDL), Filters.gt("so_luong", 0)),
                    new Document("$inc", new Document("so_luong", -1))
            ).getModifiedCount();
            return modified > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<DungLuong> layTatCa() {
        List<DungLuong> list = new ArrayList<>();
        try {
            for (Document doc : getCollection().find()) {
                list.add(map(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void them(int maSP, String dungLuong, double phuPhi, int soLuong, String hinhAnh) {
        try {
            Document last = getCollection().find().sort(new Document("ma_dl", -1)).first();
            int nextId = (last != null) ? last.getInteger("ma_dl", 0) + 1 : 1;

            Document doc = new Document("ma_dl", nextId)
                    .append("ma_san_pham", maSP)
                    .append("dung_luong", dungLuong)
                    .append("phu_phi", phuPhi)
                    .append("so_luong", soLuong)
                    .append("hinh_anh", hinhAnh);
            getCollection().insertOne(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void xoa(int maDL) {
        try {
            getCollection().deleteOne(Filters.eq("ma_dl", maDL));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static DungLuong map(Document doc) {
        DungLuong dl = new DungLuong();
        dl.setMaDL(doc.getInteger("ma_dl", 0));
        dl.setMaSanPham(doc.getInteger("ma_san_pham", 0));
        dl.setDungLuong(doc.getString("dung_luong"));
        dl.setPhuPhi(doc.getDouble("phu_phi"));
        dl.setSoLuong(doc.getInteger("so_luong", 0));
        dl.setHinhAnh(doc.getString("hinh_anh"));
        return dl;
    }
}
