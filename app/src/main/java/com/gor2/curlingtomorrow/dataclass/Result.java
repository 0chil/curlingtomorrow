package com.gor2.curlingtomorrow.dataclass;

import org.json.JSONArray;
import org.json.JSONException;

public class Result {
    private String dateTime,playerRedName,playerYellowName,imageFileName;
    int playerRedScore,playerYellowScore;

    public Result(String dateTime,String playerRedName,String playerYellowName,int playerRedScore,int playerYellowScore, String imageFileName){
        this.dateTime= dateTime;
        this.playerRedName = playerRedName;
        this.playerYellowName = playerYellowName;
        this.playerRedScore = playerRedScore;
        this.playerYellowScore = playerYellowScore;
        this.imageFileName = imageFileName;
    }

    public JSONArray toJSONArray(){
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(dateTime);
        jsonArray.put(playerRedName);
        jsonArray.put(playerYellowName);
        jsonArray.put(playerRedScore);
        jsonArray.put(playerYellowScore);
        jsonArray.put(imageFileName);
        return jsonArray;
    }

    public static Result parseJSONArray(JSONArray jsonArray) throws JSONException {
        return new Result(
                jsonArray.getString(0),
                jsonArray.getString(1),
                jsonArray.getString(2),
                jsonArray.getInt(3),
                jsonArray.getInt(4),
                jsonArray.getString(5)
        );
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getPlayerRedName() {
        return playerRedName;
    }

    public int getPlayerRedScore() { return playerRedScore; }

    public String getPlayerYellowName() {
        return playerYellowName;
    }

    public int getPlayerYellowScore() {
        return playerYellowScore;
    }

    public String getImageFileName() { return imageFileName; }

    public void setImageFileName(String imagePath) { this.imageFileName = imagePath; }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setPlayerRedName(String playerRedName) {
        this.playerRedName = playerRedName;
    }

    public void setPlayerRedScore(int playerRedScore) {
        this.playerRedScore = playerRedScore;
    }

    public void setPlayerYellowName(String playerYellowName) { this.playerYellowName = playerYellowName; }

    public void setPlayerYellowScore(int playerYellowScore) { this.playerYellowScore = playerYellowScore; }
}

