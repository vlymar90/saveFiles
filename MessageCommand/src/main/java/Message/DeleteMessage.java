package Message;

public class DeleteMessage implements Message {
    private String lineOne;

    public DeleteMessage(String lineOne) {
        this.lineOne = lineOne;
    }

    public String getLineOne() {
        return lineOne;
    }

}
