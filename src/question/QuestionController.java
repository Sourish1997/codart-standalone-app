package question;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import main.Main;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

//TODO: Clean Up This Class
public class QuestionController {

    @FXML
    private JFXButton questionSolved;

    @FXML
    private JFXButton questionForfeited;

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

    @FXML
    private StackPane mainStack;

    private String username;
    private Long startTime;
    private Thread timerThread;
    private String difficulty;
    private boolean exit = false;
    private int index, qNo;

    @FXML
    void onBackClick(ActionEvent event) throws Exception {
        exit = true;
        loadTabs();
    }

    @FXML
    void onQuestionForfeited(ActionEvent event) throws Exception {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Label(""));
        content.setBody(new Label("Are you sure you want to forfeit?"));
        JFXButton yesButton = new JFXButton("Yes");
        JFXButton noButton = new JFXButton("No ");
        yesButton.getStylesheets().add("/main/mainStyle.css");
        noButton.getStylesheets().add("/main/mainStyle.css");
        yesButton.getStyleClass().add("rect-button-raised");
        noButton.getStyleClass().add("red-rect-button-raised");

        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(yesButton, noButton);
        content.setActions(hbox);

        JFXDialog  dialog = new JFXDialog(mainStack, content, JFXDialog.DialogTransition.CENTER);
        dialog.setOverlayClose(false);
        dialog.show();

        yesButton.setOnAction(e -> {
            addNewQuestion(1);
        });

        noButton.setOnAction(e -> {
            dialog.close();
        });
    }

    @FXML
    void onQuestionSolved(ActionEvent event) throws Exception {
        addNewQuestion(2);
    }

    public void addNewQuestion(int callId) {
        JFXButton okButton = new JFXButton("Ok");
        okButton.getStylesheets().add("/main/mainStyle.css");
        okButton.getStyleClass().add("rect-button-raised");

        final ToggleGroup group = new ToggleGroup();

        JFXRadioButton easy = new JFXRadioButton("Easy");
        easy.setPadding(new Insets(10));
        easy.setToggleGroup(group);

        JFXRadioButton medium = new JFXRadioButton("Medium");
        medium.setPadding(new Insets(10));
        medium.setToggleGroup(group);

        JFXRadioButton hard = new JFXRadioButton("Hard");
        hard.setPadding(new Insets(10));
        hard.setToggleGroup(group);

        VBox vbox = new VBox(10);
        vbox.getChildren().add(new Label("Enter Difficulty of next question: "));
        vbox.getChildren().add(easy);
        vbox.getChildren().add(medium);
        vbox.getChildren().add(hard);
        vbox.setSpacing(10);

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Label(""));
        content.setBody(vbox);
        content.setActions(okButton);

        JFXDialog  dialog = new JFXDialog(mainStack, content, JFXDialog.DialogTransition.CENTER);

        okButton.setOnAction(e2 -> {
            try {
                String selected = group.getSelectedToggle().toString();
                selected = selected.charAt(selected.length() - 2) + "";

                BufferedReader reader = new BufferedReader(new FileReader("data/noq.codart"));
                int qNos[] = new int[3];
                qNos[0] = Integer.parseInt(reader.readLine());
                qNos[1] = Integer.parseInt(reader.readLine());
                qNos[2] = Integer.parseInt(reader.readLine());
                reader.close();

                reader = new BufferedReader(new FileReader("data/" + username + "_qdata.codart"));
                String solvedState[][] = new String[3][];
                solvedState[0] = reader.readLine().split(" ");
                solvedState[1] = reader.readLine().split(" ");
                solvedState[2] = reader.readLine().split(" ");
                reader.close();

                String newQuestionContainer[];
                int totalChosenQTypeNumber;
                if (selected.equals("y")) {
                    newQuestionContainer = solvedState[0];
                    totalChosenQTypeNumber = qNos[0];
                    selected = "e";
                } else if (selected.equals("m")) {
                    newQuestionContainer = solvedState[1];
                    totalChosenQTypeNumber = qNos[1];
                    selected = "m";
                } else {
                    newQuestionContainer = solvedState[2];
                    totalChosenQTypeNumber = qNos[2];
                    selected = "h";
                }

                int no = ((int) Math.random()) % totalChosenQTypeNumber;
                int found = -1;
                for (int i = no; i < newQuestionContainer.length; i++) {
                    if (newQuestionContainer[i].equals("F")) {
                        found = i;
                        break;
                    }
                }

                if (found == -1) {
                    for (int i = 0; i < no; i++) {
                        if (newQuestionContainer[i].equals("F")) {
                            found = i;
                            break;
                        }
                    }
                }

                if(found == -1) {
                    //TODO: display snackbar here
                    dialog.close();
                } else {
                    dialog.close();
                    confirmTransaction(selected, found, callId);
                }
            } catch (Exception e) {}
        });

        dialog.setOverlayClose(false);
        dialog.show();
    }

    public void confirmTransaction(String selected, int found, int callId) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Label("Enter Verification: "));
        JFXPasswordField pField = new JFXPasswordField();
        content.setBody(pField);
        JFXButton okButton = new JFXButton("Ok");
        okButton.getStylesheets().add("/main/mainStyle.css");
        okButton.getStyleClass().add("rect-button-raised");
        content.setActions(okButton);

        JFXDialog  dialog = new JFXDialog(mainStack, content, JFXDialog.DialogTransition.CENTER);
        dialog.setOverlayClose(false);

        okButton.setOnAction(e -> {
            if(pField.getText().equals("zacker")) {
                try {
                    if(callId == 1) {
                        BufferedReader reader = new BufferedReader(new FileReader("data/" + username + ".codart"));
                        String username = reader.readLine();
                        int[] userScoreInfo = new int[9];
                        for (int i = 0; i < 9; i++)
                            userScoreInfo[i] = Integer.parseInt(reader.readLine());
                        reader.close();

                        userScoreInfo[5] += 1;
                        if (difficulty.equals("e")) {
                            userScoreInfo[0] -= 20;
                            userScoreInfo[6] += 1;
                        } else if (difficulty.equals("m")) {
                            userScoreInfo[0] -= 15;
                            userScoreInfo[7] += 1;
                        } else {
                            userScoreInfo[0] -= 10;
                            userScoreInfo[8] += 1;
                        }

                        writeToUserFile(username, userScoreInfo);
                        writeToUserQuestionData();

                        String questions[] = readQuestionsFile();
                        String questionData[] = questions[index].split(" ");
                        questionData[2] = "F";
                        questions[index] = String.join(" ", questionData);
                        writeToQuestionFile(username, questions);
                    } else {
                        BufferedReader reader = new BufferedReader(new FileReader("data/" + username + ".codart"));
                        String username = reader.readLine();
                        int[] userScoreInfo = new int[9];
                        for (int i = 0; i < 9; i++)
                            userScoreInfo[i] = Integer.parseInt(reader.readLine());
                        reader.close();

                        userScoreInfo[0] += 50;
                        userScoreInfo[1] += 1;
                        if (difficulty.equals("e"))
                            userScoreInfo[2] += 1;
                        else if (difficulty.equals("m"))
                            userScoreInfo[3] += 1;
                        else
                            userScoreInfo[4] += 1;

                        writeToUserFile(username, userScoreInfo);
                        writeToUserQuestionData();

                        String questions[] = readQuestionsFile();
                        String questionData[] = questions[index].split(" ");
                        questionData[2] = "S";
                        questions[index] = String.join(" ", questionData);
                        writeToQuestionFile(username, questions);
                    }

                    String newQuestionData = selected + " " + (found + 1) + " NS " + System.nanoTime();
                    BufferedWriter writer = new BufferedWriter(new FileWriter("data/" + username + "_questions.codart", true));
                    writer.write(newQuestionData);
                    writer.newLine();
                    writer.close();
                    dialog.close();
                    loadTabs();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        dialog.setOverlayClose(false);
        dialog.show();
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

    public void writeToQuestionFile(String username, String[] questionsData) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("data/" + username + "_questions.codart"));
        for(String questionData: questionsData) {
            writer.write(questionData);
            writer.newLine();
        }
        writer.close();
    }

    public void writeToUserQuestionData() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("data/" + username + "_qdata.codart"));
        String solvedState[][] = new String[3][];
        solvedState[0] = reader.readLine().split(" ");
        solvedState[1] = reader.readLine().split(" ");
        solvedState[2] = reader.readLine().split(" ");
        reader.close();
        if(difficulty.equals("e")) {
            solvedState[0][qNo - 1] = "T";
        } else if(difficulty.equals(("m"))) {
            solvedState[1][qNo - 1] = "T";
        } else {
            solvedState[2][qNo - 1] = "T";
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter("data/" + username + "_qdata.codart"));
        for(int i = 0; i < 3; i++) {
            writer.write(String.join(" ", solvedState[i]));
            writer.newLine();
        }
        writer.close();
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
        qNo = Integer.parseInt(questionData[1]);
        index = questionIndex;
        startTime = Long.parseLong(questionData[3]);
        this.username = username;

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
                       String minutesLeft = (secondsLeft / 60) + "";
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

        difficulty = questionData[0];

        int counter = 1;
        String line, title = "", body = "";
        boolean titleEncountered = false;
        BufferedReader reader = new BufferedReader(new FileReader("data/" + difficulty + ".codart"));
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

        if(difficulty.equals("e"))
            questionType.setText("QUESTION: EASY");
        else if(difficulty.equals("m"))
            questionType.setText("QUESTION: MEDIUM");
        else
            questionType.setText("QUESTION: HARD");

        questionTitle.setText(title);
        questionBody.setText(body);
    }
}