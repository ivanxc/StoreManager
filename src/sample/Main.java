package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.stage.*;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class Main extends Application {
    public static String userName = "";
    public static Connection connection = null;

    @Override
    public void stop() throws Exception {
        if (connection != null) {
            connection.close();
            System.out.println("Close app");
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("scenes/signin.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Store Manager");
        primaryStage.setScene(scene);
        primaryStage.show();

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found?");
            e.printStackTrace();
            return;
        }
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "storemanager", "strongPassword");
        } finally {
        }

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT FROM pg_database WHERE datname = 'lab2'");
        if (!rs.next()) {
            statement.execute("CREATE DATABASE lab2;");
            System.out.println("Create database");
        }
        connection.close();
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lab2", "storemanager", "strongPassword");
        statement = connection.createStatement();
        FileInputStream initializeDB = new FileInputStream("db/db.sql");
        importSQL(connection, initializeDB);

        SplitMenuButton chooseUser = (SplitMenuButton)scene.lookup("#chooseUser");
        ResultSet cashiers = statement.executeQuery("SELECT * FROM cashier;");
        while(cashiers.next()) {
            String cashierName = cashiers.getString("name");
            System.out.println(cashierName);
            MenuItem menuItem = new MenuItem(cashierName);
            chooseUser.setDisable(false);
            menuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    ((SplitMenuButton)scene.lookup("#chooseUser")).setText(menuItem.getText());
                }
            });
            chooseUser.getItems().add(menuItem);
        }
    }

    public static void importSQL(Connection conn, InputStream in) throws SQLException
    {
        Scanner s = new Scanner(in);
        s.useDelimiter("(;(\r)?\n)|(--\n)");
        Statement st = null;
        try
        {
            st = conn.createStatement();
            while (s.hasNext())
            {
                String line = s.next();
                if (line.startsWith("/*!") && line.endsWith("*/"))
                {
                    int i = line.indexOf(' ');
                    line = line.substring(i + 1, line.length() - " */".length());
                }

                if (line.trim().length() > 0)
                {
                    st.execute(line);
                }
            }
        }
        finally
        {
            if (st != null) st.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
