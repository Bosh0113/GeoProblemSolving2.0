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
@ServerEndpoint(value = "/Step/{stepId}", configurator = MyEndPointConfigure.class)

public class StepSocket {
    private Session session = null;
    private static final Map<String, Map<String, Session>> modules = new ConcurrentHashMap<>();

    @Autowired
    MongoTemplate mongoTemplate;

    @OnOpen
    public void onOpen(@PathParam("stepId") String stepId, Session session, EndpointConfig config) {
//        this.session = session;
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        if (!modules.containsKey(stepId)) {
            Map<String, Session> step = new ConcurrentHashMap<>();
            step.put(httpSession.getAttribute("userId").toString(), session);
            modules.put(stepId, step);
        } else {
            modules.get(stepId).put(httpSession.getAttribute("userId").toString(), session);
        }
        broadcastOnlineToModule(stepId,httpSession.getAttribute("userId").toString());
        broadcastMembersToModule(stepId);
    }

    @OnMessage
    public void onMessage(@PathParam("stepId") String stepId, String message, Session session) {
        JSONObject messageObject = JSONObject.parseObject(message);
        String messageType = messageObject.getString("type");
        if (!(messageType.equals("ping"))) {
            broadcastMessageToModule(stepId, message, session);
            // 将资源记录和工具记录存储
            try {
                if(messageType.equals("resource") || messageType.equals("tools")) {

                    HistoryEventDaoImpl historyEventDao = new HistoryEventDaoImpl(mongoTemplate);
                    HistoryEventEntity historyEventEntity = new HistoryEventEntity();
                    historyEventEntity.setScopeId(stepId);
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
    public void onClose(@PathParam("stepId") String stepId, Session session) {
        for (Map.Entry<String, Session> server : modules.get(stepId).entrySet()) {
            if (server.getValue().equals(session)) {
                broadcastOfflineToModule(stepId,server.getKey());
                modules.get(stepId).remove(server.getKey());
                break;
            }
        }
        broadcastMembersToModule(stepId);
    }

    @OnError
    public void onError(@PathParam("stepId") String stepId, Session session, Throwable error) {
        for (Map.Entry<String, Session> server : modules.get(stepId).entrySet()) {
            if (server.getValue().equals(session)) {
                broadcastOfflineToModule(stepId,server.getKey());
                modules.get(stepId).remove(server.getKey());
                break;
            }
        }
        broadcastMembersToModule(stepId);
    }

    private void broadcastMembersToModule(String stepId) {
        ArrayList<String> members = new ArrayList<>();
        for (Map.Entry<String, Session> server : modules.get(stepId).entrySet()) {
            members.add(server.getKey());
        }
        JSONObject messageObject = new JSONObject();
        messageObject.put("type", "members");
        messageObject.put("message", members.toString());
        String message = messageObject.toString();
        for (Map.Entry<String, Session> server : modules.get(stepId).entrySet()) {
            try {
                server.getValue().getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcastMessageToModule(String stepId, String message, Session session) {
        for (Map.Entry<String, Session> server : modules.get(stepId).entrySet()) {
            if (!session.equals(server.getValue())) {
                try {
                    server.getValue().getBasicRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void broadcastOnlineToModule(String stepId, String userId){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject messageObject = new JSONObject();
        messageObject.put("type", "online");
        messageObject.put("userId",userId);
        messageObject.put("createTime",dateFormat.format(date));
        String message = messageObject.toString();
        for (Map.Entry<String, Session> server : modules.get(stepId).entrySet()) {
            try {
                server.getValue().getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void broadcastOfflineToModule(String stepId, String userId){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject messageObject = new JSONObject();
        messageObject.put("type", "offline");
        messageObject.put("userId",userId);
        messageObject.put("createTime",dateFormat.format(date));
        String message = messageObject.toString();
        for (Map.Entry<String, Session> server : modules.get(stepId).entrySet()) {
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
