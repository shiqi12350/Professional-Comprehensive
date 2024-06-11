package com.example.accountmanagementmicroservices.entites.dbEntities;

public class UserInfo {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public UserInfo(String userID, String userName, String userPhone, String passWord, String birth, String email, int level) {
        this.userID = userID;
        this.userName = userName;
        this.userPhone = userPhone;
        this.password = passWord;
        this.birth = birth;
        this.email = email;
        this.level = level;
    }

    public UserInfo() {
        this.userID = null;
        this.userName = null;
        this.userPhone = null;
        this.password = null;
        this.birth = null;
        this.email = null;
        this.level = 1;
    }

    String userID;
    String userName;
    String userPhone;
    String password;
    String birth;
    String email;
    int level;//1等为最低权限，2等可以查看基金信息，3等可以进行基金交易
}
