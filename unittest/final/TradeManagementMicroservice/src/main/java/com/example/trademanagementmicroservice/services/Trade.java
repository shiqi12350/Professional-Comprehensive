package com.example.trademanagementmicroservice.services;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.example.trademanagementmicroservice.entites.*;
import com.example.trademanagementmicroservice.feign.AccountFeignClient;
import com.example.trademanagementmicroservice.feign.CashManagementMicroservice;
import com.example.trademanagementmicroservice.feign.InformationMaintenanceMicroservice;
import com.example.trademanagementmicroservice.feign.MessageNotificationMicroservice;
import com.example.trademanagementmicroservice.feign.bankCardSys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;



@Service
public class Trade {


    public static String extractFirstSixDigits(String fundID) {
        // Check if the fundID is at least 6 characters long
        if (fundID.length() >= 6) {
            return fundID.substring(0, 6);
        } else{
            // Handle the case where fundID is less than 6 characters long
            throw new IllegalArgumentException("fundID must be at least 6 characters long");
        }
    }


}
