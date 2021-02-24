package Client;

import Message.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerRegistration {
    public TextField directoryServer;
    public PasswordField passwordReg;
    public TextField nickReg;

    public TextField login;
    public PasswordField password;

    private ControllerBasic control;
    private Client client;
    private ApplicationBasic app;
    public static Stage regStage;

    public void authorization(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        client = Client.getClient();
        client.write(new AuthMessage("", ""));
        Message message = (Message) client.getIn().readObject();
        if(message instanceof AuthMessage) {
            ApplicationBasic.client.show();
        }



    }

        public void registration(ActionEvent actionEvent) {
        try {
            regStage = new Stage();
            Parent parent = FXMLLoader.load(getClass().getResource("windowregistration.fxml"));
            regStage.setScene(new Scene(parent));
            regStage.setResizable(false);
            ApplicationBasic.reg.close();
            regStage.initModality(Modality.WINDOW_MODAL);
            regStage.show();
            new Thread(() -> {
                while (true) {
                    if (!regStage.isShowing()) {
                       Platform.runLater(() -> ApplicationBasic.reg.show());
                        break;
                    }
                }
            }).start();
            client = Client.getClient();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void serverReg(ActionEvent actionEvent) {
        try {
            client.write(new RegMessage(nickReg.getText(), passwordReg.getText(), directoryServer.getText()));
            Message message = (Message) client.getIn().readObject();
            if (message instanceof RegMessage) {
                //Доделать регистрацию
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
