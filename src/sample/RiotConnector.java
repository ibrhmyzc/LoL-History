package sample;


import javafx.scene.control.TextArea;
import org.json.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RiotConnector {
    private String API_KEY = "?api_key=RGAPI-7fea4d2c-0db2-4b70-b72d-86da11057b2c";
    List<String> matchHistory = null;
    TextArea showProgress = null;
    private String username = null;
    private int userId;

    public RiotConnector(TextArea showProgress, String username){
        this.showProgress = showProgress;
        this.username = username;
        matchHistory = new ArrayList<String>();
    }

    public void getData(){
        userId = getUserIdByName();
        getMatchHistoryByAccountId(userId);
        showProgress.setText(showProgress.getText() + "*UserId=" + this.userId + "\n");
    }

    private int getUserIdByName() {
        String request_url =
                "https://tr1.api.riotgames.com/lol/summoner/v3/summoners/by-name/" + username + API_KEY;
        try{
            URL url = new URL(request_url);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String s_name = "{" + "\"" + username + "\":" + br.readLine() + "}";
            JSONObject jAns = new JSONObject(s_name);
            return jAns.getJSONObject(username).getInt("accountId");
        }catch(Exception ex){
            showProgress.setText(showProgress.getText() + "*Http get request error - getUserIdByName\n");
        }
        return -1;
    }

    private void getMatchHistoryByAccountId(int accountId){
        List<Integer> gameIds = new ArrayList<Integer>();
        String request_url =
                "https://tr1.api.riotgames.com/lol/match/v3/matchlists/by-account/" + accountId + API_KEY;
        try{
            URL url = new URL(request_url);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String s_name = br.readLine();
            //String s_name = "{" + "\"" + username + "\":" + br.readLine() + "}";
            JSONObject jAns = new JSONObject(s_name);

            JSONArray jArr = jAns.getJSONArray("matches");
            for(int i = 0; i < jArr.length(); ++i){
                gameIds.add(jArr.getJSONObject(i).getInt("gameId"));
            }
            getStatisticsByGameId(gameIds);
        }catch(Exception ex){
            showProgress.setText(showProgress.getText() + "*Http get request error - getMatchHistoryByAccountId\n");
        }
    }

    private void getStatisticsByGameId(List<Integer> gameIds){
        for(int i = 0; i < gameIds.size(); ++i){
            try{
                String request_url =
                        "https://tr1.api.riotgames.com/lol/match/v3/matches/" + gameIds.get(i) + API_KEY;
                URL url = new URL(request_url);
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                String s_name = br.readLine();
                JSONObject jAns = new JSONObject(s_name);

                int participantId = getSummonerIdInMatch(jAns.getJSONArray("participantIdentities"));
                int championId = getChampionIdInMAtchByParticipantId(jAns, participantId);

                Thread.sleep(1000);
            } catch(Exception ex){
                showProgress.setText(showProgress.getText() + "*Http get request error - getStatisticsByGameId\n");
            }
        }
    }

    private int getSummonerIdInMatch(JSONArray jArrTmp){
        int participantId = 0;
        for(int i = 0; i < jArrTmp.length(); ++i){

            JSONObject obj = jArrTmp.getJSONObject(i);
            participantId = obj.getInt("participantId");

            JSONObject details = obj.getJSONObject("player");
            String s_name = details.getString("summonerName");

            if(s_name.equals(username)){
                System.out.println("Player id for this match is " + participantId);
            }
        }
        return participantId;
    }

    private int getChampionIdInMAtchByParticipantId(JSONObject obj, int participantId){

        return 0;
    }
}
