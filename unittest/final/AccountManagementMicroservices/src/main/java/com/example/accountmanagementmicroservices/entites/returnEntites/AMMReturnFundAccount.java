package com.example.accountmanagementmicroservices.entites.returnEntites;

import com.example.accountmanagementmicroservices.entites.dbEntities.FundAccount;

import java.util.List;

public class AMMReturnFundAccount {
    public AMMReturnFundAccount(int resultCode, List<FundAccount> info) {
        this.resultCode = resultCode;
        this.info = info;
    }

    public AMMReturnFundAccount() {
        this.resultCode = 1;
        this.info = null;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public List<FundAccount> getInfo() {
        return info;
    }

    public void setInfo(List<FundAccount> info) {
        this.info = info;
    }

    int resultCode;
    List<FundAccount> info;
}
