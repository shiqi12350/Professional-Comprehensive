package com.example.login.controller;

import com.example.login.entites.ResultCode;
import com.example.login.server.BasicServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Autowired
    BasicServer basicServer;

    @GetMapping("/test")
    public String Test(){
        return "OK";
    }

    @GetMapping("/login")
    public ResultCode getLogin(@RequestParam("userName")String userName,
                               @RequestParam("passWord")String passWord){
        return basicServer.getLogin(userName,passWord);
    }

    @GetMapping("/sign")
    public ResultCode getSign(@RequestParam("userName")String userName,
                               @RequestParam("passWord")String passWord){
        return basicServer.getSign(userName,passWord);
    }

}
