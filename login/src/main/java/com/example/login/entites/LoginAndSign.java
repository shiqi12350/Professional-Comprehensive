package com.example.login.entites;

public class LoginAndSign {
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public LoginAndSign(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }
    public LoginAndSign() {
        this.userName = null;
        this.passWord = null;
    }

    String userName;
    String passWord;
}
