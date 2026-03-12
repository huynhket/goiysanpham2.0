package ai;

import java.io.*;
import java.util.*;

public class AIRecommend {

    public static List<Integer> getRecommend(int userId) {

        List<Integer> list = new ArrayList<>();

        try {
            // ====== PATH CHUẨN CỦA BẠN ======
            String python = "python";
            String script = "recommend.py";

            File workingDir = new File("AI");

            System.out.println("Run: " + python + " " + script + " " + userId);

            // ====== ProcessBuilder (ổn định nhất) ======
            ProcessBuilder pb = new ProcessBuilder(
                    python,
                    script,
                    String.valueOf(userId)
            );

            // ⭐ QUAN TRỌNG: để python tìm model.pkl
            pb.directory(workingDir);

            // gộp error + output
            pb.redirectErrorStream(true);

            Process p = pb.start();

            BufferedReader br =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            String result = null;

            while ((line = br.readLine()) != null) {
                System.out.println("PYTHON OUT: " + line);
                if (line.trim().startsWith("[")) {
                    result = line.trim();
                }
            }

            int exitCode = p.waitFor();
            if (exitCode != 0) {
                System.err.println("PYTHON error: Script exited with code " + exitCode);
            }

            // ====== parse [1, 2, 3] ======
            if (result != null && result.startsWith("[")) {
                String cleanResult = result.replace("[", "").replace("]", "").trim();
                if (!cleanResult.isEmpty()) {
                    for (String s : cleanResult.split(",")) {
                        try {
                            list.add(Integer.parseInt(s.trim()));
                        } catch (NumberFormatException nfe) {
                            System.err.println("Failed to parse recommendation ID: " + s);
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("AIRecommend Exception: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }
}
