package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SellingController {
    @FXML
    private Label user;
    @FXML
    private TextField title;
    @FXML
    private TextField barcode;
    @FXML
    private TextField amount;
    @FXML
    private TextField titleUnknownProduct;
    @FXML
    private TextField priceUnknownProduct;
    @FXML
    private TextField amountUnknownProduct;
    @FXML
    private TableView table;
    @FXML
    private TableColumn<Product, String> titleT;
    @FXML
    private TableColumn<Product, String> priceT;
    @FXML
    private TableColumn<Product, String> amountT;
    @FXML
    private TableColumn<Product, String> totalT;
    @FXML
    private double sum = 0.0;
    @FXML
    private Label totalPrice;
    private ObservableList<Product> productsInCheck = FXCollections.observableArrayList();
    private int admissionId;

    public void addProduct() throws SQLException {
        barcode.setStyle("-fx-border-color: white");
        amount.setStyle("-fx-border-color: white");
        if (!barcode.getText().matches("\\d*")) {
            barcode.setStyle("-fx-border-color: red");
            return;
        }
        if (amount.getText().matches("\\d+[.,]?\\d*") || !checkAmount()) {
            amount.setText(amount.getText().replace(',', '.'));
        } else {
            amount.setStyle("-fx-border-color: red");
            return;
        }

        Connection connection = Main.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM find_product_by_title_or_barcode('" +
                title.getText() + "', " + Integer.parseInt(barcode.getText()) + ");");
        if (!resultSet.next()) {
            barcode.setStyle("-fx-border-color: red");
            amount.setStyle("-fx-border-color: red");
            return;
        }
        String price = resultSet.getString(3);
        int id = resultSet.getInt(1);
        Product product = new Product(title.getText(),
                Integer.parseInt(barcode.getText()),
                Double.parseDouble(price),
                Double.parseDouble(amount.getText()));
        product.setId(id);
        product.setTotalT(product.getPriceT() * product.getAmountT());
        ObservableList<Product> list = table.getItems();

        boolean isProductFound = false;

        for(int i = 0; i < list.size(); i++) {
            Product pr = list.get(i);
            if (pr.getTitleT().equals(product.getTitleT()) &&
                pr.getBarcodeT() == product.getBarcodeT()) {
                pr.setTotalT(pr.getTotalT() + product.getTotalT());
                pr.setAmountT(pr.getAmountT() + product.getAmountT());
                isProductFound = true;
            }
        }
        if (!isProductFound) {
            list.add(product);
        }
        table.setItems(list);
        table.refresh();
        title.clear();
        barcode.clear();
        amount.clear();
        productsInCheck.add(product);
        sum += product.getTotalT();
        totalPrice.setText("К оплате: " + sum + " руб.");
    }

    public void createCheck() throws SQLException {
        Connection connection = Main.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM get_cashier_id('" + SignInController.name + "');");
        resultSet.next();
        int cashierId = resultSet.getInt(1);
        resultSet = statement.executeQuery("SELECT * FROM add_check(" + sum + ", " + cashierId + ");");
        resultSet.next();
        int checkId = resultSet.getInt(1);
        for(int i = 0; i < productsInCheck.size(); i++) {
            Product product = productsInCheck.get(i);
            resultSet = statement.executeQuery("SELECT * FROM add_selling(" + product.getId() + ", " +
                   checkId + ", " + product.getTotalT() + ", " + product.getAmountT() + ");");
            resultSet = statement.executeQuery("SELECT * FROM sell_product_by_id(" + product.getId() + ", " +
                    product.getAmountT() + ");");
        }
        title.setStyle("-fx-border-color: white");
        barcode.setStyle("-fx-border-color: white");
        amount.setStyle("-fx-border-color: white");
        table.getItems().clear();
        totalPrice.setText("К оплате: 0 руб.");
        productsInCheck.clear();
    }

    public void showPromptTitle() throws SQLException {
        Connection connection = Main.connection;
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM find_product_by_title_or_barcode('" + title.getText() + "');");
        LinkedList<String> suggestedProducts = new LinkedList<>();
        while(rs.next()) {
            suggestedProducts.add(rs.getString(2));
        }
        String[] suggestedProductsAsArray = (suggestedProducts.stream().collect(Collectors.toSet())).toArray(new String[0]);
        TextFields.bindAutoCompletion(title, suggestedProductsAsArray);
    }

    public void showPromptBarcode() throws SQLException {
        Connection connection = Main.connection;
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM find_product_by_title_or_barcode('" + title.getText() + "');");
        LinkedList<String> suggestedBarcodes = new LinkedList<>();
        while(rs.next()) {
            suggestedBarcodes.add(rs.getString(4));
        }

        rs = statement.executeQuery("SELECT * FROM find_product_by_title_or_barcode('" + barcode.getText() + "');");
        LinkedList<String> suggestedBarcodes2 = new LinkedList<>();
        while(rs.next()) {
            suggestedBarcodes2.add(rs.getString(4));
        }


        (suggestedBarcodes.stream().collect(Collectors.toSet())).retainAll(suggestedBarcodes2.stream().collect(Collectors.toSet()));
        String[] suggestedBarcodesAsArray = suggestedBarcodes.toArray(new String[0]);
        TextFields.bindAutoCompletion(barcode, suggestedBarcodesAsArray);
    }

    public boolean checkAmount() throws SQLException {
        Connection connection = Main.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM find_product_by_title_or_barcode('" +
                title.getText() + "', " + Integer.parseInt(barcode.getText()) + ");");
        resultSet.next();
        double totalAmount = resultSet.getDouble(5);
        String productTitle = resultSet.getString(2);
        int productBarcode = resultSet.getInt(4);
        Stream<Product> productStream = productsInCheck.stream().filter(product ->
                product.getTitleT() == productTitle && product.getBarcodeT() == productBarcode);
        List<Product> products = productStream.collect(Collectors.toList());
        double totalAmountInCheck = 0;
        for (Product pr : products) {
            totalAmountInCheck += pr.getAmountT();
        }
        if (totalAmountInCheck + Double.parseDouble(amount.getText()) <= totalAmount) {
            return true;
        } else {
            return false;
        }
    }

    public void initialize() {
        titleT.setCellValueFactory(new PropertyValueFactory<>("titleT"));
        priceT.setCellValueFactory(new PropertyValueFactory<>("priceT"));
        amountT.setCellValueFactory(new PropertyValueFactory<>("amountT"));
        totalT.setCellValueFactory(new PropertyValueFactory<>("totalT"));
    }
}
