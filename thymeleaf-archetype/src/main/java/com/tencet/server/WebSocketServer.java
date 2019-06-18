package com.tencet.server;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ServerEndpoint("/websocket/{userId}")
@Component
public class WebSocketServer {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //创建一个线程安全的map
    private static Map<String, WebSocketServer> users = Collections.synchronizedMap(new HashMap());

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //放入map中的key,用来表示该连接对象
    private String username;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String username) {
        this.session=session;
        this.username =username;
        users.put(username, this);   //加入map中,为了测试方便使用username做key
        addOnlineCount();           //在线数加1
        log.info(username + "加入！当前在线人数为" + getOnlineCount());
        try {
            this.session.getBasicRemote().sendText("连接成功");
        } catch (IOException e) {
            log.error("websocket IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        users.remove(this.username);  //从set中删除
        subOnlineCount();             //在线数减1
        log.info("一个连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后触发的方法
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("来自客户端的消息:" + message);
        //群发消息
        try {
            if (StringUtils.isEmpty(message)){
                return ;
            }
            //如果给所有人发消息携带@ALL, 给特定人发消息携带@xxx@xxx#message
            String[] split = message.split("#");
            if (split.length > 1){
                String[] users = split[0].split("@");
                if (users.length < 2){return;}
                String firstuser = users[1].trim();
                if (StringUtils.isEmpty(firstuser) || "ALL".equalsIgnoreCase(firstuser.toUpperCase())){
                    String msg = username +": "+ split[1];
                    sendInfo(msg);//群发消息
                }else{//给特定人员发消息
                    for (String user : users) {
                        if (!StringUtils.isEmpty(user.trim())){
                            sendMessageToSomeBody(user.trim(), split[1]);
                        }
                    }
                }
            }else{
                sendInfo(username +": "+message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误 session: " + session);
        error.printStackTrace();
    }

//    给特定人员发送消息
    public void sendMessageToSomeBody(String username, String message) throws IOException {
        if(users.get(username) == null){
            return;
        }
        users.get(username).session.getBasicRemote().sendText(message);
        this.session.getBasicRemote().sendText(this.username + "@" + username + ": " + message);
    }

    /**
     * 群发自定义消息
     */
    public void sendInfo(String message) throws IOException {
        for (WebSocketServer item : users.values()) {
            try {
                item.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
