package com.example.accountmanagementmicroservices.entites.dbEntities;

public class AccountInfo {
    public AccountInfo(String cardID, String userID) {
        this.cardID = cardID;
        this.userID = userID;
    }
    public AccountInfo() {
        this.cardID = null;
        this.userID = null;
    }

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    String cardID;
    String userID;
}
