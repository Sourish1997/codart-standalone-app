package question;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.Main;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class QuestionController {

    @FXML
    private JFXButton questionSolved;

    @FXML
    private JFXButton questionForfeited;

    @FXML
    private JFXButton questionBack;

    @FXML
    private Label questionType;

    @FXML
    private JFXListView<?> questionCard;

    @FXML
    private Label questionTitle;

    @FXML
    private Label questionBody;

    @FXML
    private Label questionTimer;

    private String username;
    private Long startTime;
    private Thread timerThread;
    private String difficulty;
    private boolean exit = false;
    private int index;

    @FXML
    void onBackClick(ActionEvent event) throws Exception {
        exit = true;
        loadTabs();
    }

    @FXML
    void onQuestionForfeited(ActionEvent event) throws Exception {
        int neg;
        if(difficulty.equals("e"))
            neg = 20;
        else if(difficulty.equals("m"))
            neg = 15;
        else
            neg = 10;

        BufferedReader reader = new BufferedReader(new FileReader("data/" + username + ".codart"));
        String username = reader.readLine();
        int[] userScoreInfo = new int[9];
        for(int i = 0; i < 9; i++)
            userScoreInfo[i] = Integer.parseInt(reader.readLine());
        reader.close();

        userScoreInfo[5] += 1;
        if(difficulty.equals("e")) {
            userScoreInfo[0] -= 20;
            userScoreInfo[6] += 1;
        } else if(difficulty.equals("m")) {
            userScoreInfo[0] -= 15;
            userScoreInfo[7] += 1;
        } else {
            userScoreInfo[0] -= 10;
            userScoreInfo[8] += 1;
        }

        writeToUserFile(username, userScoreInfo);

        String questions[] = readQuestionsFile();
        String questionData[] = questions[index].split(" ");
        questionData[2] = "F";
        questions[index] = String.join(" ", questionData);
        writeToQuestionFile(username, questions);
    }

    @FXML
    void onSolvedClick(ActionEvent event) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader("data/" + username + ".codart"));
        String username = reader.readLine();
        int[] userScoreInfo = new int[9];
        for(int i = 0; i < 9; i++)
            userScoreInfo[i] = Integer.parseInt(reader.readLine());
        reader.close();

        userScoreInfo[0] += 50;
        userScoreInfo[1] += 1;
        if(difficulty.equals("e"))
            userScoreInfo[2] += 1;
        else if(difficulty.equals("m"))
            userScoreInfo[3] += 1;
        else
            userScoreInfo[4] += 1;

        writeToUserFile(username, userScoreInfo);

        String questions[] = readQuestionsFile();
        String questionData[] = questions[index].split(" ");
        questionData[2] = "S";
        questions[index] = String.join(" ", questionData);
        writeToQuestionFile(username, questions);
    }

    public void writeToQuestionFile(String username, String[] questionsData) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("data/" + username + "_questions.codart"));
        for(String questionData: questionsData) {
            writer.write(questionData);
            writer.newLine();
        }
        writer.close();
    }

    public String[] readQuestionsFile() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("data/" + username + "_questions.codart"));
        ArrayList<String> questionsInfo = new ArrayList<>();
        String questionItem;
        while((questionItem = reader.readLine()) != null)
            questionsInfo.add(questionItem);
        reader.close();
        String questionsInfoArray[] =  questionsInfo.toArray(new String[questionsInfo.size()]);
        return  questionsInfoArray;
    }

    public void writeToUserFile(String username, int[] userScoreInfo) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("data/" + username + ".codart"));
        writer.write(username);
        writer.newLine();
        for(int i = 0; i < 9; i++) {
            writer.write(userScoreInfo[i] + "");
            writer.newLine();
        }
        writer.close();
    }

    public void loadTabs() throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader("data/" + username + ".codart"));
        String username = reader.readLine();
        int[] userScoreInfo = new int[9];
        for(int i = 0; i < 9; i++)
            userScoreInfo[i] = Integer.parseInt(reader.readLine());
        reader.close();
        reader = new BufferedReader(new FileReader("data/" + username + "_questions.codart"));
        ArrayList<String> questionsInfo = new ArrayList<>();
        String questionItem;
        while((questionItem = reader.readLine()) != null)
            questionsInfo.add(questionItem);
        reader.close();
        String questionsInfoArray[] =  questionsInfo.toArray(new String[questionsInfo.size()]);
        Main.loadTabs(username, userScoreInfo, questionsInfoArray);
    }

    public void initVariables(String username, String[] questionData, int questionIndex) throws Exception{
        questionCard.depthProperty().set(1);
        questionForfeited.setDisable(true);
        this.username = username;
        index = questionIndex;
        startTime = Long.parseLong(questionData[3]);
        timerThread = new Thread(() -> {
               while(!exit) {
                   Long seconds = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startTime);
                   if(seconds > 30) {
                       questionForfeited.setDisable(false);
                       Platform.runLater(
                               () -> {
                                   questionTimer.setText("00:00");
                               }
                       );
                       exit = true;
                   } else {
                       int secondsLeft = (int) (30 - seconds);
                       String minutesLeft = ((int)(secondsLeft / 60)) + "";
                       String remainderSeconds = (secondsLeft - (Integer.parseInt(minutesLeft) * 60)) + "";
                       if(minutesLeft.length() == 1)
                           minutesLeft = "0" + minutesLeft;
                       if(remainderSeconds.length() == 1)
                           remainderSeconds = "0" + remainderSeconds;
                       String finalRemainderSeconds = remainderSeconds;
                       String finalMinutesLeft = minutesLeft;
                       Platform.runLater(
                               () -> {
                                   questionTimer.setText(finalMinutesLeft + ":" + finalRemainderSeconds);
                               }
                       );
                   }
                   try {
                       Thread.sleep(1000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
        });

        if(questionData[2].equals("NS"))
            timerThread.start();
        else {
            questionSolved.setDisable(true);
            questionForfeited.setDisable(true);
            questionTimer.setText("    :    ");
        }

        String diff = questionData[0];
        difficulty = diff;
        int qNo = Integer.parseInt(questionData[1]), counter = 1;
        String line, title = "", body = "";
        boolean titleEncountered = false;
        BufferedReader reader = new BufferedReader(new FileReader("data/" + diff + ".codart"));
        while ((line = reader.readLine()) != null) {
            if(qNo == counter && titleEncountered) {
                int minNoOfLines = 26;
                body += line + "\n";
                while (!(line = reader.readLine()).equals("-x-x-x-")) {
                    body += line + "\n";
                    minNoOfLines--;
                }
                while (minNoOfLines >= 0) {
                    body += "\n";
                    minNoOfLines--;
                }
                reader.close();
                break;
            } else if (qNo == counter){
                title = line;
                titleEncountered = true;
            }

            if(line.equals("-x-x-x-"))
                counter++;
        }

        if(diff.equals("e"))
            diff = "EASY";
        else if(diff.equals("m"))
            diff = "MEDIUM";
        else
            diff = "HARD";

        questionTitle.setText("QUESTION: " + diff);
        questionTitle.setText(title);
        questionBody.setText(body);
    }
}

