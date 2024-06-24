package com.example.trademanagementmicroservice.entites;

public class SPKResult {

    public SPKResult(String SPK, int resultCode) {
        this.SPK = SPK;
        this.resultCode = resultCode;
    }


    public SPKResult() {
        this.SPK = null;
        this.resultCode = 1;
    }

    public String getSPK() {
        return SPK;
    }

    public void setSPK(String SPK) {
        this.SPK = SPK;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    String SPK;//特殊交易权限码，可用于外部应用查询转账
    int resultCode;
}
