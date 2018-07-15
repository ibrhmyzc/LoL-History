package sample;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoDbConnector {
    private List<Integer> matchIdList = null;
    private List<Integer> damageList = null;
    private List<String> champNameList = null;
    private List<Document> dataList = null;
    private MongoClient mongoClient = null;
    private MongoDatabase mdb = null;

    public MongoDbConnector(List<Integer> matchIdList, List<Integer> damageList, List<String> champNameList) {
        this.matchIdList = matchIdList;
        this.damageList = damageList;
        this.champNameList = champNameList;
        dataList = new ArrayList<Document>();

        for(int i = 0; i < matchIdList.size(); ++i){
            dataList.add(new Document("MatchId", matchIdList.get(i))
                    .append("Champion", champNameList.get(i))
                    .append("Damage Dealt", damageList.get(i)));
        }
    }

    public void sendData(){
        connectToMongoDb();
    }

    private void connectToMongoDb(){
        mongoClient = new MongoClient("localhost", 27017);
        mdb = mongoClient.getDatabase("demoDb");
        MongoCollection<Document> dataMDoc= mdb.getCollection("demoDocument");
        dataMDoc.insertMany(dataList);
    }
}
