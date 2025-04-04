package org.example.config;

import java.io.File;
import java.nio.file.Paths;

public class AppConfig {

    public static final String DOWNLOAD_PATH;

    static {
        DOWNLOAD_PATH = Paths.get(
                System.getProperty("user.home"),
                "Downloads",
                "JavaIDM"
        ).toString();

        File downloadDir = new File(DOWNLOAD_PATH);
        if (!downloadDir.exists()) {
            boolean created = downloadDir.mkdirs();
            if (created) {
                System.out.println("Created download directory: " + DOWNLOAD_PATH);
            } else {
                System.err.println("Failed to create download directory at: " + DOWNLOAD_PATH);
            }
        }
    }
}
