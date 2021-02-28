package Client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class DialogController {
    public static ControllerBasic basic;

    public void rename(ActionEvent actionEvent) {
        String cell = basic.clientList.getFocusModel().getFocusedItem();
        Path oldPath = getPath(cell);
        basic.getDialog().close();
        try {
            change(oldPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void change(Path oldPath) throws IOException {
        Stage rename = new Stage();
        rename.setX(basic.getDialog().getX());
        rename.setY(basic.getDialog().getY());
        Label label = new Label("Введите новое имя файла");
        TextField textField = new TextField();
        Button button = new Button("Сменить");
        VBox box = new VBox();
        box.setSpacing(20);
        box.getChildren().addAll(label, textField, button);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (textField.getText() != null) {
                    File file = new File(oldPath.toString());
                    file.renameTo(new File(basic.getDirectory() + "/" + textField.getText()));
                    rename.close();
                    basic.Back(new ActionEvent());
                }
            }

        });

        Scene scene = new Scene(box, 200, 150);
        rename.initModality(Modality.WINDOW_MODAL);
        rename.setScene(scene);
        rename.setResizable(false);
        rename.show();
    }

    public void send(ActionEvent actionEvent) throws IOException {

    }

    public void delete(ActionEvent actionEvent) throws IOException {
        String cell = basic.clientList.getFocusModel().getFocusedItem();
        basic.setDirectory(basic.getDirectory().getParentFile());
        basic.showFile(basic.getDirectory());
        basic.getDialog().close();
        Files.delete(getPath(cell));
    }

    private Path getPath(String cell) {
        String[] nameFile = cell.split("->");
        Path path = Paths.get(basic.clientField.getText() + "/" + nameFile[0].trim());
        return path;
    }

    public void renameServer(ActionEvent actionEvent) {
    }

    public void downloadServer(ActionEvent actionEvent) {
    }

    public void deleteServer(ActionEvent actionEvent) throws IOException {

    }
}


//    byte[] buffer = new byte[1028];
//    String path = basic.serverList.getFocusModel().getFocusedItem();
//    String[] nameFile = path.split("->");
//    File file = new File("ClientSaveFiles/Download/" + nameFile[0].trim());
//        try (InputStream in = new FileInputStream(new File(basic.severField.getText()) + "/" + nameFile[0].trim());
//                OutputStream out = new FileOutputStream(file)) {
//                while (in.read(buffer) != -1) {
//                out.write(buffer);