package login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import main.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable{
    @FXML
    ImageView login_logo;

    @FXML
    private VBox loginPanel;

    @FXML
    private JFXButton login_register_button;

    @FXML
    private JFXPasswordField login_password;

    @FXML
    private JFXTextField login_username;

    private Main main;

    @FXML
    void registerButtonClicked(ActionEvent event) throws IOException {
        main.loadTabs();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new File("images/codart.png"));
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            login_logo.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
