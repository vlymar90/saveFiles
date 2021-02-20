package Client;


import java.io.IOException;
import java.net.Socket;
import java.util.List;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

public class Client {
    private String name;
    private String directory = "D:";
    private static Client instance;
    private Socket socket;
    private ObjectEncoderOutputStream out;
    private ObjectDecoderInputStream in;
    private List<StringBuilder> list;

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
                       String message = (String) in.readObject();

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

//    public List getList(String directory) {
//        list = new ArrayList();
//        File dir = new File(directory);
//        File[] listFile = dir.listFiles();
//        StringBuilder builder = new StringBuilder();
//        if (listFile != null) {
//            for (File file : listFile) {
//                if (file == null) {
//                    continue;
//                }
//                builder.append(file.getName()).append(" | ");
//                if (file.isFile()) {
//                    builder.append("[FILE] | ").append(file.length()).append(" bytes.\n");
//                    list.add(builder);
//                } else {
//                    builder.append("[DIR]\n");
//                    list.add(builder);
//                }
//            }
//        }
//        return list;
//    }
}
