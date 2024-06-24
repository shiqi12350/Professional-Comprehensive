package com.example.informationmaintenancemicroservice.feign;

import com.example.informationmaintenancemicroservice.entites.feignReturnValue.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "AccountManagementMicroservice", path = "/api/v1/accountManagement")
public interface AccountManagementFeignClient {

    @GetMapping("/test")
    String test();

    @GetMapping("/testFeign")
    String testFeign();

    @PostMapping("/Register")
    AMMResultCode register(
            @RequestParam("userID") String userID,
            @RequestParam("userName") String userName,
            @RequestParam("userPhone") String userPhone,
            @RequestParam("password") String password,
            @RequestParam("birth") String birth,
            @RequestParam("email") String email
    );

    @GetMapping("/Login")
    AMMReturnUserID login(
            @RequestParam("userPhone") String userPhone,
            @RequestParam("password") String password
    );

    @GetMapping("/GetUserInfo")
    AMMReturnUserInfo getUserInfo(
            @RequestParam("userID") String userID
    );

    @GetMapping("/ModifyUserInfo")
    AMMResultCode modifyUserInfo(
            @RequestParam("userID") String userID,
            @RequestParam("userName") String userName,
            @RequestParam("userPhone") String userPhone,
            @RequestParam("password") String password,
            @RequestParam("birth") String birth,
            @RequestParam("email") String email
    );

    @GetMapping("/getUserEmail")
    AMMReturnUserEmail getUserEmail(
            @RequestParam("userID") String userID
    );

    @GetMapping("/bindBankCard")
    AMMResultCode bindBankCard(
            @RequestParam("userID") String userID,
            @RequestParam("cardID") String cardID,
            @RequestParam("SPK") String SPK
    );

    @GetMapping("/dismissBankCard")
    AMMResultCode dismissBankCard(
            @RequestParam("userID") String userID,
            @RequestParam("cardID") String cardID
    );

    @GetMapping("/getBankCardList")
    AMMReturnBankCardList getBankCardList(
            @RequestParam("userID") String userID
    );

    @GetMapping("/modifyPortion")
    AMMResultCode modifyPortion(
            @RequestParam("cardID") String cardID,
            @RequestParam("fundID") String fundID,
            @RequestParam("amount") double amount
    );

    @GetMapping("/getEmailFromCardID")
    AMMReturnUserEmail getEmailFromCardID(
            @RequestParam("cardID") String cardID
    );

    @GetMapping("/getAllPortion")
    AMMReturnFundAccount getAllPortion(
            @RequestParam("cardID") String cardID
    );
}