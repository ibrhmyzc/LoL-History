package sample;


import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;

public class RiotConnector {
    private String API_KEY = "";
    List<String> matchHistory = null;
    TextArea showProgress = null;
    private String username = null;
    private int userId;

    public RiotConnector(TextArea showProgress, String username){
        showProgress.setText(showProgress.getText() + "*RiotConnector constructor is called\n");
        this.showProgress = showProgress;
        this.username = username;
        showProgress.setText(showProgress.getText() + "*Username=" + this.username + "\n");


        matchHistory = new ArrayList<String>();
        userId = getUserIdByName();
        showProgress.setText(showProgress.getText() + "*UserId=" + this.userId + "\n");
    }

    private int getUserIdByName(){
        return 0;
    }


}
