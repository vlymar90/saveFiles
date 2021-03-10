package Friend;

import Message.Message;
public class ClickFriend implements Message{
    private String fileDirName;
    private String nickFriend;

    public ClickFriend(String fileDirName, String nickFriend) {
        this.fileDirName = fileDirName;
        this.nickFriend = nickFriend;
    }

    public String getFileDirName() {
        return fileDirName;
    }

    public String getNickFriend() {
        return nickFriend;
    }
}
