package Client;


import javafx.application.Platform;
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
    private DialogController dialogController;
    private File directory = new File("D:");
    private Stage dialog;


    @FXML
    public ListView<String> clientList;
    @FXML
    public ListView serverList;
    @FXML
    public TextField clientField;
    @FXML
    public TextField severField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showFile(directory);
    }


    public  void showFile(File file) {
        List<String> list = new ArrayList<>();
        File[] files = file.listFiles();
        for(File doc : files) {
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
        clientList.setItems(obs);
        clientField.setText(file.getAbsolutePath());

    }

    public void windowSelect(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            DialogController.basic = this;
            dialog = new Stage();
            dialog.setX(mouseEvent.getScreenX());
            dialog.setY(mouseEvent.getScreenY());

            VBox box = FXMLLoader.load(getClass().getResource("dialog.fxml"));
            Scene scene = new Scene(box);
            box.setSpacing(1);
            box.setPadding(new Insets(0, 0, 0, 5));

            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setScene(scene);
            dialog.setResizable(false);
            dialog.showAndWait();

        }

        if(mouseEvent.getClickCount() == 2 &&mouseEvent.getButton() == MouseButton.PRIMARY) {
            String listCell = clientList.getFocusModel().getFocusedItem();
            String[] nameFile = listCell.split("->");
            Path path = Paths.get(directory + "/" + nameFile[0].trim());
            directory = path.toFile();
            showFile(directory);
        }
    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
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

    public void Back(ActionEvent actionEvent) {
        directory = directory.getParentFile();
        showFile(directory);
    }
}
