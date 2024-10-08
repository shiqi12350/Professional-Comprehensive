package com.example.trademanagementmicroservice.services;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.example.trademanagementmicroservice.entites.*;
import com.example.trademanagementmicroservice.feign.AccountManagementMicroservices;
import com.example.trademanagementmicroservice.feign.CashManagementMicroservice;
import com.example.trademanagementmicroservice.feign.InformationMaintenanceMicroservice;
import com.example.trademanagementmicroservice.feign.MessageNotificationMicroservice;
import com.example.trademanagementmicroservice.feign.bankCardSys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.MediaType;

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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;



@Service
public class Trade {
    String Emailaddr="2576022201@qq.com";
    String cardid = "966394548952071226";    //目前将持有人为林越野的银行卡作为平台银行卡
    String pwd_lyy = "5a5ea58eb9ca5b5dada7b7376b6b68c9";
    @Autowired
    InformationMaintenanceMicroservice informationMaintenanceMicroservice;
    @Autowired
    AccountManagementMicroservices accountManagementMicroservices;
    @Autowired
    MessageNotificationMicroservice messageNotificationMicroservice;
    @Autowired
    CashManagementMicroservice cashManagementMicroservice;
    @Autowired
    bankCardSys bankcard;

    public String getNowTime(){
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        ZonedDateTime timestampNow = ZonedDateTime.ofInstant(Instant.now(), zoneId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return timestampNow.format(formatter);
    }
    public String gettoday(){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }

    public String getNextValidTradingDay(){
        // 获取明天的日期
        LocalDate nextDay = LocalDate.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String nextDay_str=nextDay.format(formatter);

        // 判断明天是否是有效交易日，如果不是，一直往后找
        //while (!isValidTradingDay(nextDay)) {
        while (informationMaintenanceMicroservice.checkWorkingDay(nextDay_str).getResultCode()!=1) {
            nextDay = nextDay.plusDays(1);
            nextDay_str=nextDay.format(formatter);
        }
        return nextDay_str;
    }

    public ResultCode getNewAccounttoTrade(){
        try {
            /*//初始化一个随机用户    ---lyy   不知是否有用，先注释
            TradeAccount TA = accountFeignClient.getNewAccount();
            //为这个用户开设银行卡，初始化一个资金，该表为初始化表
            BankAccount BA = accountFeignClient.bindUserCardNumber(TA.getcId());
            //把该用户的卡号，资金加入新表(用于操作流水)
            cashManagementMicroservice.AddCash2(BA.getCardId(), BA.getAmount());

            messageNotificationMicroservice.writeLog(getNowTime(), "trademanagementmicroservice",
                    "getNewAccounttoTradeSuccess", TA.getcName() + TA.getcId() + " 卡号:" +
                     BA.getCardId()+BA.getAmount()       + "  开户成功！");*/
            ResultCode rc = new ResultCode(1);
            return rc;
        }catch (Exception e){
            ResultCode rc = new ResultCode(-1);
            return rc;
        }
    }

    public ResultCode sendOrder() {
        messageNotificationMicroservice.writeLog(getNowTime(), "trademanagementmicroservice",
                "sendOrderSuccess", "本日订单已发送  订单状态位：2" );
        return cashManagementMicroservice.DailySend();
    }

    public ResultCode getconfirm() {
        messageNotificationMicroservice.writeLog(getNowTime(), "trademanagementmicroservice",
                "sendOrderSuccess", "到期订单已确认  订单状态位：3");
        String Date=gettoday();

        return cashManagementMicroservice.DailyConfirm(Date);
    }

    public static String generateUserID(String baseUserID) {
        StringBuilder userID = new StringBuilder(baseUserID);
        Random random = new Random();

        // Append four random digits to the base user ID
        for (int i = 0; i < 4; i++) {
            int randomDigit = random.nextInt(10); // generates a random number between 0 and 9
            userID.append(randomDigit);
        }

        return userID.toString();
    }

    public static String extractFirstSixDigits(String fundID) {
        // Check if the fundID is at least 6 characters long
        if (fundID.length() >= 6) {
            return fundID.substring(0, 6);
        } else {
            // Handle the case where fundID is less than 6 characters long
            throw new IllegalArgumentException("fundID must be at least 6 characters long");
        }
    }

    
    public String callExternalApi_GET(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // For GET request
        Request getRequest = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(getRequest).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }

    }

    public String callExternalApi_POST(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

       // For POST request
       MediaType JSON = MediaType.get("application/json; charset=utf-8");
       String json = "{\"key\": \"value\"}";
       okhttp3.RequestBody body = okhttp3.RequestBody.create(json, JSON);  // 使用完全限定名
       Request postRequest = new Request.Builder()
               .url(url)
               .post(body)
               .build();
       try (Response response = client.newCall(postRequest).execute()) {
           if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
           return response.body().string();
       }

    }

    public TradeOrder Subscribe(String TraderID, String fundID, double tradeAmount, String cardID, String passWord){
        String SPK = "oA5XHdFaJ9";
        /*申购逻辑*/
        LocalDateTime currentTime = LocalDateTime.now();
        int i=1;//调试用
        //判断是否交易时间
        //if(informationMaintenanceMicroservice.checkWorkingHours(currentTime.toString()).getResultCode()==1){
        if(i==1){
            informationMaintenanceMicroservice.getMinPurchaseAmount(fundID,"commonBuy");
            ReturnRateValue MinAmount=informationMaintenanceMicroservice.getMinPurchaseAmount(fundID,"commonBuy");
            ReturnValue MaxAmount=informationMaintenanceMicroservice.getDayMaxBuyAmount(fundID);
            if(MinAmount.getResultCode()==1&&MaxAmount.getResultCode()==1) {
                if(tradeAmount<= MaxAmount.getValue()&&tradeAmount>= MinAmount.getValue()) {
                    //获取新订单号
                    String OrderNum=cashManagementMicroservice.NewTradeID();
                    double rate = informationMaintenanceMicroservice.getSubscriptionFees(fundID, tradeAmount).getValue();
                    TradeOrder currentOrder = new TradeOrder(OrderNum,TraderID, fundID, tradeAmount, rate, cardID, 1,getNextValidTradingDay());//创建订单
                    //订单记录该笔交易的交易人id，基金公司，基金品种、金额、是否成功、是否有效、交易时间、交易规则确认日
                    //accountFeignClient.openingFundAccount(cardID, TraderID, currentOrder.getCompany());
                    //记录完毕之后判断是否有基金交易公司的开基金账户流程
                    //默认客户正常付款,原账户扣钱
                    //cashManagementMicroservice.ChangeCash2(cardID,"Sub",Double.toString(tradeAmount));
                    //cashManagementMicroservice.ChangeCash1(cardID,"Add",Double.toString(tradeAmount));
                    //查找银行卡系统里是否有该基金的银行卡，如果没有，为这个基金创建一个新的银行卡,其基金代码就作为用户名,并create其SPK
                    
                    String bankcardResult = null;
                    String url="http://8.130.119.249:10101/api/v1/bankcard/checkUser?userName="+fundID;
                    try {
                        bankcardResult = callExternalApi_GET(url);
                        System.out.println("Response: " + bankcardResult);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    JSONObject bankcardJSON = new JSONObject(bankcardResult);
                    int resultCode = bankcardJSON.getInt("resultCode");
                    if(resultCode != 1){
                        String baseUserID = "35018120030720";
                        String usrID = generateUserID(baseUserID);
                        String pwd = extractFirstSixDigits(fundID);
                        url = "http://8.130.119.249:10101/api/v1/bankcard/Register?userName="+fundID+
                        "&userID="+usrID+"&passWord="+pwd+"&loginPassWord="+fundID+"&birth="+"2000-01-01";
                        try {
                            bankcardResult = callExternalApi_POST(url);
                            System.out.println("Response: " + bankcardResult);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
       
                        //CreateResult Re = bankcard.Register(fundID,usrID,pwd,fundID,"2000-01-01");
                        bankcardJSON = new JSONObject(bankcardResult);
                        String resultstr = bankcardJSON.getString("cardID");
                        url = "http://8.130.119.249:10101/api/v1/bankcard/CreateSPK?cardID="+resultstr+"&passWord="+pwd;
                        try {
                            bankcardResult = callExternalApi_GET(url);
                            System.out.println("Response: " + bankcardResult);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //bankcard.CreateSPK(Re.getCardID(), pwd);
                    }
                    url = "http://8.130.119.249:10101/api/v1/bankcard/DeductPayment?cardID="+cardID+"&passWord="+passWord+
                    "&SPK="+SPK+"&amount="+tradeAmount;
                    try {
                        bankcardResult = callExternalApi_GET(url);
                        System.out.println("Response: " + bankcardResult);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //bankcard.DeductPayment(cardID,passWord,SPK,tradeAmount);
                    url = "http://8.130.119.249:10101/api/v1/bankcard/TransferFunds?cardID="+cardid+
                    "&SPK="+SPK+"&amount="+tradeAmount;
                    try {
                        bankcardResult = callExternalApi_GET(url);
                        System.out.println("Response: " + bankcardResult);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //bankcard.TransferFunds(cardid,SPK,tradeAmount);//给平台转账，平台有且只有一张银行卡
                    //支付订单,钱从客户账户流向平台账户

                    //其中申购费会留在平台账户作为佣金
                    //设置订单暂时有效
                    //公司名设为基金代码 --lyy
                    currentOrder.setOrdercompany(fundID);
                    currentOrder.seteffect();
                    //计算此时能买到的对应份额
                    currentOrder.setportion(informationMaintenanceMicroservice.getFundValue(fundID).getValue());
    
                    //添加订单
                    cashManagementMicroservice.AddOrder(currentOrder.setOrderType(1), currentOrder.getOrderNum(),
                            currentOrder.getTraderID(),currentOrder.getcardID(), currentOrder.getCompany()
                            ,currentOrder.getfundID(),Double.toString(currentOrder.getfundAmount())
                            ,currentOrder.getfundFeeRate(),currentOrder.getportion(),
                            Integer.toString(currentOrder.getstate()),currentOrder.getsubmitDate(),currentOrder.getconfirmDate());
                            
        
                    //写日志
                    /*DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                    messageNotificationMicroservice.writeLog(currentTime.format(formatter),
                            "trademanagementmicroservice", "Subscribe-success",
                            "用户" + TraderID + "申购" + fundID + "下单成功" + " 卡号：" + currentOrder.getcardID() +
                            " 转出金额：" + currentOrder.getfundAmount() + "  平台流入金额：" + currentOrder.getfundAmount());
                    //发邮件
                    //messageNotificationMicroservice.sendEmail(Emailaddr,"订单编号"+currentOrder.getOrderNum()+"您已经成功下单基金"+fundID+"暂未到交易规则规定的确认日期，请耐心等待，今日闭市前您随时可以撤销此订单");
                    */
                    return currentOrder;

                }
                else {
                    messageNotificationMicroservice.writeLog(getNowTime(),"trademanagementmicroservice",
                            "SubscribeFail", "交易金额不符合交易规则，申购拒绝");
                    TradeOrder co=new TradeOrder();
                    return co;
                }
            }
            else {
                messageNotificationMicroservice.writeLog(getNowTime(),"trademanagementmicroservice",
                        "SubscribeFail", "获取交易上下限失败，申购拒绝");
                TradeOrder co=new TradeOrder();
                return co;
            }
        }
        else{
            messageNotificationMicroservice.writeLog(getNowTime(),"trademanagementmicroservice",
                    "SubscribeFail", "未处在交易时间，申购拒绝");
            TradeOrder co=new TradeOrder();
            return co;
        }


    }

    public TradeOrder Redemption(String Ordernum){
        /*赎回逻辑*/
/*
1. **持有期满：** 小明持有基金A的份额达到180天。
2. **选择赎回：** 小明在基金代销平台M上选择了赎回基金A的份额。
3. **计算赎回金额：** 根据基金A当日的净值和小明持有的份额数量计算赎回金额。
4. **支付赎回费：** 如果基金A设置了申购费，小明可能需要支付相应的费用。申购费通常在一定的持有期后逐渐降低，并在一定的时间后完全免除。
5. **资金结算：** 赎回的资金将在一定时间内划入小明的银行账户。
6. **确认赎回：** 基金代销平台M会生成确认文件，显示小明成功赎回了基金A的份额。
为保护长期投资者利益，证监会规定，本基金对持有期较短的投资者赎回时，将收取不低于0.5%比例的赎回费。该费用由基金公司收取，并计入基金财产。
cashManagementMicroservice.AddOrder(currentOrder.setOrderType(1), currentOrder.getOrderNum(),
                            currentOrder.getTraderID(),currentOrder.getcardID(), currentOrder.getCompany()
                            ,currentOrder.getfundID(),Double.toString(currentOrder.getfundAmount())
                            ,currentOrder.getfundFeeRate(),currentOrder.getportion(),
                            Integer.toString(currentOrder.getstate()),currentOrder.getsubmitDate(),currentOrder.getconfirmDate());
 */
        //if(informationMaintenanceMicroservice.checkWorkingHours(getNowTime()).getResultCode()==1)
        int i=1;//调试用
        if(i==1)
        {
            //判断交易时间内
            //通过订单号查询申购订单
            Map<String, Object> orderMap = cashManagementMicroservice.FindOrder("1",Ordernum);
            ////计算持有时间
            // 将申购订单提交日期转换为 LocalDate 对象
            LocalDate existingDate = LocalDate.parse(orderMap.get("SubmissionTime").toString(), DateTimeFormatter.ISO_DATE);
            // 获取今天的日期
            LocalDate today = LocalDate.now();
            // 计算两个日期之间的天数差
            double daysDifference = ChronoUnit.DAYS.between(existingDate, today);
            ////计算持有时间结束
            //根据持有时间计算赎回费率
            double rate=informationMaintenanceMicroservice.getRedemptionFees(orderMap.get("fundid").toString(), daysDifference).getValue();
            //赎回订单构造函数public TradeOrder(String OrderNum,int OrderType,String TraderID,String fundID,String cardID,double portion,double fundFeeRate)
           
            TradeOrder currentOrder = new TradeOrder(Ordernum,2,
                    orderMap.get("TradeManId").toString(), orderMap.get("fundid").toString(),orderMap.get("Cardid").toString(),
                    Double.parseDouble(orderMap.get("share").toString()),rate,getNextValidTradingDay());
            //创建订单i
            currentOrder.setOrdercompany(orderMap.get("fundid").toString());
            //设置当前订单暂时有效
            currentOrder.seteffect();
            //创建好赎回订单后计算订单对应份额能赎回的实际金额(可能去除费率)
            currentOrder.setfundAmount(informationMaintenanceMicroservice.getFundValue(orderMap.get("fundid").toString()).getValue());
            //添加赎回订单
   
            cashManagementMicroservice.AddOrder(currentOrder.setOrderType(2), currentOrder.getOrderNum(),
                    currentOrder.getTraderID(),currentOrder.getcardID(), currentOrder.getCompany()
                    ,currentOrder.getfundID(),Double.toString(currentOrder.getfundAmount())
                    ,currentOrder.getfundFeeRate(),currentOrder.getportion(),
                    Integer.toString(currentOrder.getstate()),currentOrder.getsubmitDate(),currentOrder.getconfirmDate());

            //messageNotificationMicroservice.writeLog(getNowTime(),
                    //"trademanagementmicroservice", "Redemption-Success",
                    //"用户"+currentOrder.getTraderID()+"订单编号：" + Ordernum + "赎回" + currentOrder.getfundID() + "下单成功");
            //写日志
            //messageNotificationMicroservice.sendEmail(Emailaddr,"订单编号"+Ordernum+"您已经成功下单赎回基金"+currentOrder.getfundID()+"暂未到交易规则规定的确认日期，请耐心等待，今日闭市前您随时可以撤销此订单");
            //发邮件
            return currentOrder;

        }
        else {
            messageNotificationMicroservice.writeLog(getNowTime(),"trademanagementmicroservice",
                    "RedemptionFail", "未处在交易时间赎回拒绝");
            TradeOrder co=new TradeOrder();
            return co;
        }
    }


    public ResultCode confirmSubscribe( ){
        ResultCode rc=new ResultCode(1);
        //每天执行一次，判断所有申购订单是否到了申购确认日
        String today=gettoday();
        //开始前获取所有今日到期订单
        List<String> orderNumbers = cashManagementMicroservice.ListOrderNumber(today, "1");
        // 遍历返回的所有订单 List
        for (String orderNumber : orderNumbers) {
            Map<String, Object> orderMap = cashManagementMicroservice.FindOrder("1",orderNumber);
            //对所有已经确认的订单处理
            if(Objects.equals(orderMap.get("orderstate").toString(), "3")) {
                //设置所有订单状态为
                //cashManagementMicroservice.changestate(orderNumber, "3");
                //计算应流向基金多少钱（扣除申购费）
                double amount = Double.parseDouble(orderMap.get("SubmissionAmount").toString());
                double toFundAmount = amount * (1 - Double.parseDouble(orderMap.get("Rate").toString()) / 100);
                double fee =amount*Double.parseDouble(orderMap.get("Rate").toString()) / 100;
                //平台的个人账户减少，基金账户增加
                //cashManagementMicroservice.ChangeCash1(orderMap.get("Cardid").toString(), "Sub", Double.toString(toFundAmount));
                //cashManagementMicroservice.ChangeCash2(orderMap.get("fundid").toString(), "Add", Double.toString(toFundAmount));
                String bankcardResult = null,SPK=null,CardID=null;
                String url_1 = String.format("http://8.130.119.249:10101/api/v1/bankcard/GetSPK3?userName=%s",orderMap.get("fundid").toString());
                        try {
                            SPK = callExternalApi_GET(url_1);
                            JSONObject jsonResponse = new JSONObject(SPK);
                            SPK = jsonResponse.getString("spk");
                            System.out.println("Response: " + SPK);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                String url_2 = String.format("http://8.130.119.249:10101/api/v1/bankcard/GetCardID?userName=%s",orderMap.get("fundid").toString());
                        try {
                            CardID = callExternalApi_GET(url_2);
                            
                            System.out.println("Response: " + CardID);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                String url = "http://8.130.119.249:10101/api/v1/bankcard/DeductPayment?cardID="+cardid+"&passWord="+pwd_lyy+
                    "&SPK="+SPK+"&amount="+Double.toString(toFundAmount);
                    try {
                        bankcardResult = callExternalApi_GET(url);
                        System.out.println("Response: " + bankcardResult);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //bankcard.DeductPayment(cardID,passWord,SPK,tradeAmount);
                    url = "http://8.130.119.249:10101/api/v1/bankcard/TransferFunds?cardID="+CardID+
                    "&SPK="+SPK+"&amount="+Double.toString(toFundAmount);
                    try {
                        bankcardResult = callExternalApi_GET(url);
                        System.out.println("Response: " + bankcardResult);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                //bankcard.DeductPayment(cardid,passWord,SPK,Double.toString(toFundAmount));
                //bankcard.TransferFunds(,SPK,Double.toString(toFundAmount));//
                //资金流动和份额划分同时发生，以下是份额划分
                double portion =Double.parseDouble(orderMap.get("share").toString());
                //昨*******accountFeignClient的问题
                //卡号，基金代码，修改的份额
                //System.out.println("Response: " + orderMap.get("fundid").toString());
                accountManagementMicroservices.modifyPortion(orderMap.get("Cardid").toString(), orderMap.get("fundid").toString()
                , portion);
                //accountFeignClient.confirmTransaction(orderMap.get("company").toString(), orderMap.get("TradeManId").toString()
                  //      , orderMap.get("Cardid").toString(), orderMap.get("fundid").toString(), portion);
                //份额划分结束，交易结束,设置状态位为4代表订单全部完成
                cashManagementMicroservice.ChangeState("1",orderNumber,"4");
                messageNotificationMicroservice.writeLog(getNowTime(), "trademanagementmicroservice",
                        "confirmSubscribeSuccess", "用户：" + orderMap.get("TradeManId") +
                                " 订单号："+  orderNumber + "订单已确认，份额已划分，平台流出金额：" + Double.toString(toFundAmount) +
                                " 基金流入金额："+ Double.toString(toFundAmount)+ " 销售佣金：" + Double.toString(fee));
            }
        }
        //发送确认邮件
        //messageNotificationMicroservice.sendEmail(Emailaddr,getNowTime()+"您的订单已确认，份额已划分");
        //写日志
        messageNotificationMicroservice.writeLog(getNowTime(),"trademanagementmicroservice",
                "confirmSubscribeSuccess", getNowTime()+"到期订单已确认，份额已划分");
        return rc;
    }

    public ResultCode confirmRedemption(){
        //订单确认完毕，钱从基金返回客户，份额从客户手中划走
        ResultCode rc=new ResultCode(1);
        //每天执行一次，判断所有赎回订单是否到了赎回确认日
        String today=gettoday();
        //开始前获取所有今日到期订单
        List<String> orderNumbers = cashManagementMicroservice.ListOrderNumber(today, "2");

        // 遍历返回的所有订单 List
        for (String orderNumber : orderNumbers) {
            Map<String, Object> orderMap = cashManagementMicroservice.FindOrder("2",orderNumber);
            if(Objects.equals(orderMap.get("orderstate").toString(), "3")) {
                //设置所有订单状态为
                //cashManagementMicroservice.changestate(orderNumber, "3");
                //在赎回订单中已经记录了应该流向客户的金额，无需计算
                double amount = Double.parseDouble(orderMap.get("SubmissionAmount").toString());
                //double toFundAmount = amount * (1 - Double.parseDouble(orderMap.get("Rate").toString()) / 100);
                //平台的个人账户增加，基金账户减少
                //cashManagementMicroservice.ChangeCash2(orderMap.get("Cardid").toString(), "Add", Double.toString(amount));
                //cashManagementMicroservice.ChangeCash2(orderMap.get("fundid").toString(), "Sub", Double.toString(amount));
                String bankcardResult = null,SPK=null,CardID=null;
                String url_1 = String.format("http://8.130.119.249:10101/api/v1/bankcard/GetSPK3?userName=%s",orderMap.get("fundid").toString());
                        try {
                            SPK = callExternalApi_GET(url_1);
                            JSONObject jsonResponse = new JSONObject(SPK);
                            SPK = jsonResponse.getString("spk");
                            System.out.println("Response: " + SPK);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                String url_2 = String.format("http://8.130.119.249:10101/api/v1/bankcard/GetCardID?userName=%s",orderMap.get("fundid").toString());
                        try {
                            CardID = callExternalApi_GET(url_2);
                            
                            System.out.println("Response: " + CardID);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String pwd = extractFirstSixDigits(orderMap.get("fundid").toString());
                String url = "http://8.130.119.249:10101/api/v1/bankcard/DeductPayment?cardID="+CardID+"&passWord="+pwd+
                    "&SPK="+SPK+"&amount="+Double.toString(amount);
                    try {
                        bankcardResult = callExternalApi_GET(url);
                        System.out.println("Response: " + bankcardResult);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //bankcard.DeductPayment(cardID,passWord,SPK,tradeAmount);
                    url = "http://8.130.119.249:10101/api/v1/bankcard/TransferFunds?cardID="+cardid+
                    "&SPK="+SPK+"&amount="+Double.toString(amount);
                    try {
                        bankcardResult = callExternalApi_GET(url);
                        System.out.println("Response: " + bankcardResult);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                //资金流动和份额划分同时发生，以下是份额划分
                accountManagementMicroservices.modifyPortion(orderMap.get("Cardid").toString(), orderMap.get("fundid").toString()
                ,  0-Double.parseDouble(orderMap.get("share").toString()));
                //accountFeignClient.confirmTransaction(orderMap.get("company").toString(), orderMap.get("TradeManId").toString()
                        //, orderMap.get("Cardid").toString(), orderMap.get("fundid").toString(), 0-Double.parseDouble(orderMap.get("share").toString()));
                //份额划分结束，交易结束,设置状态位为4代表订单全部完成
                cashManagementMicroservice.ChangeState("2",orderNumber,"4");
                messageNotificationMicroservice.writeLog(getNowTime(), "trademanagementmicroservice",
                        "confirmSubscribeSuccess", "用户"+orderMap.get("TradeManId").toString()+"订单编号"
                                +orderNumber + "基金:"+orderMap.get("fundid").toString()+"赎回订单已确认，金额已赎回"+
                        "基金流出金额"+Double.toString(amount)+"用户账户流入金额"+Double.toString(amount));
            }
        }

        //发送确认邮件
        messageNotificationMicroservice.sendEmail(Emailaddr,getNowTime()+"您的订单已确认，份额已划分");
        //写日志
        messageNotificationMicroservice.writeLog(getNowTime(),"trademanagementmicroservice",
                "confirmRedemptionSuccess", getNowTime()+"到期订单已确认，份额已划分");
        return rc;
    }

    public ResultCode cancelTrade(String Ordernum,String OrderType){
        ResultCode rc=new ResultCode(-1);
        //判断是否交易日内
        //if(informationMaintenanceMicroservice.checkWorkingHours(getNowTime()).getResultCode()==1)
        int i=1;//调试用
        if(i==1)
        {
            //设置订单状态位代表撤销
            cashManagementMicroservice.ChangeState(OrderType,Ordernum,"0");
            //获取当前订单
            Map<String, Object> orderMap = cashManagementMicroservice.FindOrder(OrderType,Ordernum);
            if(Objects.equals(orderMap.get("ordertype").toString(), "1")) {
                //订单对应金额从平台账户流向个人
                //cashManagementMicroservice.ChangeCash1(orderMap.get("Cardid").toString(), "Sub",
                //        orderMap.get("SubmissionAmount").toString());
                //cashManagementMicroservice.ChangeCash2(orderMap.get("Cardid").toString(), "Add",
                //        orderMap.get("SubmissionAmount").toString());

                String bankcardResult = null,SPK=null,CardID=null;
                String url_1 = String.format("http://8.130.119.249:10101/api/v1/bankcard/GetSPK3?userName=%s",orderMap.get("fundid").toString());
                        try {
                            SPK = callExternalApi_GET(url_1);
                            JSONObject jsonResponse = new JSONObject(SPK);
                            SPK = jsonResponse.getString("spk");
                            System.out.println("Response: " + SPK);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                String url_2 = String.format("http://8.130.119.249:10101/api/v1/bankcard/GetCardID?userName=%s",orderMap.get("fundid").toString());
                        try {
                            CardID = callExternalApi_GET(url_2);
                            
                            System.out.println("Response: " + CardID);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                String url = "http://8.130.119.249:10101/api/v1/bankcard/DeductPayment?cardID="+cardid+"&passWord="+pwd_lyy+
                    "&SPK="+SPK+"&amount="+orderMap.get("SubmissionAmount").toString();
                    try {
                        bankcardResult = callExternalApi_GET(url);
                        System.out.println("Response: " + bankcardResult);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //bankcard.DeductPayment(cardID,passWord,SPK,tradeAmount);
                    url = "http://8.130.119.249:10101/api/v1/bankcard/TransferFunds?cardID="+orderMap.get("Cardid").toString()+
                    "&SPK="+SPK+"&amount="+orderMap.get("SubmissionAmount").toString();
                    try {
                        bankcardResult = callExternalApi_GET(url);
                        System.out.println("Response: " + bankcardResult);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                

                //发送撤销邮件
                messageNotificationMicroservice.sendEmail(Emailaddr, "订单编号" + Ordernum + "申购撤销成功,资金将会原路返回您的账户");

                //写日志
                messageNotificationMicroservice.writeLog(getNowTime(), "trademanagementmicroservice",
                        "cancelTradeSuccess", "订单编号" + Ordernum + "申购撤销成功");
                rc.setResultCode(1);
                return rc;
            }
            else if(Objects.equals(orderMap.get("ordertype").toString(), "2")){
                //发送撤销邮件
                messageNotificationMicroservice.sendEmail(Emailaddr, "订单编号" + Ordernum + "赎回撤销成功,份额会继续留在您的账户");
                //写日志
                messageNotificationMicroservice.writeLog(getNowTime(), "trademanagementmicroservice",
                        "cancelTradeSuccess", "订单编号" + Ordernum + "赎回撤销成功");
                rc.setResultCode(1);
                return rc;
            }
            else {
                rc.setResultCode(-1);
                return rc;
            }
        }
        else {
            messageNotificationMicroservice.writeLog(getNowTime(),"trademanagementmicroservice",
                    "RedemptionFail", "未处在交易时间，撤销交易拒绝");
            return rc;
        }

    }

    public ResultCode updateFlowRule(int qps) {
        ResultCode rc = new ResultCode(1);
        try {
            List<FlowRule> rules = new ArrayList<>();
            FlowRule rule = new FlowRule();
            rule.setResource("common");
            rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
            rule.setCount(qps);
            rules.add(rule);
            FlowRuleManager.loadRules(rules);

            // 使用ZoneId.of("Asia/Shanghai")设置时区为东八区
            ZoneId zoneId = ZoneId.of("Asia/Shanghai");
            ZonedDateTime timestampNow = ZonedDateTime.ofInstant(Instant.now(), zoneId);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

            messageNotificationMicroservice.writeLog(timestampNow.format(formatter), "MessageNotificationMicroservice", "SettingSuccess", "设置限流策略成功，设置的限流数" + qps);
        } catch (Exception e) {
            // 使用ZoneId.of("Asia/Shanghai")设置时区为东八区
            ZoneId zoneId = ZoneId.of("Asia/Shanghai");
            ZonedDateTime timestampNow = ZonedDateTime.ofInstant(Instant.now(), zoneId);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

            messageNotificationMicroservice.writeLog(timestampNow.format(formatter), "MessageNotificationMicroservice", "SettingFault", "设置限流策略错误，设置的限流数" + qps);
            rc.setResultCode(-101);
        }
        return rc;
    }

     @Autowired
    private TaskScheduler taskScheduler;

    private Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public void scheduleTask(String TraderID, String fundID, double tradeAmount, String cardID, String passWord,
    String taskId, String cronExpression) {
        cancelTask(taskId); // 取消已有任务

        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(() -> executeTask(TraderID, fundID, tradeAmount,
         cardID, passWord,taskId), new CronTrigger(cronExpression));
        scheduledTasks.put(taskId, scheduledFuture);
    }

    public void schedule_Sub(String taskId, String cronExpression) {
        cancelTask(taskId); // 取消已有任务

        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(() -> executeTask_confirmSub(taskId), new CronTrigger(cronExpression));
        scheduledTasks.put(taskId, scheduledFuture);
    }

    public void schedule_Red(String taskId, String cronExpression) {
        cancelTask(taskId); // 取消已有任务

        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(() -> executeTask_confirmRed(taskId), new CronTrigger(cronExpression));
        scheduledTasks.put(taskId, scheduledFuture);
    }

    public void cancelTask(String taskId) {
        ScheduledFuture<?> scheduledFuture = scheduledTasks.get(taskId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            scheduledTasks.remove(taskId);
        }
    }


    public void rescheduleTask(String TraderID, String fundID, double tradeAmount, String cardID, String passWord,
    String taskId, String cronExpression) {
        cancelTask(taskId);
        scheduleTask(TraderID, fundID, tradeAmount, cardID, passWord,taskId, cronExpression);
    }

    private void executeTask(String TraderID, String fundID, double tradeAmount, String cardID, String passWord,String taskId) {  //创造任务
        TradeOrder t1 = Subscribe(TraderID, fundID, tradeAmount, cardID, passWord);
        // 替换为实际的任务逻辑
        System.out.println("Task [" + taskId + "] executed at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    private void executeTask_confirmSub(String taskId) {  //创造任务
        ResultCode t1 = confirmSubscribe();
        // 替换为实际的任务逻辑
        System.out.println("Task [" + taskId + "] executed at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    private void executeTask_confirmRed(String taskId) {  //创造任务
        ResultCode t1 = confirmRedemption();
        // 替换为实际的任务逻辑
        System.out.println("Task [" + taskId + "] executed at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    public List<Map<String, Object>> Getorder (String TraderID){    //获取某用户的所有订单
        return cashManagementMicroservice.Getorder(TraderID);
    }
    //public TradeOrder Fixed_investment(String TraderID, String fundID, double tradeAmount, String cardID, String passWord, String Deduction)

    }