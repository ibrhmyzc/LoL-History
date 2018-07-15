package sample;


import javafx.scene.control.TextArea;
import org.json.*;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RiotConnector {
    private String API_KEY = "?api_key=RGAPI-7fea4d2c-0db2-4b70-b72d-86da11057b2c";
    private List<Integer> matchIdList = null;
    private List<Integer> damageList = null;
    private List<String> champNameList = null;
    private TextArea showProgress = null;
    private String username = null;
    private int userId;

    public RiotConnector(TextArea showProgress, String username){
        this.showProgress = showProgress;
        this.username = username;
        matchIdList = new ArrayList<Integer>();
        damageList = new ArrayList<Integer>();
        champNameList = new ArrayList<String>();
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
        String request_url =
                "https://tr1.api.riotgames.com/lol/match/v3/matchlists/by-account/" + accountId + API_KEY;
        try{
            URL url = new URL(request_url);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String s_name = br.readLine();
            //String s_name = "{" + "\"" + username + "\":" + br.readLine() + "}";
            JSONObject jAns = new JSONObject(s_name);

            JSONArray jArr = jAns.getJSONArray("matches");
            for(int i = 0; i < jArr.length() && i < 20; ++i){
                matchIdList.add(jArr.getJSONObject(i).getInt("gameId"));
            }
            getStatisticsByGameId(matchIdList);
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

                int damage = getDamageByParticipantId(jAns.getJSONArray("participants"), participantId);
                damageList.add(damage);

                int championId = getChampionIdByParticipantId(jAns.getJSONArray("participants"), participantId);
                String championName = getChampionNameByChampionId(championId);
                champNameList.add(championName);

                showProgress.setText(showProgress.getText() + gameIds.get(i) + " " + championName + " " + damageList + "\n");
                Thread.sleep(250);
            } catch(Exception ex){
                showProgress.setText(showProgress.getText() + "*Http get request error - getStatisticsByGameId\n");
            }
        }
    }

    private int getSummonerIdInMatch(JSONArray jArrTmp){
        int participantId = -1;
        for(int i = 0; i < jArrTmp.length(); ++i){
            JSONObject obj = jArrTmp.getJSONObject(i);
            JSONObject details = obj.getJSONObject("player");
            String s_name = details.getString("summonerName");

            if(s_name.equals(username)){
                participantId = obj.getInt("participantId");
                break;
            }
        }
        return participantId;
    }

    private int getDamageByParticipantId(JSONArray jArrTmp, int participantId){
        int damage = -1;
        for(int i = 0; i < jArrTmp.length(); ++i){
            JSONObject obj = jArrTmp.getJSONObject(i);
            JSONObject details = obj.getJSONObject("stats");

            if(participantId == details.getInt("participantId")){
                damage = details.getInt("totalDamageDealtToChampions");
                break;
            }
        }
        return damage;
    }

    private int getChampionIdByParticipantId(JSONArray jArrTmp, int participantId){
        int championId = -1;
        for(int i = 0; i < jArrTmp.length(); ++i){
            JSONObject obj = jArrTmp.getJSONObject(i);
            JSONObject details = obj.getJSONObject("stats");

            if(participantId == details.getInt("participantId")){
                championId = obj.getInt("championId");
                break;
            }
        }
        return championId;
    }

    private String getChampionNameByChampionId(int championId) {
        switch (championId){
            case 266: return "Aatrox";
            case 412: return "Thresh";
            case 23: return "Tryndamere";
            case 79: return "Gragas";
            case 69: return "Cassiopeia";
            case 136: return "Aurelion Sol";
            case 13: return "Ryze";
            case 78: return "Poppy";
            case 14: return "Sion";
            case 1: return "Annie";
            case 202: return "Jhin";
            case 43: return "Karma";
            case 111: return "Nautilus";
            case 240: return "Kled";
            case 99: return "Lux";
            case 103: return "Ahri";
            case 2: return "Olaf";
            case 112: return "Viktor";
            case 34: return "Anivia";
            case 27: return "Singed";
            case 86: return "Garen";
            case 127: return "Lissandra";
            case 57: return "Maokai";
            case 25: return "Morgana";
            case 28: return "Evelynn";
            case 105: return "Fizz";
            case 74: return "Heimerdinger";
            case 238: return "Zed";
            case 68: return "Rumble";
            case 82: return "Mordekaiser";
            case 37: return "Sona";
            case 96: return "Kog'Maw";
            case 55: return "Katarina";
            case 117: return "Lulu";
            case 22: return "Ashe";
            case 30: return "Karthus";
            case 12: return "Alistar";
            case 122: return "Darius";
            case 67: return "Vayne";
            case 110: return "Varus";
            case 77: return "Udyr";
            case 89: return "Leona";
            case 126: return "Jayce";
            case 134: return "Syndra";
            case 80: return "Pantheon";
            case 92: return "Riven";
            case 121: return "Kha'Zix";
            case 42: return "Corki";
            case 268: return "Azir";
            case 51: return "Caitlyn";
            case 76: return "Nidalee";
            case 85: return "Kennen";
            case 3: return "Galio";
            case 45: return "Veigar";
            case 432: return "Bard";
            case 150: return "Gnar";
            case 90: return "Malzahar";
            case 104: return "Graves";
            case 254: return "Vi";
            case 10: return "Kayle";
            case 39: return "Irelia";
            case 64: return "Lee Sin";
            case 420: return "Illaoi";
            case 60: return "Elise";
            case 106: return "Volibear";
            case 20: return "Nunu";
            case 4: return "Twisted Fate";
            case 24: return "Jax";
            case 102: return "Shyvana";
            case 429: return "Kalista";
            case 36: return "Dr. Mundo";
            case 427: return "Ivern";
            case 131: return "Diana";
            case 223: return "Tahm Kench";
            case 63: return "Brand";
            case 113: return "Sejuani";
            case 8: return "Vladimir";
            case 154: return "Zac";
            case 421: return "Rek'Sai";
            case 133: return "Quinn";
            case 84: return "Akali";
            case 163: return "Taliyah";
            case 18: return "Tristana";
            case 120: return "Hecarim";
            case 15: return "Sivir";
            case 236: return "Lucian";
            case 107: return "Rengar";
            case 19: return "Warwick";
            case 72: return "Skarner";
            case 54: return "Malphite";
            case 157: return "Yasuo";
            case 101: return "Xerath";
            case 17: return "Teemo";
            case 75: return "Nasus";
            case 58: return "Renekton";
            case 119: return "Draven";
            case 35: return "Shaco";
            case 50: return "Swain";
            case 91: return "Talon";
            case 40: return "Janna";
            case 115: return "Ziggs";
            case 245: return "Ekko";
            case 61: return "Orianna";
            case 114: return "Fiora";
            case 9: return "Fiddlesticks";
            case 31: return "Cho'Gath";
            case 33: return "Rammus";
            case 7: return "LeBlanc";
            case 16: return "Soraka";
            case 26: return "Zilean";
            case 56: return "Nocturne";
            case 222: return "Jinx";
            case 83: return "Yorick";
            case 6: return "Urgot";
            case 203: return "Kindred";
            case 21: return "Miss Fortune";
            case 62: return "Wukong";
            case 53: return "Blitzcrank";
            case 98: return "Shen";
            case 201: return "Braum";
            case 5: return "Xin Zhao";
            case 29: return "Twitch";
            case 11: return "Master Yi";
            case 44: return "Taric";
            case 32: return "Amumu";
            case 41: return "Gangplank";
            case 48: return "Trundle";
            case 38: return "Kassadin";
            case 161: return "Vel'Koz";
            case 143: return "Zyra";
            case 267: return "Nami";
            case 59: return "Jarvan IV";
            case 81: return "Ezreal";
            default: return "Unknown";
        }
    }

    public List<Integer> getMatchId(){
        return matchIdList;
    }

    public List<Integer> getDamageList(){
        return damageList;
    }

    public List<String> getChampNameList(){
        return champNameList;
    }
}
