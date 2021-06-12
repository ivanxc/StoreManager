package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class bindProductController {
    public static Product productToBind;
    @FXML
    private Label title;
    @FXML
    private ListView list;
    @FXML
    private Button okButton;
    public static int choosedId = -2;
    private int clickedId = -1;

    public void initialize() throws SQLException {
        title.setText(productToBind.toString());
        Connection connection = Main.connection;
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM find_product_by_title_or_barcode('" + productToBind.getTitleT() + "');");
        LinkedList<Integer> ids = new LinkedList<>();
        while(rs.next()) {
            if (!rs.getString(4).equals(String.valueOf(productToBind.getBarcodeT()))) {
                continue;
            }
            String str = rs.getString(2) + " Цена: " + rs.getString(3) + " Barcode: " + rs.getString(4);
            list.getItems().add(str);
            ids.add(rs.getInt(1));
            System.out.println(rs.getInt(1));
        }
        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                if (list.getSelectionModel().getSelectedItem() == null) {
                    okButton.setDisable(true);
                    return;
                }
                clickedId = ids.get(list.getSelectionModel().getSelectedIndex());
                System.out.println(choosedId +  " - clicked");
                okButton.setDisable(false);
            }
        });
    }

    public void ok() {
        choosedId = clickedId;
        System.out.println(choosedId + " ok");
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    public void newProduct() {
        choosedId = -1;
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }
}
