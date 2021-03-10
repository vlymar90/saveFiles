package Friend;
import Message.Message;
public class FriendConsent implements Message{
    private boolean isConsent;
    private String MyNick;
    private String nickFriend;

    public FriendConsent(boolean isConsent, String myNick, String nickFriend) {
        this.isConsent = isConsent;
        MyNick = myNick;
        this.nickFriend = nickFriend;
    }

    public boolean isConsent() {
        return isConsent;
    }

    public String getMyNick() {
        return MyNick;
    }

    public String getNickFriend() {
        return nickFriend;
    }
}
