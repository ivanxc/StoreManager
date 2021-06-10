package sample;

import com.sun.javafx.fxml.FXMLLoaderHelper;
import com.sun.prism.shader.Solid_ImagePattern_Loader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitMenuButton;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    @FXML
    private Button startSellingButton;

    private Stage stage;
    private Scene scene;
    private Parent root;
    String name;
    @FXML
    private SplitMenuButton chooseUser;
    @FXML
    private SplitMenuButton chooseToDelete;
    @FXML
    private Label user;
    @FXML
    SplitMenuButton reportFrom;

    public void enter(ActionEvent actionEvent) throws IOException {
        name = chooseUser.getText();
        Parent root = FXMLLoader.load(getClass().getResource("scenes/mainScene.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        System.out.println(name);
        ((Label) scene.lookup("#user")).setText(name);
        Main.userName = name;
    }

    public void startSelling(ActionEvent actionEvent) throws IOException {
        System.out.println("click");
        Parent root = FXMLLoader.load(getClass().getResource("scenes/sellingScene.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        ((Label) scene.lookup("#user")).setText(Main.userName);
    }

    public void admission(ActionEvent actionEvent) throws IOException {
        System.out.println("click");
        Parent root = FXMLLoader.load(getClass().getResource("scenes/admission.fxml"));
        FXMLLoader.load(getClass().getResource("scenes/admission.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        ((Label) scene.lookup("#user")).setText(Main.userName);
    }

    public void inventory(ActionEvent actionEvent) throws IOException {
        System.out.println("click");
        Parent root = FXMLLoader.load(getClass().getResource("scenes/inventory.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        ((Label) scene.lookup("#user")).setText(Main.userName);
    }

    public void reports(ActionEvent actionEvent) throws IOException {
        System.out.println("click");
        Parent root = FXMLLoader.load(getClass().getResource("scenes/reports.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        ((Label) scene.lookup("#user")).setText(Main.userName);
    }

    public void control(ActionEvent actionEvent) throws IOException {
        System.out.println("click");
        System.out.println(scene);
        Parent root = FXMLLoader.load(getClass().getResource("scenes/control.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        System.out.println(scene);
        stage.setScene(scene);
        stage.show();
        ((Label) scene.lookup("#user")).setText(Main.userName);
    }

    public void toSignIn(ActionEvent actionEvent) throws IOException {
        System.out.println("click");
        Parent root = FXMLLoader.load(getClass().getResource("scenes/signin.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void addUser(ActionEvent actionEvent) throws IOException {
        System.out.println("click");
        Parent root = FXMLLoader.load(getClass().getResource("scenes/controlUsers.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void addFacility(ActionEvent actionEvent) throws IOException {
        System.out.println("click");
        Parent root = FXMLLoader.load(getClass().getResource("scenes/controlFacilities.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void clickPersonal(ActionEvent actionEvent) throws IOException {
        chooseToDelete.setText("Персонал");
    }

    public void clickChecks(ActionEvent actionEvent) throws IOException {
        chooseToDelete.setText("Чеки");
    }

    public void clickChecksDetails(ActionEvent actionEvent) throws IOException {
        chooseToDelete.setText("Детали чеков");
    }

    public void clickProducts(ActionEvent actionEvent) throws IOException {
        chooseToDelete.setText("Продукты");
    }

    public void clickAdmissions(ActionEvent actionEvent) throws IOException {
        chooseToDelete.setText("Поставки");
    }

    public void clickAdmissionsDetails(ActionEvent actionEvent) throws IOException {
        chooseToDelete.setText("Детали поставок");
    }

    public void day(ActionEvent actionEvent) throws IOException {
        reportFrom.setText("день");
    }

    public void week(ActionEvent actionEvent) throws IOException {
        reportFrom.setText("неделя");
    }

    public void month(ActionEvent actionEvent) throws IOException {
        reportFrom.setText("месяц");
    }

    public void quarter(ActionEvent actionEvent) throws IOException {
        reportFrom.setText("квартал");
    }

    public void year(ActionEvent actionEvent) throws IOException {
        reportFrom.setText("год");

    }public void allTime(ActionEvent actionEvent) throws IOException {
        reportFrom.setText("все время");
    }

}
