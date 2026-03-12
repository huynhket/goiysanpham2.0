package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import model.LichSuMuaHang;
import org.bson.Document;
import utils.DBConnection;

import java.text.SimpleDateFormat;
import java.util.*;

public class LichSuMuaHangDAO {

    public static ArrayList<LichSuMuaHang> getHistory(int maNguoiDung) {
        ArrayList<LichSuMuaHang> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try {
            MongoCollection<Document> collection = DBConnection.getDatabase().getCollection("orders");

            for (Document orderDoc : collection.find(Filters.eq("ma_nguoi_dung", maNguoiDung))) {
                int maDon = orderDoc.getInteger("ma_don_hang");
                Date ngayDate = orderDoc.getDate("ngay_mua");
                String ngay = (ngayDate != null) ? sdf.format(ngayDate) : "N/A";
                String status = orderDoc.getString("trang_thai");

                List<Document> items = orderDoc.getList("items", Document.class);
                if (items != null) {
                    for (Document itemDoc : items) {
                        LichSuMuaHang ls = new LichSuMuaHang(
                                maDon,
                                itemDoc.getString("product_name"),
                                itemDoc.getInteger("quantity"),
                                itemDoc.getDouble("price"),
                                ngay,
                                status
                        );
                        list.add(ls);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}