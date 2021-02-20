package Message;

public class RegMessage {
    private String login;
    private String password;
    private String directory;

    public RegMessage(String login, String password, String directory) {
        this.login = login;
        this.password = password;
        this.directory = directory;
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