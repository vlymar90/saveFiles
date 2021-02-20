package Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.application.Application;

public class ApplicationBasic extends Application {

    private static Stage windowClient;
    private static Stage reg;
    @Override
    public void start(Stage primaryStage) throws Exception {
        reg = new Stage();
        Parent parent = FXMLLoader.load(getClass().getResource("registr.fxml"));
        reg.setScene(new Scene(parent));
        reg.setResizable(false);
        reg.show();

        windowClient = primaryStage;
        Parent parentRegistr = FXMLLoader.load(getClass().getResource("client_window.fxml"));
        windowClient.setScene(new Scene(parentRegistr));
        windowClient.setTitle("SaveFiles");
        windowClient.setResizable(false);
    }

    public static Stage getWindowClient() {
        return windowClient;
    }

    public static Stage getReg() {
        return reg;
    }
}
