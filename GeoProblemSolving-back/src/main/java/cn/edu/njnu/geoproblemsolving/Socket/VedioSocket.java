package cn.edu.njnu.geoproblemsolving.Socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @Author: David.Xiao
 * @Date: 2019/4/14 13:14
 * @Description:
 */
@ServerEndpoint(value = "/vedioSocket/{usrId}",configurator = ServerEndpointConfig.Configurator.class)
@Component
public class VedioSocket {

//    private static List<Session> sessionList = new ArrayList<>();
    private static Map<String, Session> onlineUsrs = new HashMap();
    private static int onlineCount = 0;

    @OnOpen
    public void onOpen(@PathParam("usrId") String usrId,Session session, EndpointConfig config)
    {
        addOnlineCount();
//        String usrId = UUID.randomUUID().toString();
        onlineUsrs.put(usrId,session);
//        sessionList.add(session);
        System.out.println("[用户上线] " + session.getId() + " 当前用户数量：" + onlineCount);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("type","usrId");
//        jsonObject.put("value",usrId);
//        return jsonObject.toJSONString();
    }

    @OnMessage
    public void onMessage(@PathParam("usrId") String usrId,Session session,String message)
    {
        for (String friendId:onlineUsrs.keySet()) {
            if (friendId != usrId)
            {
                System.out.println("[接收者]: " + session.getId() + " [消息]: " + message);
                System.out.println();
                sendText(friendId,session,message);
            }
        }
    }

    @OnClose
    public void onClose(@PathParam("usrId") String usrId,Session session)
    {
        subOnlineCount();
        onlineUsrs.remove(usrId);
        System.out.println("[WebSocket已关闭] " + "当前用户数量：" + onlineCount);
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @OnError
    public void onError(@PathParam("usrId") String usrId,Session session,Throwable error)
    {

        System.out.println("[发生错误] " + "当前用户数量：" + onlineCount);
        error.printStackTrace();
    }
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        onlineCount--;
    }

    void sendText(String friendId,Session session,String message){

        try {
            Thread.sleep(500);
            onlineUsrs.get(friendId).getBasicRemote().sendText(new String(message.getBytes("utf-8")));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
