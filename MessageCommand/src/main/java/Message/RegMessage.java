package Message;

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

    public String getPassword() {
        return password;
    }

    public String getDirectory() {
        return directory;
    }

    public boolean isConnect() {
        return isConnect;
    }
}
