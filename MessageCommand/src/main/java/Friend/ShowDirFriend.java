package Friend;
import Message.Message;

import java.util.ArrayList;

public class ShowDirFriend implements Message {
    private String nick;
    private ArrayList<String> listDirClient;

    public ShowDirFriend(String nick) {
        this.nick = nick;
    }

    public ShowDirFriend(ArrayList<String> listDirClient) {
        this.listDirClient = listDirClient;
    }

    public String getNick() {
        return nick;
    }

    public ArrayList<String> getListDirClient() {
        return listDirClient;
    }
}
