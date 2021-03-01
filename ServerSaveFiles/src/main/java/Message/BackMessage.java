package Message;

public class BackMessage implements Message{
    private String childPath;

    public BackMessage(String childPath) {
        this.childPath = childPath;
    }

    public String getChildPath() {
        return childPath;
    }
}
