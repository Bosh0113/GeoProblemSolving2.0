package cn.edu.njnu.geoproblemsolving.Socket;

import cn.edu.njnu.geoproblemsolving.Config.GetHttpSessionConfigurator;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/3DviewerServer/{roomId}", configurator = GetHttpSessionConfigurator.class)
public class ThreejsServerSocket {

    private Session session=null;
    private static final Map<String,Map<String,ThreejsServerSocket>> rooms =new ConcurrentHashMap<>();

    //定义了当一个新用户连接成功后所调用的方法
    @OnOpen
    public void onOpen(@PathParam("roomId") String roomId, Session session,EndpointConfig config)
    {
        this.session=session;
        HttpSession httpSession=(HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        if (!rooms.containsKey(roomId)){
            Map<String,ThreejsServerSocket> room=new ConcurrentHashMap<>();
            room.put(httpSession.getAttribute("userId").toString(), this);
            rooms.put(roomId,room);
        }
        else {
            rooms.get(roomId).put(httpSession.getAttribute("userId").toString(), this);
        }
//        broadcastMembersToRoom(roomId);

    }
    //接收消息后所调用的方法
    @OnMessage
    public void onMessage(@PathParam("roomId") String roomId, String message)
    {
        broadcastMessageToRoom(roomId, message);
    }
    @OnClose
    public void onClose(@PathParam("roomId") String roomId)
    {
        for (Map.Entry<String, ThreejsServerSocket> server : rooms.get(roomId).entrySet()) {
            if (server.getValue().equals(this)) {
                rooms.get(roomId).remove(server.getKey());
                break;
            }
        }
//        broadcastMembersToRoom(roomId);
    }
    @OnError
    public void onError(@PathParam("roomId") String roomId,Throwable error)
    {
        for (Map.Entry<String, ThreejsServerSocket> server : rooms.get(roomId).entrySet()) {
            if (server.getValue().equals(this)) {
                rooms.get(roomId).remove(server.getKey());
                break;
            }
        }
//        broadcastMembersToRoom(roomId);
    }


    private void broadcastMembersToRoom(String roomId){
        ArrayList<String> members = new ArrayList<>();
        for (Map.Entry<String, ThreejsServerSocket> server : rooms.get(roomId).entrySet()) {
            members.add(server.getKey());
        }
        JSONObject messageObject = new JSONObject();
        messageObject.put("type", "members");
        messageObject.put("message", members.toString());
        String message = messageObject.toString();
        for (Map.Entry<String, ThreejsServerSocket> server : rooms.get(roomId).entrySet()) {
            try {
                server.getValue().session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void broadcastMessageToRoom(String roomId,String message){
        for (Map.Entry<String, ThreejsServerSocket> server : rooms.get(roomId).entrySet()) {
            if (!this.equals(server.getValue())) {
                try {
                    server.getValue().session.getBasicRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

