package com.example.accountmanagementmicroservices.entites.returnEntites;

public class AMMReturnUserInfo {
    public AMMReturnUserInfo(int level, int resultCode, String userID, String userName, String userPhone, String passWord, String birth, String email) {
        this.resultCode = resultCode;
        this.userID = userID;
        this.userName = userName;
        this.userPhone = userPhone;
        this.passWord = passWord;
        this.birth = birth;
        this.email = email;
        this.level = level;
    }

    public AMMReturnUserInfo() {
        this.resultCode = 1;
        this.level = 2;
        this.userID = null;
        this.userName = null;
        this.userPhone = null;
        this.passWord = null;
        this.birth = null;
        this.email = null;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    int resultCode;
    String userID;
    String userName;
    String userPhone;
    String passWord;
    String birth;
    String email;
    int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
