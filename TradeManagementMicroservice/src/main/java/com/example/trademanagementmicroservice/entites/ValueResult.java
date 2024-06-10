package com.example.trademanagementmicroservice.entites;

public class ValueResult {
    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public ValueResult(int resultCode, double value) {
        this.resultCode = resultCode;
        this.value = value;
    }

    public ValueResult() {
        this.resultCode = 1;
        this.value = 0.0;
    }

    int resultCode;
    double value;
}
