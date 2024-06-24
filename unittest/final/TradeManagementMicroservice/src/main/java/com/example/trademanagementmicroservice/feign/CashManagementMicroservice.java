package com.example.trademanagementmicroservice.feign;

import com.example.trademanagementmicroservice.entites.ResultCode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@FeignClient(name = "CashManagementMicroservice", path = "/api/v1/cashmanagement")
public interface CashManagementMicroservice {
  @GetMapping("/NewTradeID")
  String NewTradeID();

  // 添加订单
  @PostMapping("/AddOrder")
  ResultCode AddOrder(@RequestParam String ordertype,
          @RequestParam String ordernumber,
          @RequestParam String TradeManid,
          @RequestParam String Cardid,
          @RequestParam String company,
          @RequestParam String fundid,
          @RequestParam String SubmissionAmount,
          @RequestParam Double Rate,
          @RequestParam Double share,
          @RequestParam String orderstate,
          @RequestParam String SubmissionTime,
          @RequestParam String ConfirmTime);

  // 通过订单号查询订单
  @GetMapping("/FindOrder")
  Map<String, Object> FindOrder(@RequestParam String ordertype ,@RequestParam String ordernumber);

  //通过订单号修改订单状态
  @PostMapping("/ChangeState")
  ResultCode ChangeState(@RequestParam String ordertype ,@RequestParam String ordernumber, @RequestParam String orderstate);
  

  //接口：资金表1/2都是传入账户号 传入+/- 传入数额 能修改资金 

  @PostMapping("/ChangeCash1")
  ResultCode ChangeCash1(@RequestParam String accountid,@RequestParam String SuborAdd, @RequestParam String cashnum);

  @PostMapping("/ChangeCash2")
  ResultCode ChangeCash2(@RequestParam String accountid,@RequestParam String SuborAdd, @RequestParam String cashnum);

  //每日订单发送
  @PostMapping("/DailySend")
  ResultCode DailySend() ;
  

  //每日订单确认
  @PostMapping("/DailyConfirm")
  ResultCode DailyConfirm(@RequestParam String confirmdate) ;

  //输入日期和订单类型 返回对应日期的所有订单编号
  @GetMapping("/ListOrderNumber")
  List<String> ListOrderNumber(@RequestParam String datetime, @RequestParam String ordertype);

  @PostMapping("/AddCash2")
  ResultCode AddCash2(@RequestParam String accountid, @RequestParam Double cash);

    //////////// lyy///////////////////////
    // 生成一个没有用过的订单号
  

}
