package com.example.informationmaintenancemicroservice.entites.feignReturnValue;

public class FundAccount {
    public FundAccount(String cardID, String fundID, double amount) {
        this.cardID = cardID;
        this.fundID = fundID;
        this.amount = amount;
    }
    public FundAccount() {
        this.cardID = null;
        this.fundID = null;
        this.amount = 0;
    }

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public String getFundID() {
        return fundID;
    }

    public void setFundID(String fundID) {
        this.fundID = fundID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    String cardID;
    String fundID;
    double amount;
}
