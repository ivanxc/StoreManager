package sample;

import com.sun.javafx.fxml.FXMLLoaderHelper;
import com.sun.prism.shader.Solid_ImagePattern_Loader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitMenuButton;
import javafx.stage.Stage;

import java.io.IOException;

public class signInController {
    String name;
    @FXML
    private SplitMenuButton chooseUser;
    private Stage stage;
    private Scene scene;

    public void enter(ActionEvent actionEvent) throws IOException {
        name = chooseUser.getText();
        Parent root = FXMLLoader.load(getClass().getResource("scenes/mainScene.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        System.out.println(name);
        ((Label) scene.lookup("#user")).setText(name);
        Main.userName = name;
    }

    public void addUser(ActionEvent actionEvent) throws IOException {
        System.out.println("click");
        Parent root = FXMLLoader.load(getClass().getResource("scenes/controlUsers.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
