package com.example.trademanagementmicroservice.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.example.trademanagementmicroservice.entites.ResultCode;
import com.example.trademanagementmicroservice.entites.ReturnInfo;
import com.example.trademanagementmicroservice.entites.TradeOrder;
import com.example.trademanagementmicroservice.feign.InformationMaintenanceMicroservice;
import com.example.trademanagementmicroservice.services.Trade;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/TradeManagement")
public class controller {

    @Autowired
    Trade trade;
    @Autowired
    InformationMaintenanceMicroservice informationMaintenanceMicroservice;



    @GetMapping("/test")
    public ReturnInfo test() {

        return informationMaintenanceMicroservice.getFundInfo("168601.SZ");
    }

    @GetMapping("/test2")
    public String testConnectivity() {
        try (Entry entry = SphU.entry("common")) {

            return "测试成功！";
        }catch (Exception e){
            return "触发限流";
        }
    }

    }

