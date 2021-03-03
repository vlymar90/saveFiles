package Message;

import java.io.File;
import java.nio.file.Path;

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

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isConnect() {
        return isConnect;
    }
}
