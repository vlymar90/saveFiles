package Message;

import java.io.File;
import java.nio.file.Path;

public class RegMessage implements Message {
    private String login;
    private String password;
    private String directory;
    private boolean isConnect;

    public RegMessage(String login, String password, String directory) {
        this.login = login;
        this.password = password;
        this.directory = directory;

    }

    public RegMessage(boolean isConnect) {
        this.isConnect = isConnect;
    }

    public String getLogin() {
        return login;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public String getPassword() {
        return password;
    }

    public String getDirectory() {
        return directory;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

}
