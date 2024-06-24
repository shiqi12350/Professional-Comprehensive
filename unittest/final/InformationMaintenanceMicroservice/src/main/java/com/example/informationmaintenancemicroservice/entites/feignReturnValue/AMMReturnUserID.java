package com.example.informationmaintenancemicroservice.entites.feignReturnValue;

public class AMMReturnUserID {
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public AMMReturnUserID(String userID, int resultCode) {
        this.userID = userID;
        this.resultCode = resultCode;
    }

    public AMMReturnUserID() {
        this.userID = null;
        this.resultCode = 1;
    }

    String userID;
    int resultCode;
}
