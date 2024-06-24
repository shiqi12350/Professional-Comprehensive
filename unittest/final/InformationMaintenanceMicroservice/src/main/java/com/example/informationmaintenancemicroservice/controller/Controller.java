package com.example.informationmaintenancemicroservice.controller;

import com.example.informationmaintenancemicroservice.entites.ResultCode;
import com.example.informationmaintenancemicroservice.entites.ReturnInfo;
import com.example.informationmaintenancemicroservice.entites.ReturnRateValue;
import com.example.informationmaintenancemicroservice.entites.ReturnValue;
import com.example.informationmaintenancemicroservice.entites.feignReturnValue.AMMResultCode;
import com.example.informationmaintenancemicroservice.entites.feignReturnValue.AMMReturnContent;
import com.example.informationmaintenancemicroservice.services.BasicServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/informationmaintenance")
public class Controller {

    @Autowired
    BasicServices basicServices;

    @GetMapping("/Test")
    public String testConnectivity() {
        return "信息维护微服务链接性测试成功！";
    }



    @GetMapping("/ObtainManageFee")
    public ReturnValue getManageFee(
            @RequestParam("fundCode")String fundCode
    ){
        return basicServices.getManageFee(fundCode);
    }

    @GetMapping("/ObtainCustodyFee")
    public ReturnValue getCustodyFee(
            @RequestParam("fundCode")String fundCode
    ){
        return basicServices.getCustodyFee(fundCode);
    }


    @GetMapping("/ObtainFundInfo")
    public ReturnInfo getFundInfo(
            @RequestParam("fundCode")String fundCode
    ){
        return basicServices.getFundInfo(fundCode);
    }


}
