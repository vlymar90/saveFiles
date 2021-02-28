package Message;

import java.util.ArrayList;

public class ListServer implements Message{

    private ArrayList<String> list;
    private String path;

    public ListServer(ArrayList<String> list, String path) {
        this.list = list;
        this.path = path;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public String getPath() {
        return path;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
