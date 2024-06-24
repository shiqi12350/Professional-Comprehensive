package com.example.accountmanagementmicroservices.userFeign;

import com.example.accountmanagementmicroservices.entites.returnEntites.AMMResultCode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "MessageNotificationMicroservice", path =
        "/api/v1/notification")
public interface MessageNotificationFeignServices {
    @GetMapping("/Test")
    String testConnectivity();
    @PostMapping("/SendEmail")
    AMMResultCode sendEmail(@RequestParam("dstAddr") String dstAddr,
                            @RequestParam("content") String content);
    @PostMapping("/WriteLog")
    AMMResultCode writeLog(@RequestParam("timestamp") String timestamp,
                           @RequestParam("microservicesname") String microservicesname,
                           @RequestParam("filename") String filename,
                           @RequestParam("content") String content);
    @PostMapping("/UpdateFlowRule")
    AMMResultCode updateFlowRule(@RequestParam("qps") int qps);
}
