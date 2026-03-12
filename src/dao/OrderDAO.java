package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import utils.DBConnection;

import java.util.*;

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
