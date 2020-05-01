package cn.edu.njnu.geoproblemsolving.Socket;

import cn.edu.njnu.geoproblemsolving.Config.MyEndPointConfigure;
import cn.edu.njnu.geoproblemsolving.Dao.MessageRecords.MessageRecordsDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.MessageRecordsEntity;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/ChatServer/{roomId}", configurator = MyEndPointConfigure.class)
public class ChatServerSocket {

    private static final Map<String, Map<String, Session>> rooms = new ConcurrentHashMap<>();//在线用户
    private static JSONObject messageJson = new JSONObject();
    private static ArrayList<String> messagesArray = new ArrayList<>(); //信息缓存，用来发给新加入者
    private static JSONObject members = new JSONObject();       // 在线成员

    @Autowired
    MongoTemplate mongoTemplate;

    @OnOpen
    public void onOpen(@PathParam("roomId") String roomId, Session session, EndpointConfig config) {

        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());

        String userId = httpSession.getAttribute("userId").toString();
        String userName = httpSession.getAttribute("userName").toString();

        if (rooms.containsKey(roomId)) {
            rooms.get(roomId).put(userId, session);
        } else {
            Map<String, Session> room = new ConcurrentHashMap<>();
            room.put(httpSession.getAttribute("userId").toString(), session);

            rooms.put(roomId, room);
        }

        if (messageJson.containsKey(roomId)) {
            try {
                session.getBasicRemote().sendText(messageJson.get(roomId).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JSONObject user = new JSONObject();
        user.put("userId",userId);
        user.put("userName",userName);
        members.put(session.getId(), user);

        broadcastMembersToRoom(roomId, userId);
        broadcastOnOffToRoom(roomId, userId, userName, "on");
    }

    //接收消息后所调用的方法
    @OnMessage
    public void onMessage(@PathParam("roomId") String roomId, String message, Session session) {

        JSONObject messageObject = JSONObject.parseObject(message);
        String messageType = messageObject.getString("type");

        if (messageType.equals("message")) {

            String userId = members.getJSONObject(session.getId()).getString("userId");
            broadcastMessageToRoom(roomId, message, userId);
            // 添加消息至缓存
            messagesArray.add(message);
            messageJson.put(roomId, messagesArray.toString());

            // 将历史消息进行存储
            try {
                MessageRecordsDaoImpl messageRecordsDao = new MessageRecordsDaoImpl(mongoTemplate);
                MessageRecordsEntity messageRecordsEntity = new MessageRecordsEntity();
                messageRecordsEntity.setRoomId(roomId);
                messageRecordsEntity.setUserId(messageObject.getString("fromid"));
                messageRecordsEntity.setContent(message);
                messageRecordsEntity.setType("message");
                messageRecordsDao.saveMessageRecords(messageRecordsEntity);
            } catch (Exception ex) {
                throw ex;
            }
        }
    }

    @OnClose
    public void onClose(@PathParam("roomId") String roomId, Session session) {

        for (Map.Entry<String, Session> server : rooms.get(roomId).entrySet()) {
            if (server.getValue().equals(session)) {
                rooms.get(roomId).remove(server.getKey());
                break;
            }
        }
        String userId = members.getJSONObject(session.getId()).getString("userId");
        String userName = members.getJSONObject(session.getId()).getString("userName");
        broadcastOnOffToRoom(roomId, userId, userName, "off");
        members.remove(session.getId());

        // 最后一个人退出后清理消息缓存
        if (rooms.get(roomId).size() < 1) {
            messagesArray.clear();
            messageJson.put(roomId, messagesArray.toString());
        }
    }

    @OnError
    public void onError(@PathParam("roomId") Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    //用户列表
    private void broadcastMembersToRoom(String roomId, String userId) {
        ArrayList<String> memberList = new ArrayList<>();
        for (Map.Entry<String, Session> server : rooms.get(roomId).entrySet()) {
            memberList.add(server.getKey());
        }
        JSONObject messageObject = new JSONObject();
        messageObject.put("type", "members");
        messageObject.put("content", memberList.toString());
        String message = messageObject.toString();

        for (Map.Entry<String, Session> server : rooms.get(roomId).entrySet()) {
//            if (userId.equals(server.getKey())) {
                try {
                    server.getValue().getBasicRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//            }
        }
    }

    //消息
    private void broadcastMessageToRoom(String roomId, String message, String userId) {
        for (Map.Entry<String, Session> server : rooms.get(roomId).entrySet()) {
            if (!userId.equals(server.getKey())) {
                try {
                    server.getValue().getBasicRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void broadcastOnOffToRoom(String roomId,String userId, String userName, String behavior) {

        JSONObject messageObject = new JSONObject();
        messageObject.put("type", "notice");
        messageObject.put("behavior", behavior);
        messageObject.put("userId", userId);
        if(behavior.equals("on")) {
            messageObject.put("content", userName + " enter this room");
        }
        else if(behavior.equals("off")){
            messageObject.put("content", userName + " leave this room");
        }

        String message = messageObject.toString();

        for (Map.Entry<String, Session> server : rooms.get(roomId).entrySet()) {
//            if (!userId.equals(server.getKey())) {
                try {
                    server.getValue().getBasicRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
//            }
        }
    }
}
