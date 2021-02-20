package Message;

public class DeleteMessage implements Message {
    String lineOne;

    public DeleteMessage(String lineOne) {
        this.lineOne = lineOne;
    }

    public String getLineOne() {
        return lineOne;
    }

    public void setLineOne(String lineOne) {
        this.lineOne = lineOne;
    }
}
