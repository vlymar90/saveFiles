package Message;

import java.util.ArrayList;

public class ListClientMessage implements Message {
    private ArrayList list;

    public ListClientMessage(ArrayList <String> list) {
        this.list = list;
    }

    public ArrayList getList() {
        return list;
    }
}
