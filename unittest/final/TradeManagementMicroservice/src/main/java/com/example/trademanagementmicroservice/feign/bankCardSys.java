package com.example.trademanagementmicroservice.feign;

import com.example.trademanagementmicroservice.entites.ResultCode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.trademanagementmicroservice.entites.CreateResult;
import com.example.trademanagementmicroservice.entites.ValueResult;
import com.example.trademanagementmicroservice.entites.LogResult;
import com.example.trademanagementmicroservice.entites.NameResult;
import com.example.trademanagementmicroservice.entites.SPKResult;

@FeignClient(name = "bankCardsys", path = "/api/v1/bankcard")
public interface bankCardSys {
    @GetMapping("/Test")
    String testConnectivity();

    @PostMapping("/Register")
    CreateResult Register(
            @RequestParam("userName")String userName,
            @RequestParam("userID")String userID,
            @RequestParam("passWord")String passWord,
            @RequestParam("loginPassWord")String loginPassWord,
            @RequestParam("birth")String birth
    );

    @GetMapping("/Login")
    ResultCode Login(
            @RequestParam("cardID")String cardID,
            @RequestParam("loginPassWord")String loginPassWord
    );

    @GetMapping("/CheckValue")
    ValueResult CheckValue(
            @RequestParam("cardID")String cardID,
            @RequestParam("SPK")String SPK);

    @GetMapping("/ChangeValue")
    ResultCode ChangeValue(
            @RequestParam("cardID")String cardID,
            @RequestParam("passWord")String passWord,
            @RequestParam("value")Double value,
            @RequestParam("SPK")String SPK);

    @GetMapping("/GetLog")
    LogResult GetLog(
            @RequestParam("cardID")String cardID,
            @RequestParam("SPK")String SPK
            );

    @GetMapping("/GetUserName")
    NameResult GetUserName(
            @RequestParam("cardID")String cardID,
            @RequestParam("SPK")String SPK
    );

    @GetMapping("/CreateSPK")
    SPKResult CreateSPK(
            @RequestParam("cardID")String cardID,
            @RequestParam("passWord")String passWord
    );

    @GetMapping("/GetSPK")
    SPKResult GetSPK(
            @RequestParam("cardID")String cardID,
            @RequestParam("passWord")String passWord
    );

    @GetMapping("/GetSPK2")
    SPKResult GetSPK2(
            @RequestParam("cardID")String cardID
    );
    @GetMapping("/DelSPK")
    ResultCode DelSPK(
            @RequestParam("cardID")String cardID,
            @RequestParam("passWord")String passWord
    );

    //提供从指定账户扣款接口
    @GetMapping("/DeductPayment")
    ResultCode DeductPayment(
            @RequestParam("cardID")String cardID,
            @RequestParam("passWord")String passWord,
            @RequestParam("SPK")String SPK,
            @RequestParam("amount")double amount
    );

    //提供给指定账户汇款接口
    @GetMapping("/TransferFunds")
    ResultCode TransferFunds(
            @RequestParam("cardID")String cardID,
            @RequestParam("SPK")String SPK,
            @RequestParam("amount")double amount
    );

    //检测对应的身份证号接口
    @GetMapping("/CheckUserID")
    ResultCode  CheckUserID(
            @RequestParam("cardID")String cardID,
            @RequestParam("SPK")String SPK,
            @RequestParam("userID")String userID
    );
    @GetMapping("/checkUser")//查看是否有这个人的银行卡
    ResultCode  checkUser(
            @RequestParam("userID")String userID
    );
}
