package sample;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.textfield.TextFields;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class inventoryController {
    @FXML
    private TextField diff;
    @FXML
    private TextField title;
    @FXML
    private TableView table;
    @FXML
    private TextField amountField;
    @FXML
    private TableColumn<Product, String> titleT;
    @FXML
    private TableColumn<Product, String> amountT;
    @FXML
    private TableColumn<Product, String> actualAmountT;
    @FXML
    private TableColumn<Product, String> differenceT;
    @FXML
    private Button deleteButton;
    @FXML
    private Button confirmButton;
    @FXML
    private Button arrangeButton;
    private Product clickedProduct;
    private double differenceSum = 0.0;
    private LinkedList<Product> updatedProducts = new LinkedList<>();

    public void confirm() {
        clickedProduct.setActualAmountT(Double.parseDouble(amountField.getText()));
        clickedProduct.setDifferenceT(clickedProduct.getActualAmountT() - clickedProduct.getAmountT());
        confirmButton.setDisable(true);
        differenceSum += clickedProduct.getDifferenceT();
        diff.setText(String.valueOf(differenceSum));
        updatedProducts.add(clickedProduct);
        arrangeButton.setDisable(false);
        table.refresh();
    }

    public void inputAmount() {
        confirmButton.setDisable(false);
    }

    public void delete() throws SQLException {
        Connection connection = Main.connection;
        Statement statement = connection.createStatement();
        statement.execute("SELECT delete_product(" + clickedProduct.getId() + ");");
        table.getItems().remove(clickedProduct);
        table.refresh();
    }

    public void arrange() throws SQLException {
        Connection connection = Main.connection;
        Statement statement = connection.createStatement();
        for(int i = 0; i < updatedProducts.size(); i++) {
            Product product = updatedProducts.get(i);
            product.setAmountT(product.getActualAmountT());
            statement.execute("SELECT change_product_amount(" + product.getId() + ", " + product.getActualAmountT() + ");");
        }
        table.refresh();
        diff.setText("");
    }

    public void suggest() throws SQLException {
        Connection connection = Main.connection;
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM find_product_by_title_or_barcode('" + title.getText() + "');");
        LinkedList<String> suggestedProducts = new LinkedList<>();
        table.getItems().clear();
        ObservableList<Product> list = table.getItems();
        while(rs.next()) {
            Product product = new Product(rs.getString(2), rs.getInt(4), rs.getDouble(3), rs.getDouble(5));
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
            Product product = new Product(rs.getString(2), rs.getInt(4), rs.getDouble(3), rs.getDouble(5));
            product.setId(rs.getInt(1));
            product.setActualAmountT(product.getAmountT());
            product.setDifferenceT(product.getActualAmountT() - product.getAmountT());
            list.add(product);
        }
        table.setItems(list);

        titleT.setCellValueFactory(new PropertyValueFactory<>("titleT"));
        actualAmountT.setCellValueFactory(new PropertyValueFactory<>("actualAmountT"));
        differenceT.setCellValueFactory(new PropertyValueFactory<>("differenceT"));
        amountT.setCellValueFactory(new PropertyValueFactory<>("amountT"));

        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                if (table.getSelectionModel().getSelectedItem() == null) {
                    deleteButton.setDisable(true);
                    return;
                }
                clickedProduct = (Product)table.getSelectionModel().getSelectedItem();
                actualAmountT.setEditable(true);
                deleteButton.setDisable(false);
            }
        });

    }
}
