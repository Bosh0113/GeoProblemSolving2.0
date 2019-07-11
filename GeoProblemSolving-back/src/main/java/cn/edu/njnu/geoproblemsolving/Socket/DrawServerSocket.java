package cn.edu.njnu.geoproblemsolving.Socket;

import cn.edu.njnu.geoproblemsolving.Config.GetHttpSessionConfigurator;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/DrawServer/{roomId}", configurator = GetHttpSessionConfigurator.class)
public class DrawServerSocket {

    private Session session=null;
    private static final Map<String,Map<String,DrawServerSocket>> rooms =new ConcurrentHashMap<>();

    private static JSONObject drawingJson=new JSONObject();
    private static ArrayList<String> drawingArray = new ArrayList<>(); //信息缓存，用来发给新加入者

    //定义了当一个新用户连接成功后所调用的方法
    @OnOpen
    public void onOpen(@PathParam("roomId") String roomId, Session session,EndpointConfig config)
    {
        this.session=session;
        HttpSession httpSession=(HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        if (!rooms.containsKey(roomId)){
            Map<String,DrawServerSocket> room=new ConcurrentHashMap<>();
            room.put(httpSession.getAttribute("userId").toString(), this);
            rooms.put(roomId,room);
        }
        else {
            rooms.get(roomId).put(httpSession.getAttribute("userId").toString(), this);
        }

        if (drawingJson.containsKey(roomId)) {
            try {
                session.getBasicRemote().sendText(drawingJson.get(roomId).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        broadcastMembersToRoom(roomId);

    }
    //接收消息后所调用的方法
    @OnMessage
    public void onMessage(@PathParam("roomId") String roomId, String message)
    {
        JSONObject messageObject = JSONObject.parseObject(message);
        String messageType = messageObject.getString("type");

        if (!messageType.equals("ping")) {
            if (messageType.equals("drawing") || messageType.equals("next")) {
                // 添加消息至缓存
                drawingArray.add(message);
                drawingJson.put(roomId, drawingArray.toString());
            }
            else if(messageType.equals("clear")) {
                drawingArray.clear();
                drawingJson.put(roomId, drawingArray.toString());
            }
            else if(messageType.equals("last")) {
                if(drawingArray.size()>0){
                    drawingArray.remove(drawingArray.size()-1);
                }
                drawingJson.put(roomId, drawingArray.toString());
            }

            broadcastMessageToRoom(roomId, message);
        }
    }
    @OnClose
    public void onClose(@PathParam("roomId") String roomId)
    {
        for (Map.Entry<String, DrawServerSocket> server : rooms.get(roomId).entrySet()) {
            if (server.getValue().equals(this)) {
                rooms.get(roomId).remove(server.getKey());
                break;
            }
        }

        //最后一个人退出后清理消息缓存
        if(rooms.get(roomId).size()<1){
            drawingArray.clear();
            drawingJson.put(roomId, drawingArray.toString());
        }

         broadcastMembersToRoom(roomId);
    }
    @OnError
    public void onError(@PathParam("roomId") String roomId,Throwable error)
    {
        for (Map.Entry<String, DrawServerSocket> server : rooms.get(roomId).entrySet()) {
            if (server.getValue().equals(this)) {
                rooms.get(roomId).remove(server.getKey());
                break;
            }
        }

         broadcastMembersToRoom(roomId);
    }


    private void broadcastMembersToRoom(String roomId){
        ArrayList<String> members = new ArrayList<>();
        for (Map.Entry<String, DrawServerSocket> server : rooms.get(roomId).entrySet()) {
            members.add(server.getKey());
        }
        JSONObject messageObject = new JSONObject();
        messageObject.put("type", "members");
        messageObject.put("message", members.toString());
        String message = messageObject.toString();
        for (Map.Entry<String, DrawServerSocket> server : rooms.get(roomId).entrySet()) {
            try {
                server.getValue().session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void broadcastMessageToRoom(String roomId,String message){
        for (Map.Entry<String, DrawServerSocket> server : rooms.get(roomId).entrySet()) {
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

