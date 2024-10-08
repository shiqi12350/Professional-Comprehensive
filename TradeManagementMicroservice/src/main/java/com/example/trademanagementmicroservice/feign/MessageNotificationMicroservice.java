package com.example.trademanagementmicroservice.feign;

import com.example.trademanagementmicroservice.entites.ResultCode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
/*
@FeignClient(name = "MessageNotificationMicroservice", path = "/api/v1/notification")
public interface MessageNotificationMicroservice {
    @GetMapping("/test")
    String testConnectivity();
    @PostMapping("/sendEmail")
    ResultCode sendEmail(@RequestParam("dstAddr") String dstAddr,
                         @RequestParam("content") String content);
    @PostMapping("/writeLog")
    ResultCode writeLog(@RequestParam("timestamp") String timestamp,
                        @RequestParam("microservicesname") String microservicesname,
                        @RequestParam("filename") String filename,
                        @RequestParam("content") String content);
    @PostMapping("/updateFlowRule")
    ResultCode updateFlowRule(@RequestParam("qps") int qps);
}*/

@FeignClient(name = "MessageNotificationMicroservice", path = "/api/v1/notification")
public interface MessageNotificationMicroservice {

    @GetMapping("/test")
    String testConnectivity();

    @PostMapping("/sendEmail")
    ResultCode sendEmail(@RequestParam("dstAddr") String dstAddr,
                         @RequestParam("content") String content);

    @PostMapping("/writeLog")
    ResultCode writeLog(@RequestParam("timestamp") String timestamp,
                        @RequestParam("microservicesname") String microservicesname,
                        @RequestParam("filename") String filename,
                        @RequestParam("content") String content);

    @PostMapping("/updateFlowRule")
    ResultCode updateFlowRule(@RequestParam("qps") int qps);

    
}
