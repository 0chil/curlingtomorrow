package com.gor2.curlingtomorrow.result;

public class Result {
    private String dateTime,playerRedName,playerYellowName,imagePath;
    int playerRedScore,playerYellowScore;

    public Result(String dateTime,String playerRedName,String playerYellowName,int playerRedScore,int playerYellowScore, String imagePath){
        this.dateTime= dateTime;
        this.playerRedName = playerRedName;
        this.playerYellowName = playerYellowName;
        this.playerRedScore = playerRedScore;
        this.playerYellowScore = playerYellowScore;
        this.imagePath = imagePath;
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

    public String getImagePath() { return imagePath; }

    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setPlayerRedName(String playerRedName) {
        playerRedName = playerRedName;
    }

    public void setPlayerRedScore(String playerRedScore) {
        playerRedScore = playerRedScore;
    }

    public void setPlayerYellowName(String playerYellowName) { playerYellowName = playerYellowName; }

    public void setPlayerYellowScore(String playerYellowScore) { playerYellowScore = playerYellowScore; }
}

