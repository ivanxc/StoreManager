package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SplitMenuButton;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class controlController {
    @FXML
    private SplitMenuButton chooseToDelete;
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
    }

    public void clickPersonal(ActionEvent actionEvent) throws IOException {
        chooseToDelete.setText("Персонал");
        option = "cashier";
    }

    public void clickChecks(ActionEvent actionEvent) throws IOException {
        chooseToDelete.setText("Чеки");
        option = "check";
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
        option = "admission_detail";
    }

}
