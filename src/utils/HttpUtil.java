package utils;

import java.io.*;
import java.net.*;

public class HttpUtil {

    public static String get(String urlStr) throws Exception {
        URL url = new URI(urlStr).toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");

        BufferedReader in =
                new BufferedReader(new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuilder content = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        return content.toString();
    }
}
