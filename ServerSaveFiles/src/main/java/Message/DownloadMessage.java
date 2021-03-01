package Message;

import java.io.File;

public class DownloadMessage implements Message {
    private File downloadFile;
    private String fileNameDownload;

    public DownloadMessage(File downloadFile) {
        this.downloadFile = downloadFile;
    }

    public DownloadMessage(String fileNameDownload) {
        this.fileNameDownload = fileNameDownload;
    }

    public File getDownloadFile() {
        return downloadFile;
    }

    public String getFileNameDownload() {
        return fileNameDownload;
    }
}
