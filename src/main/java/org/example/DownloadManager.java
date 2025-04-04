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

public class DownloadManager {

    @FXML
    private TableView<FileInfo> tableView;

    @FXML
    private TextField urlTextField;

    private int index = 0;

    private DownloadThread currentDownloadThread;

    @FXML
    void downloadButtonClicked(ActionEvent event) {
        String url = urlTextField.getText().trim();
        if (url.isEmpty()) return;

        String filename = url.substring(url.lastIndexOf("/") + 1);
        String status = "STARTING";
        String action = "OPEN";
        String path = AppConfig.DOWNLOAD_PATH + File.separator + filename;
        FileInfo file = new FileInfo(String.valueOf(++index), filename, url, status, action, path, "0");

        currentDownloadThread = new DownloadThread(file, this);
        tableView.getItems().add(file);
        currentDownloadThread.start();

        urlTextField.setText("");
    }

    @FXML
    void pauseDownload(ActionEvent event) {
        if (currentDownloadThread != null) {
            currentDownloadThread.pauseDownload();
        }
    }

    @FXML
    void resumeDownload(ActionEvent event) {
        if (currentDownloadThread != null) {
            currentDownloadThread.resumeDownload();
        }
    }

    @FXML
    void cancelDownload(ActionEvent event) {
        if (currentDownloadThread != null) {
            currentDownloadThread.cancelDownload();
        }
    }

    public void updateUI(FileInfo metaFile) {
        FileInfo fileInfo = tableView.getItems().get(Integer.parseInt(metaFile.getIndex()) - 1);
        fileInfo.setStatus(metaFile.getStatus());

        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        fileInfo.setPer(decimalFormat.format(Double.parseDouble(metaFile.getPer())));
        tableView.refresh();
    }

    @FXML
    public void initialize() {
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
            SimpleStringProperty prop = new SimpleStringProperty();
            prop.set(p.getValue().getPer() + " %");
            return prop;
        });

        TableColumn<FileInfo, String> action = (TableColumn<FileInfo, String>) tableView.getColumns().get(5);
        action.setCellValueFactory(p -> p.getValue().actionProperty());
    }
}
