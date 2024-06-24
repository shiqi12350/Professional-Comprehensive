package com.example.trademanagementmicroservice.entites;

import java.util.List;

public class LogResult {
    public List<String> getInfo() {
        return info;
    }

    public void setInfo(List<String> info) {
        this.info = info;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public LogResult(List<String> info, int resultCode) {
        this.info = info;
        this.resultCode = resultCode;
    }
    public LogResult() {
        this.info = null;
        this.resultCode = 1;
    }

    List<String> info;
    int resultCode;
}
