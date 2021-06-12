package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class AdmissionController {
    @FXML
    private TextField title;
    @FXML
    private TextField barcode;
    @FXML
    private TextField price;
    @FXML
    private TextField amount;
    @FXML
    private TableView<Product> table;
    @FXML
    private TableColumn<Product, String> titleT;
    @FXML
    private TableColumn<Product, String> priceT;
    @FXML
    private TableColumn<Product, String> amountT;
    @FXML
    private TableColumn<Product, String> barcodeT;
    @FXML
    private TextField admissionTitle;
    private ObservableList<Product> productsInCheck = FXCollections.observableArrayList();
    private int admissionId;

    public void addProduct() {
        barcode.setStyle("-fx-border-color: white");
        price.setStyle("-fx-border-color: white");
        amount.setStyle("-fx-border-color: white");
        admissionTitle.setStyle("-fx-border-color: white");
        if (!barcode.getText().matches("\\d*")) {
            barcode.setStyle("-fx-border-color: red");
            return;
        }
        if (price.getText().matches("\\d*[.,]?\\d*")) {
            price.setText(price.getText().replace(',', '.'));
        } else {
            price.setStyle("-fx-border-color: red");
            return;
        }
        if (amount.getText().matches("\\d+[.,]?\\d*")) {
            amount.setText(amount.getText().replace(',', '.'));
        } else {
            amount.setStyle("-fx-border-color: red");
            return;
        }
        Product product = new Product(title.getText(),
                                      Integer.parseInt(barcode.getText()),
                                      Double.parseDouble(price.getText()),
                                      Double.parseDouble(amount.getText()));
        ObservableList<Product> list = table.getItems();
        list.add(product);
        table.setItems(list);
        title.clear();
        barcode.clear();
        price.clear();
        amount.clear();
        productsInCheck.add(product);
    }

    public void addAdmission() throws SQLException, IOException {
        if (admissionTitle.getText().length() == 0) {
            admissionTitle.setStyle("-fx-border-color: red");
            return;
        }
        Connection connection = Main.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM add_admission('" + admissionTitle.getText() + "');");
        resultSet.next();
        admissionId = resultSet.getInt(1);
        for(Product product : productsInCheck) {
            ResultSet rs = statement.executeQuery("SELECT * FROM find_product_by_title_or_barcode('" +product.getTitleT() + "');");
            int id = -2;
            if (rs.next()) {
                showModalWindow(product);
                id = bindProductController.choosedId;
                System.out.println(id + " rsnext");
            }
            System.out.println(id + " rsnext ended");
            if (id != -1 && id != -2) {
                statement.execute("SELECT update_product_price(" + id + ", " + product.getPriceT() + ");");
                statement.execute("SELECT update_product_amount(" + id + ", " + product.getAmountT() + ");");
                System.out.println(id + " yes2");
            } else {
                String sql = "SELECT * FROM add_products('" + product.getTitleT() +
                                "', " + product.getPriceT() + ", " + product.getBarcodeT() + ", " +
                                product.getAmountT() + ");";
                ResultSet idSet = statement.executeQuery(sql);
                idSet.next();
                id = idSet.getInt(1);
                System.out.println(id );
                System.out.println(id + " yes3");
            }
           // String sql = String.format("SELECT add_admission_product({0}, {1}, {2}, {3});", id, admissionId, product.getPriceT(), product.getAmountT());
            String sql = "SELECT * FROM add_admission_product(" + id +
                    ", " + admissionId + ", " + product.getPriceT() + ", " +
                    product.getAmountT() + ");";
            statement.execute(sql);
            System.out.println(id + " yes4");
        }
        barcode.setStyle("-fx-border-color: white");
        price.setStyle("-fx-border-color: white");
        amount.setStyle("-fx-border-color: white");
        admissionTitle.setStyle("-fx-border-color: white");
        table.getItems().clear();
        productsInCheck.clear();
    }

    public void showModalWindow(Product product) throws IOException {
        bindProductController.productToBind = product;
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/scenes/bindProduct.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("My modal window");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(title.getScene().getWindow());
        stage.showAndWait();
    }

    public void suggest() throws SQLException {
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

    public void setTitleAdm() {
        admissionTitle.setStyle("-fx-border-color: white");
    }

    public void initialize() {
        titleT.setCellValueFactory(new PropertyValueFactory<>("titleT"));
        barcodeT.setCellValueFactory(new PropertyValueFactory<>("barcodeT"));
        priceT.setCellValueFactory(new PropertyValueFactory<>("priceT"));
        amountT.setCellValueFactory(new PropertyValueFactory<>("amountT"));
    }
}
