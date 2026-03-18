package utils;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * One-time script to fix image paths in the DB and optionally insert sample products.
 * Run: java -cp "lib/*;bin" utils.FixSampleData
 */
public class FixSampleData {

    private static MongoCollection<Document> getCollection() {
        return DBConnection.getDatabase().getCollection("products");
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Fix Sample Data ===");

        MongoCollection<Document> col = getCollection();

        // 1. Fix wrong image paths
        int fixedCount = 0;
        for (Document doc : col.find()) {
            String img = doc.getString("hinh_anh");
            if (img == null) continue;
            String fixed = null;
            if (img.startsWith("/src/images/")) {
                fixed = "images/" + img.substring("/src/images/".length());
            } else if (img.startsWith("src/images/")) {
                fixed = "images/" + img.substring("src/images/".length());
            }
            if (fixed != null) {
                col.updateOne(Filters.eq("_id", doc.get("_id")),
                        Updates.set("hinh_anh", fixed));
                System.out.println("Fixed: " + img + " -> " + fixed);
                fixedCount++;
            }
        }
        System.out.println("Fixed " + fixedCount + " image paths.");

        // 2. Print all products
        System.out.println("\nAll products in DB:");
        for (Document doc : col.find()) {
            System.out.printf("  ID=%-3d  %-35s  %s%n",
                    doc.getInteger("ma_san_pham", 0),
                    doc.getString("ten_san_pham"),
                    doc.getString("hinh_anh"));
        }

        // 3. If DB is empty, insert sample data
        long count = col.countDocuments();
        System.out.println("\nTotal products: " + count);

        if (count == 0) {
            System.out.println("DB is empty. Inserting sample data...");
            insertSamples(col);
        }

        System.out.println("\nDone!");
    }

    private static void insertSamples(MongoCollection<Document> col) {
        // Helper to get next ID
        Document last = col.find().sort(new Document("ma_san_pham", -1)).first();
        final int[] idCounter = { last != null ? last.getInteger("ma_san_pham", 0) + 1 : 1 };

        addProduct(col, idCounter, "iPhone 17", 22000000, 50, 1,
                "Mau iPhone moi nhat voi thiet ke dot pha.", "images/iphone17.png");
        addProduct(col, idCounter, "iPhone 17 Pro", 28000000, 30, 1,
                "Hieu nang dinh cao voi chip A19 Pro.", "images/iphone17pro.webp");
        addProduct(col, idCounter, "iPhone 17 Pro Max", 34000000, 40, 1,
                "Man hinh lon, pin trau, camera 5x.", "images/iphone17promax.png");
        addProduct(col, idCounter, "iPhone 16", 19000000, 20, 1,
                "Thiet ke hien dai nhieu mau sac.", "images/iphone16.png");
        addProduct(col, idCounter, "iPhone 16 Pro Max", 29000000, 25, 1,
                "Tuyet tac titan mong nhe.", "images/iphone16promax.png");
        addProduct(col, idCounter, "iPhone 15", 16000000, 15, 1,
                "Camera 48MP, Dynamic Island.", "images/iphone15.png");
        addProduct(col, idCounter, "iPhone 15 Pro Max", 25000000, 20, 1,
                "Suc manh chip A17 Pro.", "images/iphone15promax.png");
        addProduct(col, idCounter, "iPhone 12", 9000000, 10, 1,
                "Lua chon tiet kiem hieu nang tot.", "images/iphone12.png");
        addProduct(col, idCounter, "Samsung Galaxy S25 Ultra", 33000000, 30, 1,
                "Dinh cao Android voi but S-Pen.", "images/samsungs25ultra.png");
        addProduct(col, idCounter, "Samsung Galaxy S25", 22000000, 20, 1,
                "Thiet ke phang tinh te Galaxy AI.", "images/samsungs25.png");
        addProduct(col, idCounter, "Samsung Galaxy S24", 17000000, 15, 1,
                "Nho gon manh nhieu tinh nang AI.", "images/samsungs24.png");
        addProduct(col, idCounter, "Samsung Galaxy S23", 12000000, 10, 1,
                "Flagship cu van rat dang mua.", "images/samsungs23.jpg");
        addProduct(col, idCounter, "Samsung Galaxy Z Fold 7", 40000000, 15, 1,
                "Man hinh gap the he moi da nhiem.", "images/Samsungxfold7.png");
        addProduct(col, idCounter, "Samsung Galaxy Z Flip 7", 25000000, 20, 1,
                "Thiet ke gap dang vo so thoi trang.", "images/samsungzflip7.png");
        addProduct(col, idCounter, "MacBook Pro M4", 45000000, 15, 2,
                "MacBook Pro manh nhat hien nay.", "images/macbookprom4.png");
        addProduct(col, idCounter, "MacBook Air M4", 30000000, 20, 2,
                "Sieu mong nhe hieu nang chip M4.", "images/macbookm4.png");
        addProduct(col, idCounter, "MacBook Air M3", 25000000, 25, 2,
                "Can bang hoan hao gia tri thu.", "images/macbookm3.png");
        addProduct(col, idCounter, "MacBook Air M2", 20000000, 30, 2,
                "Lua chon quoc dan sinh vien van phong.", "images/macbookm2.png");
        addProduct(col, idCounter, "MacBook Air M5", 35000000, 5, 2,
                "Mau mac book the he tiep theo.", "images/macbookm5.png");
        addProduct(col, idCounter, "Chuot Gaming Khong Day", 1500000, 50, 3,
                "Do tre sieu thap LED RGB dep mat.", "images/chuotgaming.png");
        addProduct(col, idCounter, "Ban Phim Co Gaming", 2500000, 40, 3,
                "Go cuc suong switch co hoc cao cap.", "images/banphimgaming.png");
        addProduct(col, idCounter, "Tai Nghe Gaming", 1800000, 30, 3,
                "Am thanh vong 7.1 mic chong on.", "images/tainghegaming.png");
        addProduct(col, idCounter, "Tai Nghe Bluetooth ANC", 1200000, 60, 3,
                "Chong on chu dong pin lau.", "images/tainghebluetooth.png");
        addProduct(col, idCounter, "Cu Sac Nhanh 65W", 500000, 100, 3,
                "Ho tro sac nhieu thiet bi cung luc.", "images/cusac.png");
        addProduct(col, idCounter, "Day Cap Sac 2m", 200000, 200, 3,
                "Day du chong dut sieu ben.", "images/daysac.png");

        System.out.println("Inserted sample products!");
    }

    private static void addProduct(MongoCollection<Document> col, int[] idCounter,
            String name, double price, int qty, int catId, String desc, String img) {
        Document doc = new Document("ma_san_pham", idCounter[0]++)
                .append("ten_san_pham", name)
                .append("gia", price)
                .append("so_luong", qty)
                .append("ma_danh_muc", catId)
                .append("mo_ta", desc)
                .append("hinh_anh", img)
                .append("danh_sach_hinh_anh", new ArrayList<>());
        col.insertOne(doc);
    }
}
