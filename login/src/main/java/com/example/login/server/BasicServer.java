package com.example.login.server;

import com.example.login.entites.ResultCode;
import com.example.login.mapper.InfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


@Service
public class BasicServer {
    @Autowired
    InfoMapper infoMapper;
    public ResultCode getLogin(String userName,
                               String passWord){
        ResultCode rc = new ResultCode();
        //此时我们首先需要查询数据库中有没有符合要求的数据
        List<String> check1 = new ArrayList<>();
        check1 = infoMapper.checkPassWordCorrect(userName,passWord);
        if(check1.size()!=1)
            rc.setResultCode(-1);

        return rc;
    }

    public ResultCode getSign(String userName,String passWord){
        ResultCode rc = new ResultCode();
        //此时我们首先需要查询数据库中有没有符合要求的账户名
        List<String> check1 = new ArrayList<>();
        check1 = infoMapper.checkPassWordCorrect(userName,passWord);
        if(check1.size()!=0){
            rc.setResultCode(-1);
            return rc;
        }
        infoMapper.insertNewAccount(userName, passWord);
        //这时候直接插入新值就好
        return rc;
    }
}
