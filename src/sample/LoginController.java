package sample;

import data.DataAccess;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class LoginController implements Initializable{
    @FXML
    public Button buttonConnect;
    @FXML
    public TextField txtUsername;
    @FXML
    public TextField txtHostname;
    @FXML
    public TextField txtPort;
    @FXML
    public PasswordField passField;
    @FXML
    public ImageView imageViewDefault;

    public Connection sql;
    public Statement comm;
    public String strSql;
    private Scene scene;

    public void connectMouseClicked(MouseEvent event){
        String user = txtUsername.getText();
        if (user == null) user = "";

        String pass = passField.getText();
        if (pass == null) pass = "";

        try {
            strSql = "SELECT * FROM users WHERE user = \"" + user + "\" AND password = \"" + pass +"\"";
            ResultSet rs = comm.executeQuery(strSql);
            if (!rs.next()){
                //System.out.println("Not exists users");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Sai tên tài khoản hoặc mật khẩu!");
                alert.setContentText("Vui lòng kiểm tra lại");
                alert.showAndWait();
            }
            else{
                //System.out.println("Connect successful");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Đăng nhập thành công");
                alert.setContentText("Chào mừng " + user + " đến với C500 Chat");
                alert.showAndWait();
                Stage stage = (Stage) buttonConnect.getScene().getWindow();
                stage.close();

                Parent rootChat = FXMLLoader.load(getClass().getResource("ChatUI.fxml"));
                stage.setTitle("C500 Chat");
                stage.setScene(new Scene(rootChat));
                stage.setResizable(false);
                stage.show();
            }
        } catch (Exception e) {
            System.out.println("Error when excute Database query " + e.getMessage());
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("images/default.png");
        Image image = new Image(file.toURI().toString());
        imageViewDefault.setImage(image);

        try{
            sql = DataAccess.connection();
            comm = sql.createStatement();
        }catch (Exception e){
            System.out.println("Error when connecting Database" + e.getMessage());
        }
    }
}
