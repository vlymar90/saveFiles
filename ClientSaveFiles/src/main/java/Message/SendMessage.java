package Message;

import java.io.File;

public class SendMessage implements Message {
    private File sendFile;

    public SendMessage(File sendFile) {
        this.sendFile = sendFile;
    }

    public File getSendFile() {
        return sendFile;
    }
}
