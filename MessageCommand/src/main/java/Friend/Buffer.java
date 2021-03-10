package Friend;
import Message.Message;
public class Buffer implements Message{
    private byte[] buffer;

    public Buffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public byte[] getBuffer() {
        return buffer;
    }
}
