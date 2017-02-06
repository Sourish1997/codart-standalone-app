package body;

import com.jfoenix.controls.JFXListView;
import com.sun.org.apache.xml.internal.security.Init;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
    private JFXListView<?> solvedList;

    @FXML
    private JFXListView<?> forfeitedList;

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
}

