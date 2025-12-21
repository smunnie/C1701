package app;

import java.io.IOException;
import java.nio.file.*;

/**
 * This class is responsible for managing attachment files for the application.
 * - Attachments are stored in ONE folder called "attachments/"
 * - This folder is RESET every time the app starts
 */
public class AttachmentStorage {
     // Path to the single attachments folder.
    public static final Path ATTACHMENTS_DIR = Paths.get("attachments/temp");
    /**
     * This method is called ONCE when the app starts.
     *
     * What it does:
     * 1. If the attachments folder already exists:
     *    - Delete ALL files inside it
     * 2. Re-create the attachments folder
     *
     * This guarantees:
     * - A clean state every time the app runs
     * - Old attachments from previous runs are removed
     */
    public static void init() {

        try {
            // Check if attachments folder already exists
            if (Files.exists(ATTACHMENTS_DIR)) {

                // Files.walk() gives us a stream of all files/folders inside
                // try-with-resources ensures the stream is CLOSED properly
                try (var paths = Files.walk(ATTACHMENTS_DIR)) {

                    // Only delete files (not folders)
                    paths.filter(Files::isRegularFile)
                            .forEach(p -> {
                                try {
                                    Files.delete(p); // delete each file
                                } catch (IOException e) {
                                    // If a file fails to delete, print a simple message
                                    System.err.println(
                                            "Failed to delete attachment: " + p
                                    );
                                }
                            });
                }
            }

            // Ensure the attachments directory exists
            // If it already exists, this does nothing
            Files.createDirectories(ATTACHMENTS_DIR);

        } catch (IOException e) {
            // This should rarely happen, but we handle it safely
            System.err.println(
                    "Failed to initialise attachments directory."
            );
        }
    }
}
