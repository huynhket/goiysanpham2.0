package utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class DBConnection {

    private static MongoClient mongoClient = null;
    private static final String DATABASE_NAME = "GOI_Y_SAN_PHAM";

    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            try {
                // Kết nối tới MongoDB localhost:27017
                mongoClient = MongoClients.create("mongodb://localhost:27017");
                // Thử list database để thực sự kiểm tra kết nối
                mongoClient.listDatabaseNames().first(); 
                System.out.println("Connected to MongoDB successfully!");
            } catch (Exception e) {
                System.err.println("!!! LỖI KẾT NỐI MONGODB: " + e.getMessage());
                System.err.println("Hãy đảm bảo bạn đã cài đặt và khởi động MongoDB Server (mongod).");
                mongoClient = null; // Reset to allow retry
                e.printStackTrace();
                return null; // Return null if connection fails
            }
        }
        return mongoClient != null ? mongoClient.getDatabase(DATABASE_NAME) : null;
    }
    
    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }
}
