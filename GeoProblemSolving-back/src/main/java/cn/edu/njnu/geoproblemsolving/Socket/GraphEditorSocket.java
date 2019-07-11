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
@ServerEndpoint(value = "/GraphEditorSocket/{groupID}", configurator = GetHttpSessionConfigurator.class)
public class GraphEditorSocket {
    private Session session = null;
    private static final Map<String, Map<String, GraphEditorSocket>> groups = new ConcurrentHashMap<>();
    private static final Map<String, ArrayList<String>> requireLists = new ConcurrentHashMap<>();
    private static JSONObject controllers = new JSONObject();
    private static JSONObject members = new JSONObject();
    private static JSONObject messageCache=new JSONObject();//信息缓存，用来发给新加入者

    @OnOpen
    public void onOpen(@PathParam("groupID") String groupID, Session session, EndpointConfig config) {
        this.session = session;
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        if (!groups.containsKey(groupID)) {
            Map<String, GraphEditorSocket> group = new ConcurrentHashMap<>();
            group.put(httpSession.getAttribute("userId").toString(), this);
            groups.put(groupID, group);
        } else {
            groups.get(groupID).put(httpSession.getAttribute("userId").toString(), this);
        }
        if (!requireLists.containsKey(groupID)) {
            ArrayList<String> list = new ArrayList<>();
            requireLists.put(groupID, list);
        }
        if (!messageCache.containsKey(groupID)){
            JSONObject messageObject=new JSONObject();
            messageObject.put("type","Message");
            messageObject.put("message","<mxGraphModel><root><mxCell id=\"0\"/><mxCell id=\"1\" parent=\"0\"/></root></mxGraphModel>");
            messageCache.put(groupID,messageObject.toString());
        }
        try {
            this.session.getBasicRemote().sendText(messageCache.get(groupID).toString());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @OnMessage
    public void onMessage(@PathParam("groupID") String groupID, String message) {
        JSONObject messageObject = JSONObject.parseObject(message);
        String messageType = messageObject.getString("type");
        switch (messageType) {
            case "Join":
                String userId = messageObject.getString("userId");
                String userName = messageObject.getString("userName");
                if (groups.get(groupID).size() < 2) {
                    controllers.put(groupID, userId);
                }
                members.put(userId, userName);
                //广播在线成员、请求队列和演示者
                publicMembers(groupID);
                break;
            case "Authority":
                if (messageObject.get("message").equals("Require")) {
                    requireLists.get(groupID).add(messageObject.getString("userId"));
                    if (controllers.get(groupID).equals("7014115d-2054-4c5e-99ed-ed786574cd32")) {
                        ReGrant(groupID);
                    } else {
                        publicMembers(groupID);
                    }
                } else if (messageObject.get("message").equals("Release")) {
                    ReGrant(groupID);
                }
                break;
            case "Message":
                messageCache.put(groupID,message);
                try {
                    for (Map.Entry<String, GraphEditorSocket> server : groups.get(groupID).entrySet()) {
                        if (!server.getValue().equals(this)) {
                            server.getValue().session.getBasicRemote().sendText(message);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @OnClose
    public void onClose(@PathParam("groupID") String groupID) {
        String nowUserId="";
        for (Map.Entry<String,GraphEditorSocket> server: groups.get(groupID).entrySet()){
            if (server.getValue().equals(this)){
                nowUserId=server.getKey();
                break;
            }
        }
        groups.get(groupID).remove(nowUserId);
        if (controllers.get(groupID).equals(nowUserId)){
            ReGrant(groupID);
        }
        //最后一个人退出后清理消息缓存
        if(groups.get(groupID).size()<1){
            JSONObject messageObject=new JSONObject();
            messageObject.put("type","Message");
            messageObject.put("message","<mxGraphModel><root><mxCell id=\"0\"/><mxCell id=\"1\" parent=\"0\"/></root></mxGraphModel>");
            messageCache.put(groupID,messageObject.toString());
        }
    }

    @OnError
    public void onError(Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    private void publicMembers(@PathParam("groupID") String groupID) {
        JSONObject messageObject=new JSONObject();
        ArrayList<String> mIds=new ArrayList<>();
        ArrayList<String> mNames=new ArrayList<>();
        for (Map.Entry<String,GraphEditorSocket> server:groups.get(groupID).entrySet()){
            mIds.add(server.getKey());
            mNames.add(members.getString(server.getKey()));
        }
        messageObject.put("type","Members");
        messageObject.put("controllerId",controllers.get(groupID));
        messageObject.put("requireList",requireLists.get(groupID).toString());
        messageObject.put("mIds",mIds.toString());
        messageObject.put("mNames",mNames.toString());
        try {
            for (Map.Entry<String,GraphEditorSocket> server:groups.get(groupID).entrySet()){
                server.getValue().session.getBasicRemote().sendText(messageObject.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void ReGrant(String groupID){
        if(!requireLists.get(groupID).isEmpty()){
            String firstUserId=requireLists.get(groupID).get(0);
            boolean exist=false;
            for (Map.Entry<String,GraphEditorSocket> server:groups.get(groupID).entrySet()){
                if (firstUserId.equals(server.getKey())){
                    exist=true;
                    break;
                }
            }
            if (exist){
                controllers.put(groupID,requireLists.get(groupID).get(0));
                requireLists.get(groupID).remove(0);
                publicMembers(groupID);
            }
        }
        else {
            controllers.put(groupID,"7014115d-2054-4c5e-99ed-ed786574cd32");
            publicMembers(groupID);
        }
    }
}
