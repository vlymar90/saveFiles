package Message;

public class DownloadMessage implements Message {
    String lineOne;

    public DownloadMessage(String lineOne) {
        this.lineOne = lineOne;
    }

    public String getLineOne() {
        return lineOne;
    }

    public void setLineOne(String lineOne) {
        this.lineOne = lineOne;
    }
}
