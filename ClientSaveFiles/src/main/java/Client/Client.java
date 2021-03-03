package Client;

import Message.*;
import java.io.*;
import java.net.Socket;;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Client {
    private static final Logger LOGGER = LogManager.getLogger(Client.class);
    private ControllerBasic basic;
    private String nick;
    private static Client instance;
    private Socket socket;
    private ObjectEncoderOutputStream out;
    private ObjectDecoderInputStream in;

    private Client() throws IOException {
        socket = new Socket("localhost", 9500);
        out = new ObjectEncoderOutputStream(socket.getOutputStream());
        in = new ObjectDecoderInputStream(socket.getInputStream());
        LOGGER.info("start stream socket, input and output");
    }

    public static Client getClient() throws IOException {
        if(instance == null) {
            instance = new Client();
            return instance;
        }
        else return instance;
    }

    public void close()  {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   public void connect() {
        LOGGER.info("start connect");
      Thread thread = new Thread(() -> {

          try {
              while (true) {
                  Message message = (Message) in.readObject();
                  if (message instanceof ListServer) {
                      ListServer listServer = (ListServer) message;
                      Platform.runLater(() -> basic.showList(listServer.getList(), basic.severField,
                              listServer.getPath(), basic.serverList));
                  }

                  if (message instanceof DownloadMessage) {
                      DownloadMessage downloadMessage = (DownloadMessage) message;
                      try (OutputStream out = new FileOutputStream("C:\\Users\\tatia\\OneDrive\\Рабочий стол\\java project\\SaveFiles\\ClientSaveFiles\\Download\\" +
                              downloadMessage.getFileNameDownload(), true)) {
                          out.write(downloadMessage.getBuffer());
                          Platform.runLater(() -> basic.showFile(basic.getDirectory()));
                      }
                  }
              }
          } catch (Exception e) {
              e.printStackTrace();
          }
          finally {
              close();
          }
      });
      thread.setDaemon(true);
      thread.start();
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
    public void write(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObjectDecoderInputStream getIn() {
        return in;
    }

    public void setBasic(ControllerBasic basic) {
        this.basic = basic;
    }
}
