package cn.edu.njnu.geoproblemsolving.Socket;

import cn.edu.njnu.geoproblemsolving.Config.GetHttpSessionConfigurator;
import cn.edu.njnu.geoproblemsolving.Config.MyEndPointConfigure;
import cn.edu.njnu.geoproblemsolving.Dao.MessageRecords.MessageRecordsDaoImpl;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/ChatServer/{roomId}", configurator = MyEndPointConfigure.class)
public class ChatServerSocket {

    private static final Map<String,Map<String,Session>> rooms=new ConcurrentHashMap<>();
    private static JSONObject messageJson=new JSONObject();
    private static ArrayList<String> messagesArray = new ArrayList<>(); //信息缓存，用来发给新加入者

    @Autowired
    MongoTemplate mongoTemplate;

    @OnOpen
    public void onOpen(@PathParam("roomId") String roomId, Session session,EndpointConfig config)
    {
        HttpSession httpSession=(HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        if (!rooms.containsKey(roomId)){
            Map<String,Session> room=new ConcurrentHashMap<>();
            room.put(httpSession.getAttribute("userId").toString(), session);
            rooms.put(roomId,room);
        }
        else {
            rooms.get(roomId).put(httpSession.getAttribute("userId").toString(), session);
        }
        broadcastMembersToRoom(roomId);

        if (messageJson.containsKey(roomId)) {
            try {
                session.getBasicRemote().sendText(messageJson.get(roomId).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //接收消息后所调用的方法
    @OnMessage
    public void onMessage(@PathParam("roomId") String roomId, String message, Session session)
    {
        JSONObject messageObject = JSONObject.parseObject(message);
        String messageType = messageObject.getString("type");
        if (messageType.equals("message")) {
            broadcastMessageToRoom(roomId, message, session);

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
            }
            catch (Exception ex) {
                throw ex;
            }
        }
    }
    @OnClose
    public void onClose(@PathParam("roomId") String roomId, Session session)
    {
        for (Map.Entry<String, Session> server : rooms.get(roomId).entrySet()) {
            if (server.getValue().equals(session)) {
                rooms.get(roomId).remove(server.getKey());
                break;
            }
        }
        broadcastMembersToRoom(roomId);

        //最后一个人退出后清理消息缓存
        if(rooms.get(roomId).size()<1){
            messagesArray.clear();
            messageJson.put(roomId, messagesArray.toString());
        }
    }
    @OnError
    public void onError(@PathParam("roomId") String roomId, Session session,Throwable error)
    {
        for (Map.Entry<String, Session> server : rooms.get(roomId).entrySet()) {
            if (server.getValue().equals(session)) {
                rooms.get(roomId).remove(server.getKey());
                break;
            }
        }
        broadcastMembersToRoom(roomId);
    }

    private void broadcastMembersToRoom(String roomId){
        ArrayList<String> members = new ArrayList<>();
        for (Map.Entry<String, Session> server : rooms.get(roomId).entrySet()) {
            members.add(server.getKey());
        }
        JSONObject messageObject = new JSONObject();
        messageObject.put("type", "members");
        messageObject.put("message", members.toString());
        String message = messageObject.toString();
        for (Map.Entry<String, Session> server : rooms.get(roomId).entrySet()) {
            try {
                server.getValue().getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void broadcastMessageToRoom(String roomId,String message, Session session){
        for (Map.Entry<String, Session> server : rooms.get(roomId).entrySet()) {
            if (!session.equals(server.getValue())) {
                try {
                    server.getValue().getBasicRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
