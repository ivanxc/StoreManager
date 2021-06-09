package sample;

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

public class Controller {
    @FXML
    private Button startSellingButton;

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private SplitMenuButton chooseUser;
    @FXML
    private SplitMenuButton chooseFacility;
    @FXML
    private Label user;

    public void enter(ActionEvent actionEvent) throws IOException {
        String name = chooseUser.getText();
        Parent root = FXMLLoader.load(getClass().getResource("scenes/mainScene.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        System.out.println(name);
        ((Label) scene.lookup("#user")).setText(name);
    }

    public void startSelling(ActionEvent actionEvent) throws IOException {
        System.out.println("click");
        Parent root = FXMLLoader.load(getClass().getResource("scenes/sellingScene.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void main(ActionEvent actionEvent) throws IOException {
        System.out.println("click");
        Parent root = FXMLLoader.load(getClass().getResource("scenes/mainScene.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void admission(ActionEvent actionEvent) throws IOException {
        System.out.println("click");
        Parent root = FXMLLoader.load(getClass().getResource("scenes/admission.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void inventory(ActionEvent actionEvent) throws IOException {
        System.out.println("click");
        Parent root = FXMLLoader.load(getClass().getResource("scenes/inventory.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void reports(ActionEvent actionEvent) throws IOException {
        System.out.println("click");
        Parent root = FXMLLoader.load(getClass().getResource("scenes/reports.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void toSignIn(ActionEvent actionEvent) throws IOException {
        System.out.println("click");
        Parent root = FXMLLoader.load(getClass().getResource("scenes/signin.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void addUser(ActionEvent actionEvent) throws IOException {
        System.out.println("click");
        Parent root = FXMLLoader.load(getClass().getResource("scenes/controlUsers.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void addFacility(ActionEvent actionEvent) throws IOException {
        System.out.println("click");
        Parent root = FXMLLoader.load(getClass().getResource("scenes/controlFacilities.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
