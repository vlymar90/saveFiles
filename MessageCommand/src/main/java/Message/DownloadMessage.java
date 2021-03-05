package Message;

public class DownloadMessage implements Message {
    private byte[] buffer;
    private String fileNameDownload;

    public DownloadMessage(byte[] buffer, String fileNameDownload) {
        this.buffer = buffer;
        this.fileNameDownload = fileNameDownload;
    }

    public DownloadMessage(String fileNameDownload) {
        this.fileNameDownload = fileNameDownload;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public String getFileNameDownload() {
        return fileNameDownload;
    }
}
