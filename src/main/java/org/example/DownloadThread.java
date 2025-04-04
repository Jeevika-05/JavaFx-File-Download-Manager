package org.example;

import org.example.models.FileInfo;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadThread extends Thread {

    private final FileInfo file;
    private final DownloadManager manager;

    private volatile boolean isPaused = false;
    private volatile boolean isCancelled = false;

    public DownloadThread(FileInfo file, DownloadManager manager) {
        this.file = file;
        this.manager = manager;
    }

    public void pauseDownload() {
        this.isPaused = true;
        file.setStatus("PAUSED");
        manager.updateUI(file);
    }

    public void resumeDownload() {
        this.isPaused = false;
        file.setStatus("RESUMING");
        manager.updateUI(file);
    }

    public void cancelDownload() {
        this.isCancelled = true;
        file.setStatus("CANCELLED");
        manager.updateUI(file);
    }

    @Override
    public void run() {
        file.setStatus("DOWNLOADING");
        manager.updateUI(file);

        try (BufferedInputStream bis = new BufferedInputStream(new URL(file.getUrl()).openStream());
             FileOutputStream fos = new FileOutputStream(file.getPath())) {

            URLConnection urlConnection = new URL(file.getUrl()).openConnection();
            int fileSize = urlConnection.getContentLength();

            byte[] buffer = new byte[1024];
            int countByte;
            double downloadedBytes = 0;

            while (!isCancelled && (countByte = bis.read(buffer, 0, 1024)) != -1) {

                while (isPaused) {
                    Thread.sleep(200);
                }

                fos.write(buffer, 0, countByte);
                downloadedBytes += countByte;

                if (fileSize > 0) {
                    double percentage = (downloadedBytes / fileSize) * 100;
                    file.setPer(String.valueOf(percentage));
                    manager.updateUI(file);
                }
            }

            if (isCancelled) {
                file.setStatus("CANCELLED");
            } else {
                file.setStatus("DONE");
            }

        } catch (IOException | InterruptedException e) {
            file.setStatus("FAILED");
            e.printStackTrace();
        }

        manager.updateUI(file);
    }
}
