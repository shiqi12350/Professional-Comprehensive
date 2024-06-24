package com.example.trademanagementmicroservice.entites;

public class CreateResult {
    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public CreateResult(int resultCode, String cardID) {
        this.resultCode = resultCode;
        this.cardID = cardID;
    }

    public CreateResult() {
        this.resultCode = 1;
        this.cardID = null;
    }


    int resultCode;
    String cardID;

}
