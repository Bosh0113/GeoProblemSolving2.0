package cn.edu.njnu.geoproblemsolving.business.collaboration.socket;

import cn.edu.njnu.geoproblemsolving.Config.MyEndPointConfigure;
import cn.edu.njnu.geoproblemsolving.business.collaboration.cache.CommunicationCache;
import cn.edu.njnu.geoproblemsolving.business.collaboration.entity.ChatMsg;
import cn.edu.njnu.geoproblemsolving.business.collaboration.entity.CollaborationUser;
import cn.edu.njnu.geoproblemsolving.business.collaboration.enums.CollaborationMode;
import cn.edu.njnu.geoproblemsolving.business.collaboration.service.CollaborationService;
import cn.edu.njnu.geoproblemsolving.business.collaboration.utils.CollaborationBehavior;
import cn.edu.njnu.geoproblemsolving.business.collaboration.utils.CollaborationConfig;
import cn.edu.njnu.geoproblemsolving.business.tool.chatroom.chatmessage.ChatMessageRecordsService;
import cn.edu.njnu.geoproblemsolving.business.tool.chatroom.chatmessage.dto.AddChatMessageRecordsDTO;
import cn.edu.njnu.geoproblemsolving.business.tool.chatroom.hydrologicalconcept.AnsjSegService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author ：mzy
 * @Date ：2021/4/25
 * @version: 1.0.0
 */
@Component
@ServerEndpoint(value = "/OperationServer/{toolId}/{aid}", configurator = MyEndPointConfigure.class)
public class OperationSocketServer {

    @Autowired
    CollaborationService collaborationService;

    @OnOpen
    public void onOpen(@PathParam("toolId") String toolId, @PathParam("aid") String aid, Session session, EndpointConfig config) throws IOException {
        String groupKey = toolId + aid;
        collaborationService.operationStart(groupKey, session, config);
    }

    /**
     * operation content 1: type消息类型（members, test, collaboration-init, mode, control-apply, control-stop, operation, computation）
     * operation content 2: sender发送者, receivers接收者, behavior行为, object操作对象, participants在线参与者, waiting等待人数, operator当前操作者, mode协同模式
     * @param toolId
     * @param aid
     * @param message
     * @throws IOException
     */
    @OnMessage
    public void onMessage(@PathParam("toolId") String toolId, @PathParam("aid") String aid, String message) throws IOException {String groupKey = toolId + aid;
        collaborationService.operationTransfer(toolId, aid, message);
    }

    @OnClose
    public void onClose(@PathParam("toolId") String toolId, @PathParam("aid") String aid, Session session) throws IOException {
        String groupKey = toolId + aid;
        collaborationService.operationClose(groupKey, session);
    }

    @OnError
    public void onError(@PathParam("roomId") Throwable error) {
        System.out.println("Error!");
        error.printStackTrace();
    }

}
