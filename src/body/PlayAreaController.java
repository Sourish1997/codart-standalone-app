package body;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import javax.xml.soap.SOAPPart;
import java.io.IOException;
import java.net.SocketPermission;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayAreaController implements Initializable{
    @FXML
    public BorderPane playArea;

    @FXML
    private JFXHamburger playBurger;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private JFXListView<?> questionsList;

    HamburgerSlideCloseTransition transition;

    FXMLLoader loader;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("DrawerContent.fxml"));
            VBox drawerContent = loader.load();
            drawer.setSidePane(drawerContent);
        } catch (Exception e) {}

        transition = new HamburgerSlideCloseTransition(playBurger);
        transition.setRate(-1);

        questionsList.depthProperty().set(1);

        drawer.setOnDrawerOpening((e) -> {
            transition.setRate(1);
            transition.play();
        });

        drawer.setOnDrawerClosing((e) -> {
            transition.setRate(-1);
            transition.play();
        });
    }

    @FXML
    void toggleDrawer(MouseEvent event) {
        if (drawer.isHidden() || drawer.isHidding())
            drawer.open();
        else
            drawer.close();
    }

    public void initVariables(String username, int[] userData, String[] questionData) {
        DrawerContentController controller = loader.<DrawerContentController>getController();
        controller.initVariables(username, userData);
    }
}
