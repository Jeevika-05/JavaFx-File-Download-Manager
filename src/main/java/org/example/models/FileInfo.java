package org.example.models;

import javafx.beans.property.SimpleStringProperty;

public class FileInfo {

    private SimpleStringProperty index = new SimpleStringProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty url = new SimpleStringProperty();
    private SimpleStringProperty status = new SimpleStringProperty();
    private SimpleStringProperty action = new SimpleStringProperty();
    private SimpleStringProperty path = new SimpleStringProperty();
    private SimpleStringProperty per = new SimpleStringProperty();

    private long downloadedBytes;
    private long totalBytes;

    public FileInfo(String index, String name, String url, String status, String action, String path, String per) {
        this.index.set(index);
        this.name.set(name);
        this.url.set(url);
        this.status.set(status);
        this.action.set(action);
        this.path.set(path);
        this.per.set(per);
    }

    public long getDownloadedBytes() {
        return downloadedBytes;
    }

    public void setDownloadedBytes(long downloadedBytes) {
        this.downloadedBytes = downloadedBytes;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(long totalBytes) {
        this.totalBytes = totalBytes;
    }

    // Existing Getters & Setters below

    public String getIndex() { return index.get(); }
    public void setIndex(String index) { this.index.set(index); }
    public SimpleStringProperty indexProperty() { return index; }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }
    public SimpleStringProperty nameProperty() { return name; }

    public String getUrl() { return url.get(); }
    public void setUrl(String url) { this.url.set(url); }
    public SimpleStringProperty urlProperty() { return url; }

    public String getStatus() { return status.get(); }
    public void setStatus(String status) { this.status.set(status); }
    public SimpleStringProperty statusProperty() { return status; }

    public String getAction() { return action.get(); }
    public void setAction(String action) { this.action.set(action); }
    public SimpleStringProperty actionProperty() { return action; }

    public String getPath() { return path.get(); }
    public void setPath(String path) { this.path.set(path); }
    public SimpleStringProperty pathProperty() { return path; }

    public String getPer() { return per.get(); }
    public void setPer(String per) { this.per.set(per); }
    public SimpleStringProperty perProperty() { return per; }

    @Override
    public String toString() {
        return "FileInfo{" +
                "index=" + index +
                ", name=" + name +
                ", url=" + url +
                ", status=" + status +
                ", action=" + action +
                ", path=" + path +
                ", downloadedBytes=" + downloadedBytes +
                ", totalBytes=" + totalBytes +
                '}';
    }
}
