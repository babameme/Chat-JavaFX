package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable{
    @FXML
    public ImageView userImageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("images/default.png");
        Image image = new Image(file.toURI().toString());
        userImageView.setImage(image);
    }
}
