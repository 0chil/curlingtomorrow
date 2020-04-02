package com.gor2.curlingtomorrow.dataclass;

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

