package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class controlController {
    @FXML
    private SplitMenuButton chooseToDelete;
    @FXML
    private AnchorPane startPane;
    @FXML
    private Button dropButton;
    @FXML
    private Button clearButton;
    private String option = "";

    public void clearData() throws SQLException {
        Connection connection = Main.connection;
        Statement statement = connection.createStatement();
        statement.execute("SELECT clear_table('" + option + "');");
    }

    public void clearAll() throws SQLException {
        Connection connection = Main.connection;
        Statement statement = connection.createStatement();
        statement.execute("SELECT clear_all();");
        clearButton.setDisable(true);
        startPane.setStyle("-fx-background-color: pink");
    }

    public void drop() throws SQLException {
        Connection connection = Main.connection;
        connection.close();
        connection = connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "storemanager", "strongPassword");
        Statement statement = connection.createStatement();
        statement.execute("DROP DATABASE lab2;");
        dropButton.setDisable(true);
        startPane.setStyle("-fx-background-color: red");
    }

    public void clickPersonal(ActionEvent actionEvent) throws IOException {
        chooseToDelete.setText("Персонал");
        option = "cashier";
    }

    public void clickChecks(ActionEvent actionEvent) throws IOException {
        chooseToDelete.setText("Чеки");
        option = "checks";
    }

    public void clickChecksDetails(ActionEvent actionEvent) throws IOException {
        chooseToDelete.setText("Детали чеков");
        option = "selling";
    }

    public void clickProducts(ActionEvent actionEvent) throws IOException {
        chooseToDelete.setText("Продукты");
        option = "product";
    }

    public void clickAdmissions(ActionEvent actionEvent) throws IOException {
        chooseToDelete.setText("Поставки");
        option = "admission";
    }

    public void clickAdmissionsDetails(ActionEvent actionEvent) throws IOException {
        chooseToDelete.setText("Детали поставок");
        option = "admission_product";
    }

}
