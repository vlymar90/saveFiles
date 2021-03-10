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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ControllerRegistration {
    public static final Logger LOGGER = LogManager.getLogger(ControllerRegistration.class);
    public TextField directoryServer;
    public PasswordField passwordReg;
    public TextField nickReg;
    public TextField login;
    public PasswordField password;

    private ApplicationBasic app;
    public static ControllerBasic control;
    private Client client;
    public static Stage regStage;

    public void authorization(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if(app == null) {
            app = new ApplicationBasic();
        }
        client = Client.getClient();
        client.write(new AuthMessage(login.getText(), password.getText()));
        Message message = (Message) client.getIn().readObject();
        if (message instanceof AuthMessage) {
            AuthMessage auth = (AuthMessage) message;
            if (auth.isConnect()) {
                LOGGER.info("Авторизация прошла успешно");
                client.connect();
                client.setNick(login.getText());
                ApplicationBasic.reg.close();
                control.setClient(client);
                client.setBasic(control);
                ApplicationBasic.client.show();
            }

            else {
                LOGGER.info("Клиент не авторизовался");
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
            app = new ApplicationBasic();
            regStage = new Stage();
            Parent parent = FXMLLoader.load(getClass().getResource("windowregistration.fxml"));
            regStage.setScene(new Scene(parent));
            regStage.setResizable(false);
            ApplicationBasic.reg.close();
            regStage.initModality(Modality.WINDOW_MODAL);
            regStage.show();
            LOGGER.info("Открыто окно регистрации");
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
                if (reg.isConnect()) {
                    LOGGER.info("Регистрация прошла успешно");
                    regStage.close();
                    ApplicationBasic.reg.show();
                } else {
                    LOGGER.info("Отклонение регистрации");
                    getAlert("Ошибка", "Результат регистрации",
                            "логин, пароль или директория уже используются");
                    nickReg.clear();
                    passwordReg.clear();
                    directoryServer.clear();
                    nickReg.requestFocus();
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            LOGGER.info("Ошибка отправки сообщения или ошибка соединения");
            e.printStackTrace();
        }
    }

    private void getAlert(String tittle, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setX(ApplicationBasic.reg.getX() + 265);
        alert.setY(ApplicationBasic.reg.getY() + 268);
        alert.setTitle(tittle);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
