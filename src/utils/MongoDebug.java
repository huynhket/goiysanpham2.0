package utils;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class MongoDebug {
    public static void main(String[] args) {
        try {
            System.out.println("--- DEBUG MONGODB CONNECTION ---");
            MongoDatabase db = DBConnection.getDatabase();
            System.out.println("Target Database: " + db.getName());
            
            MongoCollection<Document> collection = db.getCollection("users");
            long count = collection.countDocuments();
            System.out.println("Users Count: " + count);
            
            List<Document> users = collection.find().into(new ArrayList<>());
            for (Document u : users) {
                System.out.println("User: " + u.toJson());
            }
            System.out.println("--- END DEBUG ---");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
