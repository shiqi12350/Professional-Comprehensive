package com.example.accountmanagementmicroservices.controller;

import com.example.accountmanagementmicroservices.entites.returnEntites.*;
import com.example.accountmanagementmicroservices.services.UserServices;
import com.example.accountmanagementmicroservices.userFeign.MessageNotificationFeignServices;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accountManagement")
public class Controller {

    @Autowired
    UserServices userServices;
    @Autowired
    MessageNotificationFeignServices messageNotificationFeignServices;
    @GetMapping("/test")
    public String Test(){
        return "账户管理微服务已经成功运行！";
    }
    @GetMapping("/testFeign")
    public String TestFeign(){
        return messageNotificationFeignServices.testConnectivity();
    }

    @PostMapping ("/Register")
    public AMMResultCode Register(
            @Param("userID")String userID,
            @Param("userName")String userName,
            @Param("userPhone")String userPhone,
            @Param("password")String password,
            @Param("birth")String birth,
            @Param("email")String email
    ){
        return userServices.Register(userID,userName,userPhone,password,birth,email);
    }

    @GetMapping("/Login")
    public AMMReturnUserID Login(
            @Param("userPhone")String userPhone,
            @Param("password")String password
    ){
        return userServices.Login(userPhone,password);
    }

    @GetMapping("/GetUserInfo")
    public AMMReturnUserInfo GetUserInfo(
            @Param("userID")String userID
    ){
        return userServices.GetUserInfo(userID);
    }

    @GetMapping("/ModifyUserInfo")
    public AMMResultCode ModifyUserInfo(
            @Param("userID")String userID,
            @Param("userName")String userName,
            @Param("userPhone")String userPhone,
            @Param("password")String password,
            @Param("birth")String birth,
            @Param("email")String email
    ){
        return userServices.ModifyUserInfo(userID,userName,userPhone,password,birth,email);
    }

    @GetMapping("/getUserEmail")
    public AMMReturnUserEmail getUserEmail(
            @Param("userID")String userID
    ){
        return userServices.getUserEmail(userID);
    }

    @GetMapping("/bindBankCard")
    public AMMResultCode bindBankCard(
            @Param("userID")String userID,
            @Param("cardID")String cardID,
            @Param("SPK")String SPK
    ){
        return userServices.bindBankCard(userID,cardID,SPK);
    }

    @GetMapping("/dismissBankCard")
    public AMMResultCode dismissBankCard(
            @Param("userID")String userID,
            @Param("cardID")String cardID
    ){
        return userServices.dismissBankCard(userID,cardID);
    }

    @GetMapping("/getBankCardList")
    public AMMReturnBankCardList getBankCardList(
            @Param("userID")String userID
    ){
        return userServices.getBankCardList(userID);
    }

    @GetMapping("/modifyPortion")
    public AMMResultCode modifyPortion(
            @Param("cardID")String cardID,
            @Param("fundID")String fundID,
            @Param("amount")double amount
    ){
        return userServices.modifyPortion(cardID,fundID,amount);
    }
    @GetMapping("/getEmailFromCardID")
    public AMMReturnUserEmail getEmailFromCardID(
            @Param("cardID")String cardID
    ){
        return userServices.getUserEmailFromCardID(cardID);
    }

    @GetMapping("/getAllPortion")
    public AMMReturnFundAccount getAllPortion(
            @Param("cardID")String cardID
    ){
        return userServices.getAllPortion(cardID);
    }

}
