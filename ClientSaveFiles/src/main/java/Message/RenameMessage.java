package Message;

public class RenameMessage implements Message  {
    String lineOne;

    public RenameMessage(String lineOne) {
        this.lineOne = lineOne;
    }

    public String getLineOne() {
        return lineOne;
    }

    public void setLineOne(String lineOne) {
        this.lineOne = lineOne;
    }
}
