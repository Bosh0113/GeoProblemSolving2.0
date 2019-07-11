package cn.edu.njnu.geoproblemsolving.Socket;

import cn.edu.njnu.geoproblemsolving.Config.GetHttpSessionConfigurator;
import cn.edu.njnu.geoproblemsolving.Config.MyEndPointConfigure;
import cn.edu.njnu.geoproblemsolving.Dao.HistoryEvent.HistoryEventDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.HistoryEventEntity;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/Module/{moduleId}", configurator = MyEndPointConfigure.class)

public class ModuleSocket {
    private Session session = null;
    private static final Map<String, Map<String, Session>> modules = new ConcurrentHashMap<>();

    @Autowired
    MongoTemplate mongoTemplate;

    @OnOpen
    public void onOpen(@PathParam("moduleId") String moduleId, Session session, EndpointConfig config) {
//        this.session = session;
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        if (!modules.containsKey(moduleId)) {
            Map<String, Session> module = new ConcurrentHashMap<>();
            module.put(httpSession.getAttribute("userId").toString(), session);
            modules.put(moduleId, module);
        } else {
            modules.get(moduleId).put(httpSession.getAttribute("userId").toString(), session);
        }
        broadcastOnlineToModule(moduleId,httpSession.getAttribute("userId").toString());
        broadcastMembersToModule(moduleId);
    }

    @OnMessage
    public void onMessage(@PathParam("moduleId") String moduleId, String message, Session session) {
        JSONObject messageObject = JSONObject.parseObject(message);
        String messageType = messageObject.getString("type");
        if (!(messageType.equals("ping"))) {
            broadcastMessageToModule(moduleId, message, session);
            // 将资源记录和工具记录存储
            try {
                if(messageType.equals("resource") || messageType.equals("tools")) {

                    HistoryEventDaoImpl historyEventDao = new HistoryEventDaoImpl(mongoTemplate);
                    HistoryEventEntity historyEventEntity = new HistoryEventEntity();
                    historyEventEntity.setScopeId(moduleId);
                    historyEventEntity.setEventType("record");
                    historyEventEntity.setDescription(message);
                    historyEventDao.saveHistoryEvent(historyEventEntity);
                }
            }
            catch (Exception ex) {
                throw ex;
            }
        }
    }

    @OnClose
    public void onClose(@PathParam("moduleId") String moduleId, Session session) {
        for (Map.Entry<String, Session> server : modules.get(moduleId).entrySet()) {
            if (server.getValue().equals(session)) {
                broadcastOfflineToModule(moduleId,server.getKey());
                modules.get(moduleId).remove(server.getKey());
                break;
            }
        }
        broadcastMembersToModule(moduleId);
    }

    @OnError
    public void onError(@PathParam("moduleId") String moduleId, Session session, Throwable error) {
        for (Map.Entry<String, Session> server : modules.get(moduleId).entrySet()) {
            if (server.getValue().equals(session)) {
                broadcastOfflineToModule(moduleId,server.getKey());
                modules.get(moduleId).remove(server.getKey());
                break;
            }
        }
        broadcastMembersToModule(moduleId);
    }

    private void broadcastMembersToModule(String moduleId) {
        ArrayList<String> members = new ArrayList<>();
        for (Map.Entry<String, Session> server : modules.get(moduleId).entrySet()) {
            members.add(server.getKey());
        }
        JSONObject messageObject = new JSONObject();
        messageObject.put("type", "members");
        messageObject.put("message", members.toString());
        String message = messageObject.toString();
        for (Map.Entry<String, Session> server : modules.get(moduleId).entrySet()) {
            try {
                server.getValue().getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcastMessageToModule(String moduleId, String message, Session session) {
        for (Map.Entry<String, Session> server : modules.get(moduleId).entrySet()) {
            if (!session.equals(server.getValue())) {
                try {
                    server.getValue().getBasicRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void broadcastOnlineToModule(String moduleId, String userId){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        JSONObject messageObject = new JSONObject();
        messageObject.put("type", "online");
        messageObject.put("userId",userId);
        messageObject.put("createTime",dateFormat.format(date));
        String message = messageObject.toString();
        for (Map.Entry<String, Session> server : modules.get(moduleId).entrySet()) {
            try {
                server.getValue().getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void broadcastOfflineToModule(String moduleId, String userId){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        JSONObject messageObject = new JSONObject();
        messageObject.put("type", "offline");
        messageObject.put("userId",userId);
        messageObject.put("createTime",dateFormat.format(date));
        String message = messageObject.toString();
        for (Map.Entry<String, Session> server : modules.get(moduleId).entrySet()) {
            try {
                if(!server.getKey().equals(userId)){
                    server.getValue().getBasicRemote().sendText(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
