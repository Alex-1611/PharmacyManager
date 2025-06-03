package Util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static final String CSV_FILE = "audit.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void logAction(String actionName) {
        try (FileWriter writer = new FileWriter(CSV_FILE, true)) {
            String timestamp = LocalDateTime.now().format(FORMATTER);
            writer.append(actionName).append(",").append(timestamp).append("\n");
        } catch (IOException e) {
            System.err.println("Error writing to audit CSV: " + e.getMessage());
        }
    }
}
