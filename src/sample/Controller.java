package sample;

import data.DataAccess;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Controller {
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

    public void connectMouseClicked(MouseEvent event){
        String user = txtUsername.getText();
        if (user == null) user = "";

        String pass = passField.getText();
        if (pass == null) pass = "";

        try {
            Connection sql = DataAccess.connection();
            Statement comm = sql.createStatement();
            comm = sql.createStatement();
            String strSql = "SELECT * FROM users WHERE user = \"" + user + "\" AND password = \"" + pass +"\"";
            ResultSet rs = comm.executeQuery(strSql);
            if (!rs.next()){
                System.out.println("Not exists users");
            }
            else{
                System.out.println("Connect successful");
            }
        } catch (Exception e) {
            System.out.println("Error when connecting Database" + e.getMessage());
        }

    }
}
