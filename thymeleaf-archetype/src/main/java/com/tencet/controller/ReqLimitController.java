package com.tencet.controller;

import com.tencet.common.RequestLimit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ReqLimitController {

    @RequestLimit(count = 5, time = 30*1000)
    @RequestMapping("/hello")
    @ResponseBody
    public String test(HttpServletRequest request){
        return "hello";
    }

}
