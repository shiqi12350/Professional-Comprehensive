package com.example.informationmaintenancemicroservice.services;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.informationmaintenancemicroservice.entites.ResultCode;
import com.example.informationmaintenancemicroservice.entites.ReturnInfo;
import com.example.informationmaintenancemicroservice.entites.ReturnRateValue;
import com.example.informationmaintenancemicroservice.entites.ReturnValue;
import com.example.informationmaintenancemicroservice.entites.SubscriptionFee;
import com.example.informationmaintenancemicroservice.entites.feignReturnValue.AMMResultCode;
import com.example.informationmaintenancemicroservice.entites.feignReturnValue.AMMReturnContent;
import com.example.informationmaintenancemicroservice.feign.MessageNotificationMicroservice;
import com.example.informationmaintenancemicroservice.mapper.InfoMapper;

@Service
public class BasicServices {


    @Autowired
    MessageNotificationMicroservice messageNotificationMicroservice;

    @Autowired
    InfoMapper infoMapper;



    public ReturnValue getManageFee(String fundCode) {
        ReturnValue rc = new ReturnValue();

        try {
            //判断此时基金代码是否存在
            List<Double> check1 = infoMapper.getManageFee(fundCode);
            if (check1.size() == 0) {
                rc.setResultCode(-1);
                return rc;
            }
            rc.setValue(check1.get(0));
            rc.setValueType(0);
            rc.setResultCode(1);
            return rc;
        } catch (Exception e) {
            rc.setResultCode(-2);
            return rc;
        }
    }

    public ReturnValue getCustodyFee(String fundCode) {
        ReturnValue rc = new ReturnValue();

        try {
            //判断此时基金代码是否存在
            List<Double> check1 = infoMapper.getCustodyFee(fundCode);
            if (check1.size() == 0) {
                rc.setResultCode(-1);
                return rc;
            }
            rc.setValue(check1.get(0));
            rc.setValueType(0);
            rc.setResultCode(1);
            return rc;
        } catch (Exception e) {
            rc.setResultCode(-2);
            return rc;
        }
    }


    public ReturnInfo getFundInfo(String fundCode) {
        ReturnInfo rc = new ReturnInfo();

        try {
            //判断此时基金代码是否存在
            List<String> check1 = infoMapper.getFundInfo(fundCode);
            if (check1.size() == 0) {
                rc.setResultCode(-1);
                return rc;
            }
            rc.setInfo(check1.get(0));
            rc.setResultCode(1);
            return rc;
        } catch (Exception e) {
            rc.setResultCode(-2);
            return rc;
        }
    }

}