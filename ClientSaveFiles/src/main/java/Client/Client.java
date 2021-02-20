package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private String name;
    private String directory = "D:";
    private static Client instance;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private List<StringBuilder> list;

    private Client() throws IOException {
        socket = new Socket("localhost", 9500);
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());
    }

    public static Client getClient() throws IOException {
        if(instance == null) {
            instance = new Client();
            return instance;
        }
        else return instance;
    }


    public void write(String message) throws IOException {
        out.writeUTF(message);
        out.flush();
    }

    public String read() throws IOException {
        return in.readUTF();
    }

    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public DataInputStream getIs() {
        return in;
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

    public List getList(String directory) {
        list = new ArrayList();
        File dir = new File(directory);
        File[] listFile = dir.listFiles();
        StringBuilder builder = new StringBuilder();
        if (listFile != null) {
            for (File file : listFile) {
                if (file == null) {
                    continue;
                }
                builder.append(file.getName()).append(" | ");
                if (file.isFile()) {
                    builder.append("[FILE] | ").append(file.length()).append(" bytes.\n");
                    list.add(builder);
                } else {
                    builder.append("[DIR]\n");
                    list.add(builder);
                }
            }
        }
        return list;
    }
}
