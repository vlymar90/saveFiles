package Message;

public class AuthMessage implements Message {
   private String nick;
   private String password;
   private boolean isConnect;

    public AuthMessage(String nick, String password) {
        this.nick = nick;
        this.password = password;
    }

    public AuthMessage(boolean isConnect) {
        this.isConnect = isConnect;
    }

    public String getNick() {
        return nick;
    }

    public String getPassword() {
        return password;
    }

    public boolean isConnect() {
        return isConnect;
    }
}
