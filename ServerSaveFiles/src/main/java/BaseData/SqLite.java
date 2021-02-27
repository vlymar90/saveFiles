package BaseData;

import java.sql.*;

public class SqLite {
    private static Connection connection;
    private static Statement statement;

    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\tatia\\OneDrive\\Рабочий стол\\" +
                            "java project\\SaveFiles\\ServerSaveFiles\\src\\main\\resources\\BaseData\\saveFilesbd.bd");
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disConnect() {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static boolean registration(String nick, String password, String direc) {
        try {
            statement.executeUpdate("insert into clientlist (nick, pass, direc) values ('" + nick + "'" +
                    ",'" + password + "', '" + direc + "')");
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public static String getDirectory(String nick, String pass) {
        ResultSet result = null;
        try {
            result = statement.executeQuery("Select direc from clientlist where nick = '" + nick + "' and pass = '"
                    + pass + "'");
            if (result.next()) {
                return result.getString("direc");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}





