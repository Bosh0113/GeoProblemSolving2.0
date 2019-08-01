package cn.edu.njnu.geoproblemsolving.Socket;

import cn.edu.njnu.geoproblemsolving.Config.GetHttpSessionConfigurator;
import cn.edu.njnu.geoproblemsolving.Config.MyEndPointConfigure;
import cn.edu.njnu.geoproblemsolving.Dao.MessageRecords.MessageRecordsDaoImpl;
import cn.edu.njnu.geoproblemsolving.Dao.MessageRecords.MessageRecordsDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.MessageRecordsEntity;
import com.alibaba.fastjson.JSONObject;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;
import org.apache.log4j.Logger;

import javax.mail.Message;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import static cn.edu.njnu.geoproblemsolving.Socket.VedioSocket.*;

@Component
@ServerEndpoint(value = "/ChatServer/{roomId}", configurator = MyEndPointConfigure.class)
public class ChatServerSocket {

    private static final Map<String,Map<String,Session>> rooms=new ConcurrentHashMap<>();//在线用户
    private static JSONObject messageJson=new JSONObject();
    private static ArrayList<String> messagesArray = new ArrayList<>(); //信息缓存，用来发给新加入者

    private static int onlineCount = 0; //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
   // private static CopyOnWriteArraySet<ChatServerSocket> webSocketSet = new CopyOnWriteArraySet<ChatServerSocket>();
    private Session session;    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private HttpSession httpSession;    //request的session
    private String userid;      //用户名
    private static List list = new ArrayList<>();   //在线列表,记录用户名称
    private static Map routetab = new HashMap<>();  //用户名和websocket的session绑定的路由表


    @Autowired
    MongoTemplate mongoTemplate;

    @OnOpen
    public void onOpen(@PathParam("roomId") String roomId, Session session,EndpointConfig config)
    {
//        this.session = session;
//        addOnlineCount();//在线数加1;
//        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
//        this.userid=(String) httpSession.getAttribute("userId");    //获取当前用户
//        list.add(userid);  //将用户名加入在线列表
//        routetab.put(userid, session);   //将用户名和session绑定到路由表
//        String message = getMessage( userid ,"notice",  list,getOnlineCount());

        HttpSession httpSession=(HttpSession) config.getUserProperties().get(HttpSession.class.getName());

        if (!rooms.containsKey(roomId)){
            Map<String,Session> room=new ConcurrentHashMap<>();
            room.put(httpSession.getAttribute("userId").toString(), session);
            rooms.put(roomId,room);
        }
        else {
            rooms.get(roomId).put(httpSession.getAttribute("userId").toString(), session);
        }
        //broadcast(roomId,message);     //广播
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
        subOnlineCount();           //在线数减1
        list.remove(userid);        //从在线列表移除这个用户
        routetab.remove(userid);
        for (Map.Entry<String, Session> server : rooms.get(roomId).entrySet()) {
            if (server.getValue().equals(session)) {
                rooms.get(roomId).remove(server.getKey());
                break;
            }
        }
        String message = getMessage( userid ,"notice", list , getOnlineCount());
        broadcast(roomId,message);         //广播
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

    //用户列表
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

    //消息
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


    public void broadcast(String roomId,String message){
//        for(ChatServerSocket chat: webSocketSet){
//            try {
//                chat.session.getBasicRemote().sendText(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//                continue;
//            }
//        }
        for (Map.Entry<String, Session> server : rooms.get(roomId).entrySet()) {
            try {
                server.getValue().getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }


    }

    public String getMessage(String message, String type, List list,Integer number){
        JSONObject member = new JSONObject();
        member.put("message", message);
        member.put("type", type);
        member.put("list", list);
        member.put("number", number);
        return member.toString();
    }

    public  int getOnlineCount() {
        return onlineCount;
    }

    public  void addOnlineCount() {
        ChatServerSocket.onlineCount++;
    }

    public  void subOnlineCount() {
        ChatServerSocket.onlineCount--;
    }


}
