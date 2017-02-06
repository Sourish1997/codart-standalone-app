package question;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class QuestionController implements Initializable{

    @FXML
    private JFXListView<?> questionCard;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        questionCard.depthProperty().set(1);
    }
}
