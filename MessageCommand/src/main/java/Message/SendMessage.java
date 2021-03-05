package Message;

public class SendMessage implements Message {
    private String sendFile;
    private byte[] buffer;

    public SendMessage(byte[] buffer, String sendFile) {
        this.buffer = buffer;
        this.sendFile = sendFile;
    }

    public String getSendFile() {
        return sendFile;
    }

    public byte[] getBuffer() {
        return buffer;
    }
}
