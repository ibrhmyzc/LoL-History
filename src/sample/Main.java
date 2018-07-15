package sample;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application  implements EventHandler<ActionEvent> {
    private Button button = null;
    private TextField textFieldUserName = null;
    private Label labelEnterYourName = null;
    private TextArea textAreaShowProgress = null;
    private ProgressBar progressBarPercentage = null;
    private Stage mainWindow = null;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("LoL History Saver");
        primaryStage.setScene(new Scene(root, 300, 350));
        primaryStage.setResizable(false);
        mainWindow = primaryStage;
        setGui();
    }

    /**
     * This method is used for setting up the gui
     * it shows a textbox, a label and a button
     * and it informs the user what actions are being taken
     */
    private void setGui()

    {
        // setting up labels
        labelEnterYourName = new Label("Username");

        // creating a textbox for user to enter its name
        textFieldUserName = new TextField("canberk2007");

        // creating text area for informing user about the progress
        textAreaShowProgress = new TextArea("*Program started\n");
        textAreaShowProgress.setPrefWidth(275);

        // create a button
        button = new Button();
        button.setText("Save");
        button.setOnAction(this);

        // progress bar
        progressBarPercentage = new ProgressBar();
        progressBarPercentage.setProgress(0.0);
        progressBarPercentage.setPrefWidth(275);

        // create a layoutInput
        GridPane layoutInput = new GridPane();
        GridPane layoutOutput = new GridPane();

        VBox VBoxLayout = new VBox();
        layoutOutput.setHgap(10);
        layoutOutput.setVgap(12);
        VBoxLayout.getChildren().addAll(layoutInput, layoutOutput);

        layoutInput.setHgap(10);
        layoutInput.setVgap(12);

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.getChildren().addAll(labelEnterYourName, textFieldUserName, button);

        layoutInput.add(labelEnterYourName,1,0);
        layoutInput.add(textFieldUserName, 2, 0);
        layoutInput.add(button , 2, 2);
        layoutOutput.add(textAreaShowProgress, 1, 1);
        layoutOutput.add(progressBarPercentage, 1, 2);
        Scene scene = new Scene(VBoxLayout, 325, 300);
        mainWindow.setScene(scene);
        mainWindow.show();
    }

    /**
     * Starts application
     * @param args commandline arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * It is called when the user clicked the button
     * It starts http get request from riot
     * @param event
     */
    @Override
    public void handle(ActionEvent event) {
        if(event.getSource() == button){

            // doing heavy work on another thread
            new Thread(() -> {

                RiotConnector riotConnector = new RiotConnector(textAreaShowProgress, textFieldUserName.getText(), progressBarPercentage);
                riotConnector.getData();

                List<Integer> matchIdList = riotConnector.getMatchId();
                List<Integer> damageList = riotConnector.getDamageList();
                List<String> champNameList = riotConnector.getChampNameList();

                MongoDbConnector mongoDbConnector = new MongoDbConnector(matchIdList, damageList, champNameList, textAreaShowProgress);
                mongoDbConnector.sendData();

                MyPDFObject my = new MyPDFObject(matchIdList, damageList, champNameList);

            }).start();

        }
    }
}
