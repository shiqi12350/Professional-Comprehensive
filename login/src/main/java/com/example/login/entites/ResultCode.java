package com.example.login.entites;

public class ResultCode {
    int resultCode;

    public ResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public ResultCode() {
        this.resultCode = 1;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
