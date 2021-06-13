package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.SplitMenuButton;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class reportsController {
    @FXML
    private SplitMenuButton reportFrom;
    @FXML
    private Label income;

    public void day(ActionEvent actionEvent) throws IOException, SQLException {
        Connection connection = Main.connection;
        Statement statement = connection.createStatement();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date();
        String time = dateFormat.format(date1);
        Date dateBefore = new Date(date1.getTime() - 1 * 24 * 3600 * 1000l);
        String timeUp = dateFormat.format(dateBefore);
        ResultSet rs = statement.executeQuery("SELECT * FROM get_checks_from_period('" + time + "', '" + dateBefore + "');");
        double sum = 0.0;
        while(rs.next()) {
            sum += rs.getDouble(3);
        }
        income.setText("Доход: " + sum + " руб.");
        reportFrom.setText("день");
    }

    public void week(ActionEvent actionEvent) throws IOException, SQLException {
        Connection connection = Main.connection;
        Statement statement = connection.createStatement();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date();
        String time = dateFormat.format(date1);
        Date dateBefore = new Date(date1.getTime() - 7 * 24 * 3600 * 1000l);
        String timeUp = dateFormat.format(dateBefore);
        ResultSet rs = statement.executeQuery("SELECT * FROM get_checks_from_period('" + time + "', '" + dateBefore + "');");
        double sum = 0.0;
        while(rs.next()) {
            sum += rs.getDouble(3);
        }
        income.setText("Доход: " + sum + " руб.");
        reportFrom.setText("неделя");
    }

    public void month(ActionEvent actionEvent) throws IOException, SQLException {
        Connection connection = Main.connection;
        Statement statement = connection.createStatement();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date();
        String time = dateFormat.format(date1);
        Date dateBefore = new Date(date1.getTime() - 31 * 24 * 3600 * 1000l);
        String timeUp = dateFormat.format(dateBefore);
        ResultSet rs = statement.executeQuery("SELECT * FROM get_checks_from_period('" + time + "', '" + dateBefore + "');");
        double sum = 0.0;
        while(rs.next()) {
            sum += rs.getDouble(3);
        }
        income.setText("Доход: " + sum + " руб.");
        reportFrom.setText("месяц");
    }

    public void quarter(ActionEvent actionEvent) throws IOException, SQLException {
        Connection connection = Main.connection;
        Statement statement = connection.createStatement();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date();
        String time = dateFormat.format(date1);
        Date dateBefore = new Date(date1.getTime() - 92 * 24 * 3600 * 1000l);
        String timeUp = dateFormat.format(dateBefore);
        ResultSet rs = statement.executeQuery("SELECT * FROM get_checks_from_period('" + time + "', '" + dateBefore + "');");
        double sum = 0.0;
        while(rs.next()) {
            sum += rs.getDouble(3);
        }
        income.setText("Доход: " + sum + " руб.");
        reportFrom.setText("квартал");
    }

    public void year(ActionEvent actionEvent) throws IOException, SQLException {
        Connection connection = Main.connection;
        Statement statement = connection.createStatement();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date();
        String time = dateFormat.format(date1);
        Date dateBefore = new Date(date1.getTime() - 365 * 24 * 3600 * 1000l);
        String timeUp = dateFormat.format(dateBefore);
        ResultSet rs = statement.executeQuery("SELECT * FROM get_checks_from_period('" + time + "', '" + dateBefore + "');");
        double sum = 0.0;
        while(rs.next()) {
            sum += rs.getDouble(3);
        }
        income.setText("Доход: " + sum + " руб.");
        reportFrom.setText("год");

    }

    public void allTime(ActionEvent actionEvent) throws IOException, SQLException {
        Connection connection = Main.connection;
        Statement statement = connection.createStatement();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date();
        String time = dateFormat.format(date1);
        ResultSet rs = statement.executeQuery("SELECT * FROM get_checks_from_period('" + time + "');");
        double sum = 0.0;
        while(rs.next()) {
            sum += rs.getDouble(3);
        }
        income.setText("Доход: " + sum + " руб.");
        reportFrom.setText("все время");
    }
}
