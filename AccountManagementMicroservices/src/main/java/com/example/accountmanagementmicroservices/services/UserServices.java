package com.example.accountmanagementmicroservices.services;

import com.example.accountmanagementmicroservices.entites.dbEntities.AccountInfo;
import com.example.accountmanagementmicroservices.entites.dbEntities.FundAccount;
import com.example.accountmanagementmicroservices.entites.dbEntities.UserInfo;
import com.example.accountmanagementmicroservices.entites.returnEntites.*;
import com.example.accountmanagementmicroservices.mapper.UserAccountMapper;
import com.example.accountmanagementmicroservices.userFeign.MessageNotificationFeignServices;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

@Service
public class UserServices {
    @Autowired
    UserAccountMapper userAccountMapper;
    @Autowired
    MessageNotificationFeignServices messageNotificationFeignServices;

    public String getTimeStamp(){
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        ZonedDateTime timestampNow = ZonedDateTime.ofInstant(Instant.now(), zoneId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String resultTimeStamp = timestampNow.format(formatter);

        return resultTimeStamp;
    }
    public AMMReturnUserID Login(
            String userPhone,
            String password
    ){
        AMMReturnUserID rc = new AMMReturnUserID();
        List<UserInfo>l1 = userAccountMapper.getUserInfoFromUserPhoneAndPassword(userPhone,password);
        if(l1.size()!=1){
            rc.setResultCode(-101);
        }else{
            rc.setUserID(l1.get(0).getUserID());
        }
        return rc;
    }
    // 解析JSON响应中的resultCode字段
    public int parseResultCode(String jsonResponse) {
        // 这里假设JSON格式如下：{"resultCode": 1}，实际情况可能需要根据接口返回的具体JSON结构进行调整
        int resultCode = 0;
        try {
            String[] parts = jsonResponse.split(":");
            String codeStr = parts[1].trim();
            codeStr = codeStr.substring(0, codeStr.length() - 1); // 去掉末尾的"}"
            resultCode = Integer.parseInt(codeStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultCode;
    }

    //与后台银行卡系统进行交互
    public int communicateWithBankSysToCheckBankCard(String userID,String cardID,String SPK){
        String apiUrl = "http://8.130.119.249:10100/api/v1/bankcard/CheckUserID";

        try {
            // 构建URL
            URL url = new URL(apiUrl + "?cardID=" + cardID + "&SPK=" + SPK + "&userID=" + userID);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // 解析JSON响应
            String jsonResponse = response.toString();
            int resultCode = parseResultCode(jsonResponse);

            // 根据返回结果输出不同的提示信息
            switch (resultCode) {
                case 1:
                    return 1;
                case -201:
                    return -103;
                case -101:
                    return -102;
                case -203:
                    return -105;
                default:
                    System.out.println("未知错误码：" + resultCode);
                    return -404;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -404;
        }
    }
    public AMMResultCode Register(
            String userID,
            String userName,
            String userPhone,
            String password,
            String birth,
            String email
    ){
        AMMResultCode rc = new AMMResultCode();

        //首先我们需要判断此时对应的用户身份证号有无重复
        List<UserInfo> l1 = userAccountMapper.getUserInfoFromUserID(userID);
        if(l1.size()!=0){
            rc.setResultCode(-201);
            return rc;
        }

        //接着需要判断此时用户对应的手机号有无重复
        List<UserInfo> l2 = userAccountMapper.getUserInfoFromUserPhone(userPhone);
        if(l2.size()!=0){
            rc.setResultCode(-202);
            return rc;
        }

        //将注册信息写入数据库
        userAccountMapper.insertUserInfo(userID,userName,userPhone,password,birth,email,2);

        //留出未来接入信息通知微服务的部分
        messageNotificationFeignServices.sendEmail(email,"您的账号创建成功！当前权限：可浏览基础信息，如需进行交易，请进一步绑定银行卡");

        //留出未来接入日志微服务
        String timeStamp = getTimeStamp();
        messageNotificationFeignServices.writeLog(timeStamp,"AccountManagementMicroservice","账户注册","身份证号为"+userID+"的用户注册账户成功！当前账户等级：2");

        return rc;
    }



    public AMMReturnUserInfo GetUserInfo(
            String userID
    ){
        AMMReturnUserInfo rc = new AMMReturnUserInfo();
        List<UserInfo>l1 = userAccountMapper.getUserInfoFromUserID(userID);
        if(l1.size()!=1){
            rc.setResultCode(-101);
            return rc;
        }
        rc.setBirth(l1.get(0).getBirth());
        rc.setUserName(l1.get(0).getUserName());
        rc.setUserPhone(l1.get(0).getUserPhone());
        rc.setEmail(l1.get(0).getEmail());
        rc.setUserID(l1.get(0).getUserID());
        rc.setPassWord(l1.get(0).getPassword());
        rc.setLevel(l1.get(0).getLevel());
        return rc;
    }


    public AMMResultCode ModifyUserInfo(
            String userID,
            String userName,
            String userPhone,
            String password,
            String birth,
            String email
    ){
        AMMResultCode rc = new AMMResultCode();

        //首先我们需要判断此时对应的用户身份证号是否存在
        List<UserInfo> l1 = userAccountMapper.getUserInfoFromUserID(userID);
        if(l1.size()!=1){
            rc.setResultCode(-101);
            return rc;
        }

        //接着需要判断此时用户对应的手机号有无重复
        List<UserInfo> l2 = userAccountMapper.getUserInfoFromUserPhone(userPhone);
        //如果此时对应手机号不是仅出现一次或者未出现，则进行报错
        if(!(l2.size()==0||(l2.size()==1&&l2.get(0).getUserID().equals(userID)))){
            rc.setResultCode(-202);
            return rc;
        }

        //将更新后的信息写入数据库
        userAccountMapper.updateUserInfo(userID,userName,userPhone,password,birth,email);
        messageNotificationFeignServices.sendEmail(email,"您的账号信息已经修改！如非本人操作，请及时联系客服");

        //留出未来接入日志微服务
        String timeStamp = getTimeStamp();
        messageNotificationFeignServices.writeLog(timeStamp,"AccountManagementMicroservice","用户信息修改","身份证号为"+userID+"的用户信息发生修改！");

        return rc;
    }

    public AMMReturnUserEmail getUserEmail(
            String userID
    ){
        AMMReturnUserEmail ammReturnUserEmail = new AMMReturnUserEmail();

        List<UserInfo>l1 = userAccountMapper.getUserInfoFromUserID(userID);
        if(l1.size()!=1){
            ammReturnUserEmail.setResultCode(-101);
            return ammReturnUserEmail;
        }
        ammReturnUserEmail.setEmail(l1.get(0).getEmail());

        return ammReturnUserEmail;
    }

    public AMMReturnUserEmail getUserEmailFromCardID(
            String cardID
    ){
        AMMReturnUserEmail ammReturnUserEmail = new AMMReturnUserEmail();

        List<AccountInfo>l1 = userAccountMapper.getAccountInfoFromCardID(cardID);
        if(l1.size()!=1){
            ammReturnUserEmail.setResultCode(-104);
            return ammReturnUserEmail;
        }
        AMMReturnUserEmail ammReturnUserEmail1 = getUserEmail(l1.get(0).getUserID());

        return ammReturnUserEmail1;
    }

    public AMMResultCode bindBankCard(
            String userID,
            String cardID,
            String SPK
    ){
        AMMResultCode ammResultCode = new AMMResultCode();

        //首先需要验证userID的合法性
        List<UserInfo>l1 = userAccountMapper.getUserInfoFromUserID(userID);
        if(l1.size()!=1){
            ammResultCode.setResultCode(-101);
            return ammResultCode;
        }
        //接下来我们需要判断cardID是否已经注册过了
        List<AccountInfo>l2 = userAccountMapper.getAccountInfoFromCardID(cardID);
        if(l2.size()!=0){
            ammResultCode.setResultCode(-203);
            return ammResultCode;
        }
        //然后通过与银行系统交互，查看对应的银行卡号是否合法
        ammResultCode.setResultCode(communicateWithBankSysToCheckBankCard(userID,cardID,SPK));
        if(ammResultCode.getResultCode()!=1)
            return ammResultCode;

        //接下来下来插入对应的银行卡信息
        userAccountMapper.insertAccountInfo(userID,cardID);

        //修改对应账户的权限
        userAccountMapper.updateUserLevel(userID,3);

        AMMReturnUserEmail ammReturnUserEmail = getUserEmail(userID);

        messageNotificationFeignServices.sendEmail(ammReturnUserEmail.getEmail(),"您的账户银行卡绑定成功，绑定的银行卡为："+cardID+"，账户等级变动，当前账户等级3，您可以进行基金的买卖操作！");
        messageNotificationFeignServices.writeLog(getTimeStamp(),"AccountManagementMicroservice","用户信息修改","身份证为"+userID+"的账户绑定银行卡"+cardID+"成功！当前权限等级为3！");

        return ammResultCode;
    }

    public AMMResultCode dismissBankCard(
            @Param("userID")String userID,
            @Param("cardID")String cardID
    ){
        AMMResultCode ammResultCode = new AMMResultCode();

        //先检查对应的cardID是否合法
        List<AccountInfo>l1 = userAccountMapper.getAccountInfoFromCardID(cardID);
        if(l1.size()!=1){
            ammResultCode.setResultCode(-104);
            return ammResultCode;
        }
        if(!l1.get(0).getUserID().equals(userID)){
            ammResultCode.setResultCode(-106);
            return ammResultCode;
        }

        //再检查对应的cardID内是否还有份额
        List<FundAccount>l2 = userAccountMapper.getFundAccountFromCardID(cardID);
        if(l2.size()!=0){
            ammResultCode.setResultCode(-302);
            return ammResultCode;
        }

        //删除对应的card
        userAccountMapper.deleteAccountInfo(cardID);

        //检查userID是否还有其他的cardID,如果没有，需要降低等级
        List<AccountInfo>l3 = userAccountMapper.getAccountInfoFromUserID(userID);
        if(l3.size()==0){
            userAccountMapper.updateUserLevel(userID,2);
            messageNotificationFeignServices.writeLog(getTimeStamp(),"AccountManagementMicroservice","用户信息修改","身份证为"+userID+"的账户解绑银行卡"+cardID+"成功！当前用户权限：不可进行交易，如需交易需进一步绑定银行卡");
            AMMReturnUserEmail ammReturnUserEmail = getUserEmail(userID);
            messageNotificationFeignServices.sendEmail(ammReturnUserEmail.getEmail(),"您的账户银行卡解绑，解绑的银行卡为："+cardID+"，账户等级变动，当前用户权限：不可进行交易，如需交易需进一步绑定银行卡！");
        }else{
            messageNotificationFeignServices.writeLog(getTimeStamp(),"AccountManagementMicroservice","用户信息修改","身份证为"+userID+"的账户解绑银行卡"+cardID+"成功！当前用户权限：可正常进行交易");
            AMMReturnUserEmail ammReturnUserEmail = getUserEmail(userID);
            messageNotificationFeignServices.sendEmail(ammReturnUserEmail.getEmail(),"您的账户银行卡解绑，解绑的银行卡为："+cardID);
        }
        return ammResultCode;
    }

    public AMMReturnBankCardList getBankCardList(
            String userID
    ){
        AMMReturnBankCardList ammReturnBankCardList = new AMMReturnBankCardList();
        //首先需要验证userID的合法性
        List<UserInfo>l1 = userAccountMapper.getUserInfoFromUserID(userID);
        if(l1.size()!=1){
            ammReturnBankCardList.setResultCode(-101);
            return ammReturnBankCardList;
        }
        //接着获取其对应的银行卡号列表
        List<AccountInfo>l2 = userAccountMapper.getAccountInfoFromUserID(userID);
        List<String> lC = new ArrayList<>();
        for (AccountInfo accountInfo : l2) {
            lC.add(accountInfo.getCardID());
        }
        ammReturnBankCardList.setBankCardList(lC);

        return ammReturnBankCardList;
    }



    public AMMResultCode modifyPortionSendMessage(
            String cardID,
            String fundID,
            double srcAmount
    ){
        AMMResultCode ammResultCode = new AMMResultCode();
        try{
            String timeStamp = getTimeStamp();
            messageNotificationFeignServices.writeLog(timeStamp,"AccountManagementMicroservice","用户份额变动","银行卡为"+cardID+"的账户的"+fundID+"基金份额发生变动，修改后的份额为："+srcAmount);
            AMMReturnUserEmail ammReturnUserEmail =  getUserEmailFromCardID(cardID);
            if(ammReturnUserEmail.getResultCode()==1){
                messageNotificationFeignServices.sendEmail(ammReturnUserEmail.getEmail(),"银行卡为"+cardID+"的账户的"+fundID+"基金份额发生变动，修改后的份额为："+srcAmount);
            }
            return ammResultCode;
        }catch (Exception e){
            e.printStackTrace();
            ammResultCode.setResultCode(-404);
            return ammResultCode;
        }
    }
    public AMMResultCode modifyPortion(
            String cardID,
            String fundID,
            double amount
    ){
        AMMResultCode ammResultCode = new AMMResultCode();

        //首先我们需要判断cardID合法性
        List<AccountInfo>l1 = userAccountMapper.getAccountInfoFromCardID(cardID);
        if(l1.size()!=1){
            ammResultCode.setResultCode(-104);
            return ammResultCode;
        }
        //接下来，我们需要读取数据库中是否已经存在对应的份额
        List<FundAccount>l2 = userAccountMapper.getFundAccountFromCardIDAndFundID(cardID, fundID);

        //如果存在的话
        if(l2.size()!=0){
            //首先计算出此时修改后对应的份额
            double srcPortion = amount + l2.get(0).getAmount();
            if(abs(srcPortion)>1e-4&&srcPortion<0){
                //这时候需要报错
                ammResultCode.setResultCode(-301);
                return ammResultCode;
            }
            if(abs(srcPortion)<1e-4){
                userAccountMapper.deleteFundAccount(cardID,fundID);
            }else{
                userAccountMapper.updateFundAccount(cardID,fundID,srcPortion);
            }
            modifyPortionSendMessage(cardID,fundID,srcPortion);
            return ammResultCode;
        }

        //如果不存在的话
        if(amount<0){
            ammResultCode.setResultCode(-301);
            return ammResultCode;
        }
        userAccountMapper.insertFundAccount(cardID,fundID,amount);
        modifyPortionSendMessage(cardID,fundID,amount);
        return ammResultCode;
    }

    public AMMReturnFundAccount getAllPortion(
            @Param("cardID")String cardID
    ){
        AMMReturnFundAccount ammReturnFundAccount = new AMMReturnFundAccount();
        //首先我们需要判断cardID合法性
        List<AccountInfo>l1 = userAccountMapper.getAccountInfoFromCardID(cardID);
        if(l1.size()!=1){
            ammReturnFundAccount.setResultCode(-104);
            return ammReturnFundAccount;
        }
        //接着我们需要获取cardID对应的份额列表
        List<FundAccount>l2 = userAccountMapper.getFundAccountFromCardID(cardID);
        ammReturnFundAccount.setInfo(l2);
        return ammReturnFundAccount;
    }
}
