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
@ServerEndpoint(value = "/VideoChatServer/{roomId}", configurator = GetHttpSessionConfigurator.class)
public class VideoChatSocket {

    private Session session=null;
    private static final Map<String,Map<String,VideoChatSocket>> rooms =new ConcurrentHashMap<>();

    //定义了当一个新用户连接成功后所调用的方法
    @OnOpen
    public void onOpen(Session session)
    {
        this.session=session;

    }
    //接收消息后所调用的方法
    @OnMessage
    public void onMessage(@PathParam("roomId") String roomId, String message) {
        JSONObject messageObject = JSONObject.parseObject(message);
        String type = messageObject.getString("type");
        if(type.equals("connect")){
            if (!rooms.containsKey(roomId)){
                Map<String,VideoChatSocket> room=new ConcurrentHashMap<>();
                room.put(messageObject.getString("userId"), this);
                rooms.put(roomId,room);
            }
            else {
                rooms.get(roomId).put(messageObject.getString("userId"), this);
            }
            broadcastMembersToRoom(roomId);
        }else {
            String msgToId = messageObject.getString("to");
            for (Map.Entry<String, VideoChatSocket> server : rooms.get(roomId).entrySet()) {
                if (msgToId.equals(server.getKey())) {
                    try {
                        server.getValue().session.getBasicRemote().sendText(message);
                    } catch (Exception ignored) {
                    }
                    break;
                }
            }
        }
    }
    @OnClose
    public void onClose(@PathParam("roomId") String roomId)
    {
        for (Map.Entry<String, VideoChatSocket> server : rooms.get(roomId).entrySet()) {
            if (server.getValue().equals(this)) {
                rooms.get(roomId).remove(server.getKey());
                break;
            }
        }
        broadcastMembersToRoom(roomId);
    }
    @OnError
    public void onError(@PathParam("roomId") String roomId,Throwable error)
    {
        for (Map.Entry<String, VideoChatSocket> server : rooms.get(roomId).entrySet()) {
            if (server.getValue().equals(this)) {
                rooms.get(roomId).remove(server.getKey());
                break;
            }
        }
        broadcastMembersToRoom(roomId);
    }


    private void broadcastMembersToRoom(String roomId){
        ArrayList<String> members = new ArrayList<>();
        for (Map.Entry<String, VideoChatSocket> server : rooms.get(roomId).entrySet()) {
            members.add(server.getKey());
        }
        JSONObject messageObject = new JSONObject();
        messageObject.put("type", "members");
        messageObject.put("message", members.toString());
        String message = messageObject.toString();
        for (Map.Entry<String, VideoChatSocket> server : rooms.get(roomId).entrySet()) {
            try {
                server.getValue().session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

