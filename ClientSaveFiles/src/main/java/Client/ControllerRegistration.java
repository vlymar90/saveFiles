package Client;

import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ControllerRegistration {
    ControllerBasic control;
    public TextField login;
    public PasswordField password;
    private Client client;

    public void authorization(ActionEvent actionEvent) {
        try {
            ApplicationBasic.getReg().close();
            ApplicationBasic.getWindowClient().show();
            control = new ControllerBasic();
            client = Client.getClient();
            client.connect();
            control.setClient(client);


        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registration(ActionEvent actionEvent) {
        try {
            client = Client.getClient();
            client.connect();

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void instruction(ActionEvent actionEvent) {

    }

}
