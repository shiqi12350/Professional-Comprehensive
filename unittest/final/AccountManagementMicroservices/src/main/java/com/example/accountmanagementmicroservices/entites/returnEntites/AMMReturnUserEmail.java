package com.example.accountmanagementmicroservices.entites.returnEntites;

public class AMMReturnUserEmail {
    public AMMReturnUserEmail(int resultCode, String email) {
        this.resultCode = resultCode;
        this.email = email;
    }

    public AMMReturnUserEmail() {
        this.resultCode = 1;
        this.email = null;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    int resultCode;
    String email;
}
