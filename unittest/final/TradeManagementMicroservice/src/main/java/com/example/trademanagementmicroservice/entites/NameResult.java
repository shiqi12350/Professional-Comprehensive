package com.example.trademanagementmicroservice.entites;

public class NameResult {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public NameResult(String name, int resultCode) {
        this.name = name;
        this.resultCode = resultCode;
    }

    public NameResult() {
        this.name = null;
        this.resultCode = 1;
    }

    String name;
    int resultCode;
}
