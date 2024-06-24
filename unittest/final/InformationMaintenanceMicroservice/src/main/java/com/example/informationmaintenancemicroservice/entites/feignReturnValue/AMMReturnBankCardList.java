package com.example.informationmaintenancemicroservice.entites.feignReturnValue;

import java.util.List;

public class AMMReturnBankCardList {
    public AMMReturnBankCardList(int resultCode, List<String> bankCardList) {
        this.resultCode = resultCode;
        this.bankCardList = bankCardList;
    }

    public AMMReturnBankCardList() {
        this.resultCode = 1;
        this.bankCardList = null;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public List<String> getBankCardList() {
        return bankCardList;
    }

    public void setBankCardList(List<String> bankCardList) {
        this.bankCardList = bankCardList;
    }

    int resultCode;
    List<String>bankCardList;
}
