package Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ApplicationBasic extends Application {
    public static final Logger LOGGER = LogManager.getLogger(ApplicationBasic.class);
    public static final String RegFXML = "registr.fxml";
    public static final String ClientFXML = "client_window.fxml";
    public static Stage reg;
    public static Stage client;



    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource(RegFXML));
        Parent parentClient = FXMLLoader.load(getClass().getResource(ClientFXML));
        reg = primaryStage;

        reg.setScene(new Scene(parent));
        reg.setResizable(false);
        reg.show();
        LOGGER.info("запуск приложения");
        client = new Stage();
        client.setScene(new Scene(parentClient));
        client.setResizable(false);
        client.close();

    }
}
