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
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoginController implements Initializable{
    @FXML
    ImageView loginLogo;

    @FXML
    private VBox loginPanel;

    @FXML
    private JFXButton loginRegisterButton;

    @FXML
    private JFXPasswordField loginPassword;

    @FXML
    private JFXTextField loginUsername;

    //private Main main;

    @FXML
    void registerButtonClicked(ActionEvent event) throws Exception {
        String userAndPass;
        String authArray[];
        BufferedReader reader = new BufferedReader(new FileReader("data/auth.codart"));
        while ((userAndPass = reader.readLine()) != null) {
            authArray = userAndPass.split(" ");
            if(authArray[0].equals(loginUsername.getText()) && authArray[1].equals(loginPassword.getText())) {
                reader.close();
                File userData = new File("data/" + loginUsername.getText() + ".codart");
                if(userData.exists()) {
                    reader = new BufferedReader(new FileReader("data/" + loginUsername.getText() + ".codart"));
                    String username = reader.readLine();
                    int[] userScoreInfo = new int[9];
                    for(int i = 0; i < 9; i++)
                        userScoreInfo[i] = Integer.parseInt(reader.readLine());
                    reader.close();
                    reader = new BufferedReader(new FileReader("data/" + loginUsername.getText() + "_questions.codart"));
                    ArrayList<String> questionsInfo = new ArrayList<>();
                    String questionItem;
                    while((questionItem = reader.readLine()) != null)
                        questionsInfo.add(questionItem);
                    reader.close();
                    String questionsInfoArray[] =  questionsInfo.toArray(new String[questionsInfo.size()]);
                    Main.loadTabs(username, userScoreInfo, questionsInfoArray);
                } else {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("data/" + loginUsername.getText() + ".codart"));
                    writer.write(loginUsername.getText());
                    writer.newLine();
                    for(int i = 0; i < 9; i++) {
                        writer.write("0");
                        writer.newLine();
                    }
                    writer.close();
                    writer = new BufferedWriter(new FileWriter("data/" + loginUsername.getText() + "_questions.codart"));
                    String questionDetails = "e 1 NS " + System.nanoTime();
                    writer.write(questionDetails + "\n");
                    writer.close();
                    Main.loadTabs(loginUsername.getText(), new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0}, new String[]{questionDetails});
                }
                return;
            }
        }
        //Show invalid auth credentials snackbar here
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new File("images/codart.png"));
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            loginLogo.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
