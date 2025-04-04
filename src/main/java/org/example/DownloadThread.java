package org.example;

import org.example.models.FileInfo;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadThread extends Thread {

    private final FileInfo file;
    private final DownloadManager manager;

    public DownloadThread(FileInfo file, DownloadManager manager) {
        this.file = file;
        this.manager = manager;
    }

    @Override
    public void run() {
        BufferedInputStream bis = null;
        FileOutputStream fos = null;

        try {
            file.setStatus("DOWNLOADING");
            manager.updateUI(file);

            URL url = new URL(file.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10_000); // 10 seconds timeout
            connection.setReadTimeout(10_000);

            int fileSize = connection.getContentLength();
            if (fileSize <= 0) {
                throw new IOException("Invalid file size.");
            }

            bis = new BufferedInputStream(connection.getInputStream());
            fos = new FileOutputStream(file.getPath());

            byte[] buffer = new byte[1024];
            int bytesRead;
            double downloaded = 0;

            while ((bytesRead = bis.read(buffer, 0, buffer.length)) != -1) {
                fos.write(buffer, 0, bytesRead);
                downloaded += bytesRead;

                double percent = (downloaded / fileSize) * 100;
                file.setPer(String.valueOf(percent));
                manager.updateUI(file);
            }

            file.setStatus("DONE");
            setName("Thread-" + file.getIndex());
        } catch (IOException e) {
            file.setStatus("FAILED");
            System.err.println("Download failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) fos.close();
                if (bis != null) bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            manager.updateUI(file);
        }
    }
}
