import java.nio.file.*;
import java.nio.charset.StandardCharsets;

public class EscapeUnicode {
    public static void main(String[] args) throws Exception {
        Files.walk(Paths.get("src/view"))
            .filter(Files::isRegularFile)
            .filter(p -> p.toString().endsWith(".java"))
            .forEach(p -> {
                try {
                    String content = new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
                    StringBuilder sb = new StringBuilder();
                    boolean changed = false;
                    for (char c : content.toCharArray()) {
                        if (c > 127) {
                            sb.append(String.format("\\u%04x", (int) c));
                            changed = true;
                        } else {
                            sb.append(c);
                        }
                    }
                    if (changed) {
                        Files.write(p, sb.toString().getBytes(StandardCharsets.UTF_8));
                        System.out.println("Updated " + p);
                    }
                } catch (Exception e) {}
            });
    }
}
