package Friend;


import Message.Message;

public class FriendAddMessage implements Message {
    private String MyNick;
    private String nickFriend;

    public FriendAddMessage(String myNick, String nickFriend) {
        MyNick = myNick;
        this.nickFriend = nickFriend;
    }

    public String getMyNick() {
        return MyNick;
    }

    public String getNickFriend() {
        return nickFriend;
    }
}
