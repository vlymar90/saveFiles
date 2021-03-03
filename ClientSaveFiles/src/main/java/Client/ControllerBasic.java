package Client;

import Message.*;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import static javafx.scene.input.KeyCode.R;

public class ControllerBasic implements Initializable {
    private static final String path = "D:";
    private File directory = new File(path);
    private Client client;
    private static final int SIZE_BUFFER = 100000;
    private byte[] buffer = new byte[SIZE_BUFFER];
    public static final Logger LOGGER = LogManager.getLogger(ControllerRegistration.class);

    @FXML
    public ContextMenu dialogServer;
    @FXML
    public ContextMenu dialogClient;
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
                list.add(doc.getName() + " -> " + "[FILE]" + " -> " + doc.length() + " байт");
            } else {
                list.add(doc.getName() + " -> " + "[DIR]" + " -> " + doc.length() + " байт");
            }
        }
        showList(list, clientField, file.getPath(), clientList);
    }

    public void windowSelect(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            dialogClient.show(new VBox(), mouseEvent.getScreenX(), mouseEvent.getScreenY());
        }

        if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseButton.PRIMARY) {
            String listCell = clientList.getFocusModel().getFocusedItem();
            String[] nameFile = listCell.split("->");
            Path path = Paths.get(directory + "/" + nameFile[0].trim());
            directory = path.toFile();
            File[] file = directory.listFiles();
            if (file == null) {
                setDirectory(directory.getParentFile());
                showFile(directory);
            }
            showFile(directory);
        }
    }

    public void windowSelectServer(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            dialogClient.show(new VBox(), mouseEvent.getScreenX(), mouseEvent.getScreenY());
        }

        if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseButton.PRIMARY) {
            String cell = serverList.getFocusModel().getFocusedItem();
            client.write(new ClickMessage(getPath(cell, severField).toString()));
        }
    }

    public void exit(ActionEvent actionEvent) {
        LOGGER.info("exit program!");
        System.exit(0);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public File getDirectory() {
        return directory;
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

    public void showList(ArrayList<String> list, TextField field, String path, ListView<String> listView) {
        ObservableList<String> obs = FXCollections.observableList(list);
        listView.setItems(obs);
        field.setText(path);

    }

    public void actionKeyClient(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == R) {
            rename(new ActionEvent());
        }
       else if (keyEvent.getCode() == KeyCode.S) {
            send(new ActionEvent());
        }
       else if (keyEvent.getCode() == KeyCode.D) {
         delete(new ActionEvent());
        }
    }

    public void actionKeyServer(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == R) {
            renameServer(new ActionEvent());
        }

        if (keyEvent.getCode() == KeyCode.D) {
            downloadServer(new ActionEvent());
        }

        if (keyEvent.getCode() == KeyCode.DELETE) {
            deleteServer(new ActionEvent());
        }
    }

    public void rename(ActionEvent actionEvent) {
        String cell = clientList.getFocusModel().getFocusedItem();
        Path oldPath = getPath(cell, clientField);
        LOGGER.info("file " + oldPath.toString() + " is rename!");
        Stage rename = new Stage();
        rename.setX(dialogClient.getX());
        rename.setY(dialogClient.getY());
        Label label = new Label("Введите новое имя файла");
        TextField textField = new TextField();
        Button button = new Button("Сменить");
        VBox box = new VBox();
        box.setSpacing(20);
        box.getChildren().addAll(label, textField, button);
        button.setOnAction(event -> {
            if (textField.getText() != null) {
                File file = new File(oldPath.toString());
                file.renameTo(new File(directory + "/" + textField.getText()));
                rename.close();
                Back();
            }
        });
        Scene scene = new Scene(box, 200, 150);
        rename.initModality(Modality.WINDOW_MODAL);
        rename.setScene(scene);
        rename.setResizable(false);
        rename.show();
    }

    public void send(ActionEvent actionEvent) {
        String sendFile = clientList.getFocusModel().getFocusedItem();
        File file = new File(getPath(sendFile, clientField).toString());
        LOGGER.info("file " + file.getName() + " is send to server");
        int count = (int) (file.length() - 1) / (SIZE_BUFFER + 1);
        try (InputStream in = new FileInputStream(file)) {
            for (int i = 0; i < count; i++) {
                in.read(buffer);
                client.write(new SendMessage(buffer, file.getName()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(ActionEvent actionEvent)  {
        String cell = clientList.getFocusModel().getFocusedItem();
        File file = new File(getPath(cell, clientField).toString());
        LOGGER.info("file " + file.getName() + " is delete");
        file.delete();
        setDirectory(directory.getParentFile());
        showFile(directory);
    }

    private Path getPath(String cell, TextField textField) {
        String[] nameFile = cell.split("->");
        Path path = Paths.get(textField.getText() + "/" + nameFile[0].trim());
        return path;
    }

    public void renameServer(ActionEvent actionEvent) {
        String oldFile = serverList.getFocusModel().getFocusedItem();
        Stage rename = new Stage();
        rename.setX(dialogServer.getX());
        rename.setY(dialogServer.getY());
        Label label = new Label("Введите новое имя файла");
        TextField textField = new TextField();
        Button button = new Button("Сменить");
        VBox box = new VBox();
        box.setSpacing(20);
        box.getChildren().addAll(label, textField, button);
        button.setOnAction(event -> {
            if (textField.getText() != null) {
                client.write(new RenameMessage(getPath(oldFile, severField).toString(), textField.getText()));
                LOGGER.info("file " + getPath(oldFile, severField).toString() + " rename to server");
                rename.close();
            }
        });
        Scene scene = new Scene(box, 200, 150);
        rename.initModality(Modality.WINDOW_MODAL);
        rename.setScene(scene);
        rename.setResizable(false);
        rename.show();
    }

    public void downloadServer(ActionEvent actionEvent) {
        String downloadFile = serverList.getFocusModel().getFocusedItem();
        client.write(new DownloadMessage(getPath(downloadFile, severField).toString()));
        LOGGER.info("file " + getPath(downloadFile, severField).toString() + " download on client");
    }

    public void deleteServer(ActionEvent actionEvent) {
        String deleteFile = serverList.getFocusModel().getFocusedItem();
        client.write(new DeleteMessage(getPath(deleteFile, severField).toString()));
        LOGGER.info("file " + getPath(deleteFile, severField).toString() + " delete on client");
    }
}


