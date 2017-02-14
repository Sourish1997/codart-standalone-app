package body;

import com.jfoenix.controls.JFXListView;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LeaderboardController implements Initializable {
    @FXML
    JFXListView leaderCard;

    @FXML
    VBox leaderMessage;

    @FXML
    ImageView constrLogo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        leaderCard.depthProperty().set(1);

        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new File("images/constr.png"));
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            constrLogo.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
