package com.tencet.controller;

import com.tencet.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class WebSocketController {

    @Autowired
    WebSocketServer server;

    @GetMapping("/websocket.html")
    public String toPage(){
        return "websocket";
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(String username, String password) throws IOException {
        //TODO: 校验密码
        server.sendInfo(username + "进入了聊天室!");
        return username;
    }

}