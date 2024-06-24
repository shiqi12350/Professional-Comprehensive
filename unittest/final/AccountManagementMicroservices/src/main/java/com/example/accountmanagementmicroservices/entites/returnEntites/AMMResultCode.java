package com.example.accountmanagementmicroservices.entites.returnEntites;

public class AMMResultCode {
    int resultCode;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public AMMResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
    public AMMResultCode() {
        this.resultCode = 1;
    }

}
