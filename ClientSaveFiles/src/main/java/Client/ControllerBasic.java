package Client;

import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerBasic implements Initializable {
    private Client client;
    ApplicationBasic app = new ApplicationBasic();
    @FXML
    public ListView clientList;
    @FXML
    public ListView serverList;
    @FXML
    public TextField clientField;
    @FXML
    public TextField severField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        try {
//            network = Client.getClient();
////            showFile();
////        clientList.getSelectionModel().selectedItemProperty().addListener(ch -> {
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }





    public void showFile(List<String> list) {
        ObservableList<String> obs = FXCollections.observableList(list);
        clientList.setItems(obs);
//        StringBuilder builder = new StringBuilder();
//        List<String> list = network.getList();
//        for (String line : list) {
//           builder.append(line + System.lineSeparator());
//        }
//       clientList.appendText(builder.toString());
    }


    public void windowSelect(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            Stage dialog = new Stage();
            dialog.setX(mouseEvent.getScreenX());
            dialog.setY(mouseEvent.getScreenY());

            VBox box = FXMLLoader.load(getClass().getResource("dialog.fxml"));
            box.setSpacing(1);
            box.setPadding(new Insets(0,0,0,5));
            Scene scene = new Scene(box);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setScene(scene);
            dialog.setResizable(false);
            dialog.showAndWait();
        }
    }
    public void setClient(Client network) {
        if (client == null) {
            client = network;
        }
    }
}
