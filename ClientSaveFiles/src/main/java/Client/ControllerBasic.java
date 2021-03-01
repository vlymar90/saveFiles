package Client;

import Message.BackMessage;
import Message.ClickMessage;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static javafx.scene.input.KeyCode.R;

public class ControllerBasic implements Initializable {
    private static final String path = "D:";
    private File directory = new File(path);
    private Stage dialog;
    private static final String resourceClient = "dialog.fxml";
    private static final String resourceServer = "dialogserver.fxml";
    private Client client;
    private DialogController dialogController;

    @FXML
    public ListView<String> clientList;
    @FXML
    public ListView<String> serverList;
    @FXML
    public TextField clientField;
    @FXML
    public TextField severField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ControllerRegistration.control = this;
        dialogController = DialogController.controller();
        showFile(directory);
    }

    public void showFile(File file) {
        ArrayList<String> list = new ArrayList<>();
        File[] files = file.listFiles();
        for (File doc : files) {
            if (doc == null) {
                continue;
            }
            if (doc.isFile()) {
                list.add(doc.getName() + " -> " + "[FILE]" + " -> " + file.length());
            } else {
                list.add(doc.getName() + " -> " + "[DIR]");
            }
        }
        showList(list, clientField, file.getPath(), clientList);
    }

    public void windowSelect(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            openDialog(resourceClient, mouseEvent);
        }

        if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseButton.PRIMARY) {
            String listCell = clientList.getFocusModel().getFocusedItem();
            String[] nameFile = listCell.split("->");
            Path path = Paths.get(directory + "/" + nameFile[0].trim());
            directory = path.toFile();
            File[] file = directory.listFiles();
            if (file == null) {
                directory = directory.getParentFile();
                showFile(directory);
            }
            showFile(directory);
        }
    }

    public void windowSelectServer(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            openDialog(resourceServer, mouseEvent);
        }

        if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseButton.PRIMARY) {
            String cell = serverList.getFocusModel().getFocusedItem();
            client.write(new ClickMessage(cell));
        }
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public Client getClient() {
        return client;
    }

    public void setDialogController(DialogController dialogController) {
        this.dialogController = dialogController;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public Stage getDialog() {
        return dialog;
    }

    public void Back() {
        if (directory.getParentFile() != null) {
            directory = directory.getParentFile();
            showFile(directory);
        }
    }

    public void backServer(ActionEvent actionEvent) {
        client.write(new BackMessage(severField.getText()));
    }

    private void openDialog(String resource, MouseEvent mouseEvent) {
        try {
            DialogController.basic = this;
            dialog = new Stage();
            dialog.setX(mouseEvent.getScreenX());
            dialog.setY(mouseEvent.getScreenY());

            VBox box = FXMLLoader.load(getClass().getResource(resource));
            Scene scene = new Scene(box);
            box.setSpacing(1);
            box.setPadding(new Insets(0, 0, 0, 5));

            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setScene(scene);
            dialog.setResizable(false);
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showList(ArrayList<String> list, TextField field, String path, ListView<String> listView) {
        ObservableList<String> obs = FXCollections.observableList(list);
        listView.setItems(obs);
        field.setText(path);
    }

    public void actionKeyClient(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == R) {
            dialogController.rename(new ActionEvent());
        }
       else if (keyEvent.getCode() == KeyCode.S) {
            dialogController.send(new ActionEvent());
        }
       else if (keyEvent.getCode() == KeyCode.D) {
         dialogController.delete(new ActionEvent());
        }
    }

    public void actionKeyServer(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == R) {
            dialogController.renameServer(new ActionEvent());
        }

        if (keyEvent.getCode() == KeyCode.D) {
            dialogController.downloadServer(new ActionEvent());
        }

        if (keyEvent.getCode() == KeyCode.DELETE) {
            dialogController.deleteServer(new ActionEvent());
        }
    }
}


