package body;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.sun.org.apache.xml.internal.security.Init;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DrawerContentController implements Initializable{

    @FXML
    private ImageView profilePic;

    @FXML
    private Label drawerUsername;

    @FXML
    private JFXButton logoutButton;

    @FXML
    private Label scoreLabel;

    @FXML
    private JFXListView<?> solvedList;

    @FXML
    private Label solvedLabel;

    @FXML
    private Label solvedEasy;

    @FXML
    private Label solvedMedium;

    @FXML
    private Label solvedHard;

    @FXML
    private JFXListView<?> forfeitedList;

    @FXML
    private Label forfeitedLabel;

    @FXML
    private Label forfeitedEasy;

    @FXML
    private Label forfeitedMedium;

    @FXML
    private Label forfeitedHard;

    @FXML
    void logoutClicked(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new File("images/avatar.png"));
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            profilePic.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initVariables(String username, int[] userData) {
        drawerUsername.setText(username.substring(0, 1).toUpperCase() + username.substring(1));
        scoreLabel.setText("Total Score: " + userData[0]);
        solvedLabel.setText("Questions Solved: " + userData[1]);
        solvedEasy.setText("Easy: " + userData[2]);
        solvedMedium.setText("Medium: " + userData[3]);
        solvedHard.setText("Hard: " + userData[4]);
        forfeitedLabel.setText("Questions Forfeited: " + userData[5]);
        forfeitedEasy.setText("Easy: " + userData[6]);
        forfeitedMedium.setText("Medium: " + userData[7]);
        forfeitedHard.setText("Hard: " + userData[8]);
    }
}