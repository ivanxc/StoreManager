package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SignInController {
    String name;
    @FXML
    private SplitMenuButton chooseUser;
    @FXML
    private Button enterButton;
    private Stage stage;
    private Scene scene;

    public void enter(ActionEvent actionEvent) throws IOException {
        name = chooseUser.getText();
        Parent root = FXMLLoader.load(getClass().getResource("/scenes/mainScene.fxml"));
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
        Parent root = FXMLLoader.load(getClass().getResource("/scenes/controlUsers.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() throws SQLException {
        Connection connection = Main.connection;
        Statement statement = connection.createStatement();
        ResultSet cashiers = statement.executeQuery("SELECT * FROM cashier;");
        while(cashiers.next()) {
            String cashierName = cashiers.getString("name");
            System.out.println(cashierName);
            MenuItem menuItem = new MenuItem(cashierName);
            chooseUser.setDisable(false);
            menuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    chooseUser.setText(menuItem.getText());
                    enterButton.setDisable(false);
                }
            });
            chooseUser.getItems().add(menuItem);
        }
    }
}
