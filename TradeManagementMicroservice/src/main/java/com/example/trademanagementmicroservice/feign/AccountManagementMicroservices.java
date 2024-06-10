package com.example.trademanagementmicroservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.ibatis.annotations.Param;
import com.example.trademanagementmicroservice.entites.returnEntites.*;


@FeignClient(name = "AccountManagementMicroservice", path = "/api/v1/accountManagement")
public interface AccountManagementMicroservices {
    @GetMapping("/test")
    public String Test();

    @GetMapping("/testFeign")
    public String TestFeign();


    @PostMapping ("/Register")
    public AMMResultCode Register(
            @RequestParam("userID")String userID,
            @RequestParam("userName")String userName,
            @RequestParam("userPhone")String userPhone,
            @RequestParam("password")String password,
            @RequestParam("birth")String birth,
            @RequestParam("email")String email
    );

    @GetMapping("/Login")
    public AMMReturnUserID Login(
            @RequestParam("userPhone")String userPhone,
            @RequestParam("password")String password
    );

    @GetMapping("/GetUserInfo")
    public AMMReturnUserInfo GetUserInfo(
            @RequestParam("userID")String userID
    );

    @GetMapping("/ModifyUserInfo")
    public AMMResultCode ModifyUserInfo(
            @RequestParam("userID")String userID,
            @RequestParam("userName")String userName,
            @RequestParam("userPhone")String userPhone,
            @RequestParam("password")String password,
            @RequestParam("birth")String birth,
            @RequestParam("email")String email
    );

    @GetMapping("/getUserEmail")
    public AMMReturnUserEmail getUserEmail(
            @RequestParam("userID")String userID
    );

    @GetMapping("/bindBankCard")
    public AMMResultCode bindBankCard(
            @RequestParam("userID")String userID,
            @RequestParam("cardID")String cardID,
            @RequestParam("SPK")String SPK
    );

    @GetMapping("/dismissBankCard")
    public AMMResultCode dismissBankCard(
            @RequestParam("userID")String userID,
            @RequestParam("cardID")String cardID
    );

    @GetMapping("/getBankCardList")
    public AMMReturnBankCardList getBankCardList(
            @RequestParam("userID")String userID
    );

    @GetMapping("/modifyPortion")
    public AMMResultCode modifyPortion(
            @RequestParam("cardID")String cardID,
            @RequestParam("fundID")String fundID,
            @RequestParam("amount")double amount
    );

    @GetMapping("/getEmailFromCardID")
    public AMMReturnUserEmail getEmailFromCardID(
            @RequestParam("cardID")String cardID
    );

    @GetMapping("/getAllPortion")
    public AMMReturnFundAccount getAllPortion(
            @RequestParam("cardID")String cardID
    );

}
