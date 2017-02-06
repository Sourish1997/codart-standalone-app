package body;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.effects.JFXDepthManager;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class TabsController implements Initializable{
    @FXML
    private JFXTabPane tabPane;

    @FXML
    private PlayAreaController playAreaController;

    @FXML
    private LeaderboardController leaderboardController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}
}
