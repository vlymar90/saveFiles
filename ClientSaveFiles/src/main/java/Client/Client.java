package Client;

import Message.*;
import java.io.*;
import java.net.Socket;;
import java.util.List;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;


public class Client {

    private String name;
    private String directory = "./";
    private static Client instance;
    private Socket socket;
    private ObjectEncoderOutputStream out;
    private ObjectDecoderInputStream in;
    private List<StringBuilder> list;
    ApplicationBasic app = new ApplicationBasic();

    private Client() throws IOException {
        socket = new Socket("localhost", 9500);
        out = new ObjectEncoderOutputStream(socket.getOutputStream());
        in = new ObjectDecoderInputStream(socket.getInputStream());
    }

    public static Client getClient() throws IOException {

        if(instance == null) {
            instance = new Client();
            return instance;
        }
        else return instance;
    }




    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

   public void connect() {

        new Thread(() -> {
                try {

                   while (true) {
                       Message message = (Message) in.readObject();
                       if(message instanceof AuthMessage) {




                       }

                   }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
    }



    public String getName() {
        return name;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void write(Message message) throws IOException {
        out.writeObject(message);
    }

    public ObjectDecoderInputStream getIn() {
        return in;
    }
}
