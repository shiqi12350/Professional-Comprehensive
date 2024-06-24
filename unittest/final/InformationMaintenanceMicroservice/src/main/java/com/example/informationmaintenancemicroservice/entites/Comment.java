package com.example.informationmaintenancemicroservice.entites;

public class Comment {
    public String getFundID() {
        return fundID;
    }

    public void setFundID(String fundID) {
        this.fundID = fundID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Comment(String fundID, String content, String timeStamp) {
        this.fundID = fundID;
        this.content = content;
        this.timeStamp = timeStamp;
    }

    public Comment() {
        this.fundID = null;
        this.content = null;
        this.timeStamp = null;
    }

    String fundID;
    String content;
    String timeStamp;

}
