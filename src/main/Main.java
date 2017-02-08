package main;

import body.TabsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import question.QuestionController;

import java.io.IOException;

public class Main extends Application {
    public static Stage pStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        pStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/login/Login.fxml"));
        pStage.setTitle("Codart");
        pStage.setScene(new Scene(root, 700, 650));
        pStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void loadTabs(String username, int[] userData, String[] questionsData) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/body/Tabs.fxml"));
        Parent root = loader.load();
        TabsController controller = loader.<TabsController>getController();
        controller.initVariables(username, userData, questionsData);
        pStage.setScene(new Scene(root, 700, 650));
    }

    public static void loadQuestion(String username, String[] questionData, int questionIndex) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/question/Question.fxml"));
        Parent root = loader.load();
        QuestionController controller = loader.<QuestionController>getController();
        controller.initVariables(username, questionData, questionIndex);
        pStage.setScene(new Scene(root, 700, 650));
    }

    public static void loadLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/login/Login.fxml"));
        Parent root = loader.load();
        pStage.setScene(new Scene(root, 700, 650));
    }
}
