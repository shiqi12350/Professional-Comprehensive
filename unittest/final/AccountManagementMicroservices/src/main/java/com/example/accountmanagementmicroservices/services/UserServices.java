package com.example.accountmanagementmicroservices.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import static java.lang.Math.abs;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.accountmanagementmicroservices.entites.dbEntities.AccountInfo;
import com.example.accountmanagementmicroservices.entites.dbEntities.FundAccount;
import com.example.accountmanagementmicroservices.entites.dbEntities.UserInfo;
import com.example.accountmanagementmicroservices.entites.returnEntites.AMMResultCode;
import com.example.accountmanagementmicroservices.entites.returnEntites.AMMReturnBankCardList;
import com.example.accountmanagementmicroservices.entites.returnEntites.AMMReturnFundAccount;
import com.example.accountmanagementmicroservices.entites.returnEntites.AMMReturnUserEmail;
import com.example.accountmanagementmicroservices.entites.returnEntites.AMMReturnUserID;
import com.example.accountmanagementmicroservices.entites.returnEntites.AMMReturnUserInfo;
import com.example.accountmanagementmicroservices.mapper.UserAccountMapper;
import com.example.accountmanagementmicroservices.userFeign.MessageNotificationFeignServices;

@Service
public class UserServices {
    @Autowired
    UserAccountMapper userAccountMapper;
    @Autowired
    MessageNotificationFeignServices messageNotificationFeignServices;

    public AMMReturnUserID Login(
            String userPhone,
            String password
    ){
        AMMReturnUserID rc = new AMMReturnUserID();
        List<UserInfo>l1 = userAccountMapper.getUserInfoFromUserPhoneAndPassword(userPhone,password);
        if(l1.size()!=1){
            rc.setResultCode(-101);
        }else{
            rc.setUserID(l1.get(0).getUserID());
        }
        return rc;
    }

    public AMMReturnUserInfo GetUserInfo(
            String userID
    ){
        AMMReturnUserInfo rc = new AMMReturnUserInfo();
        List<UserInfo>l1 = userAccountMapper.getUserInfoFromUserID(userID);
        if(l1.size()!=1){
            rc.setResultCode(-101);
            return rc;
        }
        rc.setBirth(l1.get(0).getBirth());
        rc.setUserName(l1.get(0).getUserName());
        rc.setUserPhone(l1.get(0).getUserPhone());
        rc.setEmail(l1.get(0).getEmail());
        rc.setUserID(l1.get(0).getUserID());
        rc.setPassWord(l1.get(0).getPassword());
        rc.setLevel(l1.get(0).getLevel());
        return rc;
    }


    public AMMReturnUserEmail getUserEmail(
            String userID
    ){
        AMMReturnUserEmail ammReturnUserEmail = new AMMReturnUserEmail();

        List<UserInfo>l1 = userAccountMapper.getUserInfoFromUserID(userID);
        if(l1.size()!=1){
            ammReturnUserEmail.setResultCode(-101);
            return ammReturnUserEmail;
        }
        ammReturnUserEmail.setEmail(l1.get(0).getEmail());

        return ammReturnUserEmail;
    }

    public AMMReturnUserEmail getUserEmailFromCardID(
            String cardID
    ){
        AMMReturnUserEmail ammReturnUserEmail = new AMMReturnUserEmail();

        List<AccountInfo>l1 = userAccountMapper.getAccountInfoFromCardID(cardID);
        if(l1.size()!=1){
            ammReturnUserEmail.setResultCode(-104);
            return ammReturnUserEmail;
        }
        AMMReturnUserEmail ammReturnUserEmail1 = getUserEmail(l1.get(0).getUserID());

        return ammReturnUserEmail1;
    }


    public AMMReturnBankCardList getBankCardList(
            String userID
    ){
        AMMReturnBankCardList ammReturnBankCardList = new AMMReturnBankCardList();
        //首先需要验证userID的合法性
        List<UserInfo>l1 = userAccountMapper.getUserInfoFromUserID(userID);
        if(l1.size()!=1){
            ammReturnBankCardList.setResultCode(-101);
            return ammReturnBankCardList;
        }
        //接着获取其对应的银行卡号列表
        List<AccountInfo>l2 = userAccountMapper.getAccountInfoFromUserID(userID);
        List<String> lC = new ArrayList<>();
        for (AccountInfo accountInfo : l2) {
            lC.add(accountInfo.getCardID());
        }
        ammReturnBankCardList.setBankCardList(lC);

        return ammReturnBankCardList;
    }

}
