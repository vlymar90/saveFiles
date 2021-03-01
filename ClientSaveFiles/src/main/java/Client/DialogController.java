package Client;

import Message.DeleteMessage;
import Message.DownloadMessage;
import Message.RenameMessage;
import Message.SendMessage;
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


    public void rename(ActionEvent actionEvent) throws IOException {
        String cell = basic.clientList.getFocusModel().getFocusedItem();
        Path oldPath = getPath(cell);
        basic.getDialog().close();
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
                    basic.Back();
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
            String sendFile = basic.clientList.getFocusModel().getFocusedItem();
            basic.getClient().write(new SendMessage(new File(getPath(sendFile).toString())));
            basic.getDialog().close();
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
     String oldFile = basic.serverList.getFocusModel().getFocusedItem();
     basic.getDialog().close();
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
                    basic.getClient().write(new RenameMessage(oldFile, textField.getText()));
                    rename.close();
                }
            }
        });
        Scene scene = new Scene(box, 200, 150);
        rename.initModality(Modality.WINDOW_MODAL);
        rename.setScene(scene);
        rename.setResizable(false);
        rename.show();

    }

    public void downloadServer(ActionEvent actionEvent) {
        String downloadFile = basic.serverList.getFocusModel().getFocusedItem();
        basic.getClient().write(new DownloadMessage(downloadFile));
        basic.getDialog().close();

    }

    public void deleteServer(ActionEvent actionEvent) throws IOException {
             String deleteFile = basic.serverList.getFocusModel().getFocusedItem();
             basic.getClient().write(new DeleteMessage(deleteFile));
             basic.getDialog().close();
    }
}
