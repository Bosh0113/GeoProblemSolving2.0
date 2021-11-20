package cn.edu.njnu.geoproblemsolving.business.collaboration.socket;

import cn.edu.njnu.geoproblemsolving.Config.GetHttpSessionConfigurator;
import cn.edu.njnu.geoproblemsolving.Config.MyEndPointConfigure;
import cn.edu.njnu.geoproblemsolving.Config.SpringUtil;
import cn.edu.njnu.geoproblemsolving.business.collaboration.service.CollaborationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @Author ：mzy
 * @Date ：2021/4/25
 * @version: 1.0.0
 */
@Component
@ServerEndpoint(value = "/MsgServer/{aid}", configurator = GetHttpSessionConfigurator.class)
public class MsgSocketServer {

    // @Autowired
    // CollaborationService collaborationService;
    private static CollaborationService collaborationService;

    @Autowired
    public void setCollaborationService(CollaborationService collaborationService){
        MsgSocketServer.collaborationService = collaborationService;
    }


    @OnOpen
    public void onOpen(@PathParam("aid") String aid, Session session, EndpointConfig config) {
        collaborationService.msgStart(aid, session, config);
        // System.out.println("----Message onopen----");
    }

    /**
     * message content: type消息类型（members, message, test, message-cache, message-store）, sender发送者, receivers接收者, content消息内容, participants在线参与者, time时间
     * @param aid
     * @param message
     * @throws IOException
     */
    @OnMessage
    public void onMessage(@PathParam("aid") String aid, String message) {
        collaborationService.msgTransfer(aid, message);
    }

    @OnClose
    public void onClose(@PathParam("aid") String aid, Session session) {
        collaborationService.communicationClose(aid, session);
        // System.out.println("----Message Onclose----");
    }

    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }
}
