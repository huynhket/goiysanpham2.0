package dao;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import utils.DBConnection;

public class HanhViDAO {

    // =================================================
    // LƯU HÀNH VI NGƯỜI DÙNG (AI dùng bảng này)
    // =================================================
    public static void luuHanhVi(int maNguoiDung, int maSanPham, String hanhDong) {
        try {
            MongoCollection<Document> collection = DBConnection.getDatabase().getCollection("behaviors");

            Document doc = new Document("ma_nguoi_dung", maNguoiDung)
                    .append("ma_san_pham", maSanPham)
                    .append("hanh_dong", hanhDong)
                    .append("timestamp", new java.util.Date());

            collection.insertOne(doc);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
