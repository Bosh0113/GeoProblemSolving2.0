package cn.edu.njnu.geoproblemsolving.business.collaboration.socket;

import cn.edu.njnu.geoproblemsolving.Config.GetHttpSessionConfigurator;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/NoticeSocket",configurator = GetHttpSessionConfigurator.class)
public class NoticeSocket {

    private Session session=null;
    private static final Map<String,NoticeSocket> servers=new ConcurrentHashMap<>();
    @OnOpen
    public void onOpen(Session session,EndpointConfig config)
    {
        HttpSession httpSession=(HttpSession)config.getUserProperties().get(HttpSession.class.getName());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowDate = dateFormat.format(new Date());
        System.out.println("Notice已连接："+"用户名-"+httpSession.getAttribute("name") + "-----" + "连接时间："+ nowDate);
        this.session=session;
        servers.put(httpSession.getAttribute("userId").toString(),this);
    }
    //接收消息后所调用的方法
    @OnMessage
    public void onMessage(String message)
    {
        try {
            if(!message.equals("ping")){
                for(Map.Entry<String,NoticeSocket> server:servers.entrySet()){
                    if(server.getKey().equals(message)){
                        servers.get(message).session.getBasicRemote().sendText("Notice");
                        break;
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @OnClose
    public void onClose()
    {
        String unConnectId="";
        for(Map.Entry<String,NoticeSocket> server:servers.entrySet()){
            if(server.getValue().equals(this)){
                unConnectId=server.getKey();
                servers.remove(server.getKey());
                break;
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowDate = dateFormat.format(new Date());
        System.out.println("有用户断开连接："+unConnectId + "-----" + "断开时间："+ nowDate);
        System.out.println("---------------------------------------------------------------------------");
    }
    @OnError
    public void onError(Throwable error)
    {
        String unConnectId="";
        for(Map.Entry<String,NoticeSocket> server:servers.entrySet()){
            if(server.getValue().equals(this)){
                unConnectId=server.getKey();
                servers.remove(server.getKey());
                break;
            }
        }
        System.out.println("有用户失去连接："+unConnectId);
    }
}
