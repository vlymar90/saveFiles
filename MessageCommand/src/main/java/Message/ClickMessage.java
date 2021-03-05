package Message;

public class ClickMessage implements Message {

    private String path;

    public ClickMessage(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
