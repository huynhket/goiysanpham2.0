package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import utils.DBConnection;

import java.util.*;
import java.time.LocalDate;
import java.time.ZoneId;

public class OrderDAO {

    private static MongoCollection<Document> getCollection() {
        return DBConnection.getDatabase().getCollection("orders");
    }

    public static List<Document> layTatCa() {
        List<Document> ds = new ArrayList<>();
        try {
            for (Document doc : getCollection().find().sort(new Document("ma_don_hang", -1))) {
                ds.add(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public static List<Document> layDonHangTrongThangNay() {
        List<Document> ds = new ArrayList<>();
        try {
            LocalDate now = LocalDate.now();
            LocalDate firstDayOfMonth = now.withDayOfMonth(1);
            LocalDate firstDayOfNextMonth = now.plusMonths(1).withDayOfMonth(1);

            Date startDate = Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(firstDayOfNextMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Tìm những đơn hàng có ngày mua nằm trong khoảng từ đầu tháng đến < đầu tháng sau
            // Và trạng thái có thể là Hoàn thành (hoặc lấy tất cả, tuỳ logic, ở đây lấy tất cả)
            for (Document doc : getCollection().find(Filters.and(
                    Filters.gte("ngay_mua", startDate),
                    Filters.lt("ngay_mua", endDate)
            )).sort(new Document("ngay_mua", -1))) {
                ds.add(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public static void capNhatTrangThai(int maDonHang, String trangThai) {
        try {
            getCollection().updateOne(
                    Filters.eq("ma_don_hang", maDonHang),
                    Updates.set("trang_thai", trangThai)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
