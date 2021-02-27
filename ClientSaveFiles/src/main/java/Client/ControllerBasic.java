package Client;

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

public class ControllerBasic implements Initializable {
    private static final String path = "D:";
    private String serverPath;
    private File directory = new File(path);
    private Stage dialog;
    private File directoryServer;
    private static final String resourceClient = "dialog.fxml";
    private static final String resourceServer = "dialogserver.fxml";


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
        showFile(directory, clientList, clientField);
    }


    public void showFile(File file, ListView<String> listView, TextField field) {
        List<String> list = new ArrayList<>();
        File[] files = file.listFiles();
        for (File doc : files) {
            if (doc == null) {
                continue;
            }
            if (doc.isFile()) {
                list.add(new String(doc.getName() + " -> " + "[FILE]" + " -> " + file.length()));
            } else {
                list.add(new String(doc.getName() + " -> " + "[DIR]"));
            }
        }

        ObservableList<String> obs = FXCollections.observableList(list);
        listView.setItems(obs);
        field.setText(file.getPath());
    }

    public void windowSelect(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            openDialog(resourceClient, mouseEvent);
        }

        if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseButton.PRIMARY) {
                directory = navigation(directory, clientList);
                File[] file = directory.listFiles();
                if (file == null) {
                    directory = directory.getParentFile();
                    showFile(directory, clientList, clientField);
                }
                showFile(directory, clientList, clientField);
            }
        }


    public void windowSelectServer(MouseEvent mouseEvent) throws IOException{
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            openDialog(resourceServer, mouseEvent);
        }
        if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseButton.PRIMARY) {
            directoryServer = navigation(directoryServer, serverList);
            File[] file = directoryServer.listFiles();
            if (file == null) {
                directoryServer = directoryServer.getParentFile();
                showFile(directoryServer, serverList, severField);
            }
            showFile(directoryServer, serverList, severField);
        }

    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
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

    public void setDirectoryServer(File directoryServer) {
        this.directoryServer = directoryServer;
        serverPath = directoryServer.getPath();
    }

    public void Back(ActionEvent actionEvent) {
        if (directory.getPath().equals(path)) {
           return;
        }
        else if(directory != null) {
            directory = directory.getParentFile();
            showFile(directory, clientList, clientField);
        }
    }

    public void backServer(ActionEvent actionEvent) {
        if (directoryServer.getPath().equals(serverPath)) {
            return;
        }
        else if(directoryServer != null) {
            directoryServer = directoryServer.getParentFile();
            showFile(directoryServer, serverList, severField);
        }
    }

    private void openDialog(String resource, MouseEvent mouseEvent) throws IOException {
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
    }

    private File navigation(File dir, ListView<String> listView) {
        String listCell = listView.getFocusModel().getFocusedItem();
        String[] nameFile = listCell.split("->");
        Path path = Paths.get(dir + "/" + nameFile[0].trim());
        dir = path.toFile();
        return dir;
    }
}

