package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.textfield.TextFields;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class inventoryController {
    @FXML
    private TextField title;
    @FXML
    private TableView table;
    @FXML
    private TableColumn<Product, String> titleT;
    @FXML
    private TableColumn<Product, String> priceT;
    @FXML
    private TableColumn<Product, String> amountT;
    @FXML
    private TableColumn<Product, String> actualAmountT;
    @FXML
    private TableColumn<Product, String> differenceT;

    public void confirm() {

    }

    public void delete() {

    }

    public void arrange() {

    }

    public void suggest() throws SQLException {
        Connection connection = Main.connection;
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM find_product_by_title_or_barcode('" + title.getText() + "');");
        LinkedList<String> suggestedProducts = new LinkedList<>();
        ObservableList<Product> list = table.getItems();
        while(rs.next()) {
            Product product = new Product(rs.getString(2), rs.getInt(4),
                    rs.getDouble(3), rs.getDouble(5));
            product.setId(rs.getInt(1));
            product.setActualAmountT(product.getAmountT());
            product.setDifferenceT(product.getActualAmountT() - product.getAmountT());
            suggestedProducts.add(rs.getString(2));
            list.add(product);
        }
        String[] suggestedProductsAsArray = (suggestedProducts.stream().collect(Collectors.toSet())).toArray(new String[0]);
        TextFields.bindAutoCompletion(title, suggestedProductsAsArray);
        table.setItems(list);
        table.refresh();
    }

    public void initialize() throws SQLException {
        Connection connection = Main.connection;
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM get_all_products()");
        ObservableList<Product> list = table.getItems();
        while(rs.next()) {
            Product product = new Product(rs.getString(2), rs.getInt(5), rs.getDouble(4), rs.getDouble(3));
            product.setId(rs.getInt(1));
        }


        titleT.setCellValueFactory(new PropertyValueFactory<>("titleT"));
        actualAmountT.setCellValueFactory(new PropertyValueFactory<>("actualAmountT"));
        differenceT.setCellValueFactory(new PropertyValueFactory<>("differenceT"));
        amountT.setCellValueFactory(new PropertyValueFactory<>("amountT"));
    }
}
