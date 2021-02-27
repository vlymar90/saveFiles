package Client;

import Message.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ControllerRegistration {
    public TextField directoryServer;
    public PasswordField passwordReg;
    public TextField nickReg;
    public TextField login;
    public PasswordField password;

    private File serverDir;
    public static ControllerBasic control;
    private Client client;
    public static Stage regStage;

    public void authorization(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        client = Client.getClient();
        client.write(new AuthMessage(login.getText(), password.getText()));
        Message message = (Message) client.getIn().readObject();
        if(message instanceof AuthMessage) {
            AuthMessage auth = (AuthMessage) message;
            if(auth.isConnect()) {
                client.setNick(login.getText());
                ApplicationBasic.reg.close();
                serverDir = auth.getDir();
                control.setDirectoryServer(serverDir);
                control.showFile(serverDir, control.serverList, control.severField);
                client.setBasic(control);
                client.connect();
                ApplicationBasic.client.show();
            }
            else {
                getAlert("Авторизация", "Информация",
                        "Неверный логин или пароль, или аккаунта не существует");
                login.clear();
                password.clear();
                login.requestFocus();
            }
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
            Client client = Client.getClient();
            client.write(new RegMessage(nickReg.getText(), passwordReg.getText(), directoryServer.getText()));
            Message message = (Message) client.getIn().readObject();
            if (message instanceof RegMessage) {
                RegMessage reg = (RegMessage) message;
                if(reg.isConnect()) {
                    regStage.close();
                    ApplicationBasic.reg.show();
                }
                else {
                    getAlert("Ошибка", "Результат регистрации",
                            "логин, пароль или директория уже используются");
                    nickReg.clear();
                    passwordReg.clear();
                    directoryServer.clear();
                    nickReg.requestFocus();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getAlert(String tittle, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(tittle);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
