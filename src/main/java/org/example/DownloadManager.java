package org.example;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.example.config.AppConfig;
import org.example.models.FileInfo;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

public class DownloadManager {

    @FXML
    private TableView<FileInfo> tableView;

    @FXML
    private TextField urlTextField;

    private int index = 0;

    @FXML
    void downloadButtonClicked(ActionEvent event) {
        String url = urlTextField.getText().trim();

        if (url.isEmpty() || !url.startsWith("http")) {
            System.out.println("Invalid URL entered.");
            return;
        }

        String filename = url.substring(url.lastIndexOf("/") + 1);

        // Avoid duplicate downloads
        List<FileInfo> existingFiles = tableView.getItems();
        boolean alreadyAdded = existingFiles.stream().anyMatch(f -> f.getUrl().equals(url));
        if (alreadyAdded) {
            System.out.println("File already added to download list.");
            return;
        }

        String status = "STARTING";
        String action = "OPEN";
        String path = AppConfig.DOWNLOAD_PATH + File.separator + filename;

        FileInfo file = new FileInfo(String.valueOf(++index), filename, url, status, action, path, "0");
        tableView.getItems().add(file);

        DownloadThread thread = new DownloadThread(file, this);
        thread.start();

        urlTextField.setText("");
    }

    /**
     * Called from DownloadThread to update the table UI
     */
    public void updateUI(FileInfo metaFile) {
        try {
            int idx = Integer.parseInt(metaFile.getIndex()) - 1;
            FileInfo fileInfo = tableView.getItems().get(idx);
            fileInfo.setStatus(metaFile.getStatus());

            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            double percent = Double.parseDouble(metaFile.getPer());
            fileInfo.setPer(decimalFormat.format(percent));

            tableView.refresh();
        } catch (Exception e) {
            System.out.println("Error updating UI: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        System.out.println("View initialized");

        TableColumn<FileInfo, String> sn = (TableColumn<FileInfo, String>) tableView.getColumns().get(0);
        sn.setCellValueFactory(p -> p.getValue().indexProperty());

        TableColumn<FileInfo, String> filename = (TableColumn<FileInfo, String>) tableView.getColumns().get(1);
        filename.setCellValueFactory(p -> p.getValue().nameProperty());

        TableColumn<FileInfo, String> url = (TableColumn<FileInfo, String>) tableView.getColumns().get(2);
        url.setCellValueFactory(p -> p.getValue().urlProperty());

        TableColumn<FileInfo, String> status = (TableColumn<FileInfo, String>) tableView.getColumns().get(3);
        status.setCellValueFactory(p -> p.getValue().statusProperty());

        TableColumn<FileInfo, String> per = (TableColumn<FileInfo, String>) tableView.getColumns().get(4);
        per.setCellValueFactory(p -> {
            String formatted = p.getValue().getPer() + " %";
            return new SimpleStringProperty(formatted);
        });

        TableColumn<FileInfo, String> action = (TableColumn<FileInfo, String>) tableView.getColumns().get(5);
        action.setCellValueFactory(p -> p.getValue().actionProperty());
    }
}
