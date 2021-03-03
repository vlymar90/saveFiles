package Message;

import java.io.File;

public class SendMessage implements Message {
    private byte[] buffer;
    private String sendFile;

    public SendMessage(String sendFile) {
        this.sendFile = sendFile;
    }

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
