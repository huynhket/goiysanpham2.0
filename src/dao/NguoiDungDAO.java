package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import model.NguoiDung;
import org.bson.Document;
import utils.DBConnection;

public class NguoiDungDAO {

    // =================================================
    // ĐĂNG NHẬP → TRẢ VỀ OBJECT NGƯỜI DÙNG (MongoDB)
    // =================================================
    public static NguoiDung dangNhap(String tenDangNhap, String matKhau) {
        try {
            System.out.println("Đang thử đăng nhập với user: [" + tenDangNhap + "]");
            
            com.mongodb.client.MongoDatabase database = DBConnection.getDatabase();
            if (database == null) {
                System.err.println("!!! Lỗi: Không thể kết nối tới cơ sở dữ liệu.");
                return null;
            }

            MongoCollection<Document> collection = database.getCollection("users");

            Document doc = collection.find(Filters.and(
                    Filters.eq("ten_dang_nhap", tenDangNhap),
                    Filters.eq("mat_khau", matKhau)
            )).first();

            if (doc != null) {
                System.out.println("Đăng nhập thành công cho user: " + tenDangNhap);
                return map(doc);
            } else {
                System.out.println("Sai tài khoản hoặc mật khẩu.");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi truy vấn đăng nhập: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public static boolean kienTraTonTai(String tenDangNhap) {
        try {
            Document doc = getCollection().find(Filters.eq("ten_dang_nhap", tenDangNhap)).first();
            return doc != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static MongoCollection<Document> getCollection() {
        return DBConnection.getDatabase().getCollection("users");
    }

    public static java.util.List<NguoiDung> layTatCa() {
        java.util.List<NguoiDung> ds = new java.util.ArrayList<>();
        try {
            for (Document doc : getCollection().find()) {
                ds.add(map(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public static void them(String user, String pass, String name, String role) {
        try {
            Document last = getCollection().find().sort(new Document("ma_nguoi_dung", -1)).first();
            int nextId = (last != null) ? last.getInteger("ma_nguoi_dung", 0) + 1 : 1;

            Document doc = new Document("ma_nguoi_dung", nextId)
                    .append("ten_dang_nhap", user)
                    .append("mat_khau", pass)
                    .append("ho_ten", name)
                    .append("vai_tro", role);
            getCollection().insertOne(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void xoa(int ma) {
        try {
            getCollection().deleteOne(Filters.eq("ma_nguoi_dung", ma));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static NguoiDung map(Document doc) {
        NguoiDung nd = new NguoiDung();
        nd.setMaNguoiDung(doc.getInteger("ma_nguoi_dung", 0));
        nd.setTenDangNhap(doc.getString("ten_dang_nhap"));
        nd.setMatKhau(doc.getString("mat_khau"));
        nd.setHoTen(doc.getString("ho_ten"));
        nd.setVaiTro(doc.getString("vai_tro"));
        return nd;
    }
}
