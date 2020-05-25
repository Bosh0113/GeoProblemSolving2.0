package cn.edu.njnu.geoproblemsolving.Socket.VideoChatSignalSocket;

import cn.edu.njnu.geoproblemsolving.Config.GetHttpSessionConfigurator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
@ServerEndpoint(value = "/VideoChatSignalSocket/{roomId}", configurator = GetHttpSessionConfigurator.class)
public class VideoChatSignalSocket {

    private Session session=null;
    private static final Map<String, Map<String, VideoChatSignalSocket>> rooms_online =new ConcurrentHashMap<>();
    private static final Map<String, Map<String, VideoChatSignalSocket>> rooms_chat =new ConcurrentHashMap<>();
    private static final JSONObject memberInfos = new JSONObject();

    @OnOpen
    public void onOpen(@PathParam("roomId") String roomId, Session session){
        this.session=session;
        if (!rooms_online.containsKey(roomId)){
            Map<String, VideoChatSignalSocket> room=new ConcurrentHashMap<>();
            rooms_online.put(roomId,room);
        }
        if (!rooms_chat.containsKey(roomId)){
            Map<String, VideoChatSignalSocket> room=new ConcurrentHashMap<>();
            rooms_chat.put(roomId,room);
        }
    }

    @OnMessage
    public void onMessage(@PathParam("roomId") String roomId, String message){
        VCSMessage messageObject = JSON.parseObject(message, VCSMessage.class);
        String messageType = messageObject.getType();
        JSONObject userInfo = messageObject.getUserInfo();
        String userId =userInfo.getString("userId");
        switch (messageType){
            case "connect":{
                memberInfos.put(userId, userInfo);
                rooms_online.get(roomId).put(userId, this);
                broadcastMembersToRoom(roomId);
                break;
            }
            case "apply":{
                rooms_chat.get(roomId).put(userId, this);
                broadcastMembersToRoom(roomId);
                broadcastMessageToChatRoom(roomId, message);
                break;
            }
            case "candidate":{
                broadcastMessageToChatRoom(roomId, message);
                break;
            }
            case "reply":{
                JSONObject messageContent = messageObject.getContent();
                String toUserId = messageContent.getString("replyTo");
                broadcastMessageToSomeone(roomId, toUserId,message);
                break;
            }
            case "quit":{
                someoneQuitChat(roomId, userId);
                broadcastMessageToChatRoom(roomId, message);
                broadcastMembersToRoom(roomId);
            }
        }
    }

    @OnClose
    public void onClose(@PathParam("roomId") String roomId)
    {
        someoneOffline(roomId);
    }

    @OnError
    public void onError(@PathParam("roomId") String roomId, Throwable error)
    {
        someoneOffline(roomId);
    }

    private void broadcastMembersToRoom(String roomId){
        ArrayList<JSONObject> members = new ArrayList<>();
        for (Map.Entry<String, VideoChatSignalSocket> server : rooms_online.get(roomId).entrySet()) {
            members.add(new JSONObject((JSONObject) memberInfos.get(server.getKey())) );
        }
        ArrayList<JSONObject> chats = new ArrayList<>();
        for (Map.Entry<String, VideoChatSignalSocket> server : rooms_chat.get(roomId).entrySet()) {
            chats.add(new JSONObject((JSONObject) memberInfos.get(server.getKey())) );
        }
        JSONObject messageObject = new JSONObject();
        messageObject.put("type", "members");
        messageObject.put("memberList", members);
        messageObject.put("chatList", chats);
        for (Map.Entry<String, VideoChatSignalSocket> server : rooms_online.get(roomId).entrySet()) {
            try {
                server.getValue().session.getBasicRemote().sendText(messageObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void broadcastMessageToSomeone(String roomId, String toUserId, String message){
        for (Map.Entry<String, VideoChatSignalSocket> server : rooms_online.get(roomId).entrySet()) {
            if (toUserId.equals(server.getKey())) {
                try {
                    server.getValue().session.getBasicRemote().sendText(message);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void broadcastMessageToChatRoom(String roomId,String message){
        for (Map.Entry<String, VideoChatSignalSocket> server : rooms_chat.get(roomId).entrySet()) {
            if (!this.equals(server.getValue())) {
                try {
                    server.getValue().session.getBasicRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void someoneQuitChat(String roomId, String userId){
        for (Map.Entry<String, VideoChatSignalSocket> server : rooms_chat.get(roomId).entrySet()) {
            if (server.getKey().equals(userId)) {
                rooms_chat.get(roomId).remove(server.getKey());
                break;
            }
        }
    }

    private void someoneOffline(String roomId){
        String userId = "";
        for (Map.Entry<String, VideoChatSignalSocket> server : rooms_chat.get(roomId).entrySet()) {
            if (server.getValue().equals(this)) {
                userId = server.getKey();
                rooms_chat.get(roomId).remove(server.getKey());
                break;
            }
        }
        for (Map.Entry<String, VideoChatSignalSocket> server : rooms_online.get(roomId).entrySet()) {
            if (server.getValue().equals(this)) {
                memberInfos.remove(server.getKey());
                rooms_online.get(roomId).remove(server.getKey());
                break;
            }
        }
        broadcastMembersToRoom(roomId);
        JSONObject quitMessage = new JSONObject();
        quitMessage.put("type","quit");
        JSONObject userInfo = new JSONObject();
        userInfo.put("userId",userId);
        quitMessage.put("userInfo",userInfo);
        broadcastMessageToChatRoom(roomId,quitMessage.toJSONString());
    }
}
