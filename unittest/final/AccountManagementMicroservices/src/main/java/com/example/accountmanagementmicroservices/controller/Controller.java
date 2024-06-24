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



    @GetMapping("/getUserEmail")
    public AMMReturnUserEmail getUserEmail(
            @Param("userID")String userID
    ){
        return userServices.getUserEmail(userID);
    }



    @GetMapping("/getBankCardList")
    public AMMReturnBankCardList getBankCardList(
            @Param("userID")String userID
    ){
        return userServices.getBankCardList(userID);
    }

    @GetMapping("/getEmailFromCardID")
    public AMMReturnUserEmail getEmailFromCardID(
            @Param("cardID")String cardID
    ){
        return userServices.getUserEmailFromCardID(cardID);
    }


}
