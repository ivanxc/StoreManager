package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ControlUsersController {
    @FXML
    TextField userNameField;
    @FXML
    ListView users;
    @FXML
    Button delete;
    @FXML
    Label warning;
    private String clickedUser = "";
    private Stage stage;
    private Scene scene;

    public void toSignIn(ActionEvent actionEvent) throws IOException {
        System.out.println("click");
        Parent root = FXMLLoader.load(getClass().getResource("/scenes/signin.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void addUser(ActionEvent actionEvent) throws IOException, SQLException {
        Connection connection = Main.connection;
        Statement statement = connection.createStatement();

        String name = userNameField.getText();

        ResultSet isUserExist = statement.executeQuery("SELECT count(*) FROM cashier WHERE name = '" + name + "'");
        isUserExist.next();
        if (isUserExist.getInt(1) != 0) {
            warning.setVisible(true);
        } else {
            warning.setVisible(false);

            ResultSet cashiers = statement.executeQuery("SELECT add_cashier('" + name + "');");
            users.getItems().add(userNameField.getText());
            users.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
                @Override
                public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                    if (users.getSelectionModel().getSelectedItem() == null) {
                        delete.setDisable(true);
                        return;
                    }
                    clickedUser = users.getSelectionModel().getSelectedItem().toString();
                    System.out.println("clicked on " + users.getSelectionModel().getSelectedItem());
                    delete.setDisable(false);
                }
            });
        }
    }

    public void deleteUser(ActionEvent actionEvent) throws SQLException {
        System.out.println("delete user");
        Connection connection = Main.connection;
        Statement statement = connection.createStatement();
        statement.execute("DELETE FROM cashier" +
                "  WHERE name = '" + clickedUser + "';");
        Object[] usersWitoutDeletedUser = users.getItems().stream().filter(name -> name != clickedUser).toArray();
        users.getItems().clear();
        users.getItems().addAll(usersWitoutDeletedUser);
    }

    public void initialize() throws SQLException {
        System.out.println("initialuze isers");
        Connection connection = Main.connection;
        Statement statement = connection.createStatement();
        ResultSet cashiers = statement.executeQuery("SELECT * FROM cashier;");
        while(cashiers.next()) {
            String cashierName = cashiers.getString("name");
            users.getItems().add(cashierName);
            users.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
                @Override
                public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                    if (users.getSelectionModel().getSelectedItem() == null) {
                        delete.setDisable(true);
                        return;
                    }
                    clickedUser = users.getSelectionModel().getSelectedItem().toString();
                    System.out.println("clicked on " + users.getSelectionModel().getSelectedItem());
                    delete.setDisable(false);
                }
            });
        }
        statement.close();
    }

}
