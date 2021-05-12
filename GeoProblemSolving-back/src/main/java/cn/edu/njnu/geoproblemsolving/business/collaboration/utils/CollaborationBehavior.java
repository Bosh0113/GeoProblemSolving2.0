package cn.edu.njnu.geoproblemsolving.business.collaboration.utils;

import cn.edu.njnu.geoproblemsolving.business.collaboration.entity.ChatMsg;
import cn.edu.njnu.geoproblemsolving.business.collaboration.entity.MsgRecords;
import cn.edu.njnu.geoproblemsolving.business.collaboration.repository.MsgRecordsRepository;
import cn.edu.njnu.geoproblemsolving.business.collaboration.service.MsgRecordsService;
import cn.edu.njnu.geoproblemsolving.business.collaboration.utils.CollaborationConfig;
import cn.edu.njnu.geoproblemsolving.business.user.dao.IUserDao;
import cn.edu.njnu.geoproblemsolving.business.user.dto.InquiryUserDto;
import cn.edu.njnu.geoproblemsolving.business.collaboration.entity.CollaborationUser;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class CollaborationBehavior {

    @Autowired
    IUserDao iUserDao;

    @Autowired
    MsgRecordsService msgRecordsService;

    /**common**/
    public CollaborationUser getMemberInfo(String userId, Session session) {

        JsonResult userInfo = iUserDao.getUserInfo("userId", userId);

        CollaborationUser collaborationUser = new CollaborationUser();
        collaborationUser.setUserId(userId);
        collaborationUser.setName(((InquiryUserDto) userInfo.getData()).getName());
        collaborationUser.setEmail(((InquiryUserDto) userInfo.getData()).getEmail());
        collaborationUser.setEmail(((InquiryUserDto) userInfo.getData()).getAvatar());
        collaborationUser.setSession(session);

        return collaborationUser;
    }

    public void sendCollaborationStatus(CollaborationConfig config, Session session) {
        try {
            JSONObject messageObject = new JSONObject();
            messageObject.put("type", "collaboration-init");
            messageObject.put("mode", config.getMode().toString());
            messageObject.put("operator", config.getOperator());
            messageObject.put("waiting", config.getApplyQueue().size());
            session.getBasicRemote().sendText(messageObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendParticipantsInfo(HashMap<String, CollaborationUser> participants, CollaborationUser user, String behavior) {
        try {
            if(user == null) return;

            JSONObject messageObject = new JSONObject();
            messageObject.put("type", "members");
            messageObject.put("participants", participants);

            for (Map.Entry<String, CollaborationUser> participant : participants.entrySet()) {
                CollaborationUser receiver = participant.getValue();
                if (!receiver.getUserId().equals(user.getUserId())) {
                    messageObject.put("behavior", behavior);
                    messageObject.put("activeUser", user);
                }
                receiver.getSession().getBasicRemote().sendText(messageObject.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**Message**/
    public void sendMessageCache(ArrayList<ChatMsg> msgRecords, Session session) {
        try {
            JSONObject messageObject = new JSONObject();
            messageObject.put("type", "message-cache");
            messageObject.put("content", msgRecords);
            session.getBasicRemote().sendText(messageObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void transferMessage(String type, HashMap<String, CollaborationUser> participants, CollaborationUser sender, List<String> receivers, String message, String time) {
        try {
            JSONObject messageObject = new JSONObject();
            messageObject.put("type", type);
            messageObject.put("sender", sender);
            messageObject.put("content", message);
            messageObject.put("time", time);

            if (receivers.size() == 0) {
                messageObject.put("receivers", "");
                // send message
                for (Map.Entry<String, CollaborationUser> user : participants.entrySet()) {
                    user.getValue().getSession().getBasicRemote().sendText(messageObject.toString());
                }
            } else {
                messageObject.put("receivers", receivers);
                // send message
                for (String receiver : receivers) {
                    for (Map.Entry<String, CollaborationUser> user : participants.entrySet()) {
                        if (user.getValue().getUserId().equals(receiver)) {
                            user.getValue().getSession().getBasicRemote().sendText(messageObject.toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 将聊天记录按时段存储
    public void msgCacheStore(String aid, ArrayList<ChatMsg> records) {
        if(records != null &&records.size() > 0) {
            msgRecordsService.msgRecordsCreate(aid, records);
        }
    }


    /**operation**/
    public void transferOperation(HashMap<String, CollaborationUser> participants, String sender, List<String> receivers, String behavior, String object) {
        try {
            JSONObject messageObject = new JSONObject();
            messageObject.put("type", "operation");
            messageObject.put("sender", sender);
            messageObject.put("behavior", behavior);
            messageObject.put("object", object);

            if (receivers.size() == 0) {
                messageObject.put("receivers", "");
                // send message
                for (Map.Entry<String, CollaborationUser> user : participants.entrySet()) {
                    user.getValue().getSession().getBasicRemote().sendText(messageObject.toString());
                }
            } else {
                messageObject.put("receivers", receivers);
                // send message
                for (String receiever : receivers) {
                    for (Map.Entry<String, CollaborationUser> user : participants.entrySet()) {
                        if (user.getValue().getUserId().equals(receiever)) {
                            user.getValue().getSession().getBasicRemote().sendText(messageObject.toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void operationRefuse(HashMap<String, CollaborationUser> participants, String sender) {
        try {
            JSONObject messageObject = new JSONObject();
            messageObject.put("type", "operation");
            messageObject.put("behavior", "Refuse");

            // send message
            for (Map.Entry<String, CollaborationUser> user : participants.entrySet()) {
                if (user.getValue().getUserId().equals(sender)) {
                    user.getValue().getSession().getBasicRemote().sendText(messageObject.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送协同模式
     *
     * @param participants
     * @param mode
     */
    public void sendModeType(HashMap<String, CollaborationUser> participants, String mode) {
        try {
            for (Map.Entry<String, CollaborationUser> participant : participants.entrySet()) {
                JSONObject messageObject = new JSONObject();
                messageObject.put("type", "mode");
                messageObject.put("content", mode);

                participant.getValue().getSession().getBasicRemote().sendText(messageObject.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送控制信息
     *
     * @param participants
     * @param queue
     * @param user
     * @param behavior
     */
    public void sendControlInfo(HashMap<String, CollaborationUser> participants, List<String> queue, String user, String behavior) {
        try {
            JSONObject messageObject = new JSONObject();
            messageObject.put("type", "message");
            messageObject.put("behavior", behavior);

            if (behavior.equals("apply")) {
                messageObject.put("sender", user);
                messageObject.put("waiting", queue.size());
                // send message
                for (Map.Entry<String,CollaborationUser> participant : participants.entrySet()) {
                    participant.getValue().getSession().getBasicRemote().sendText(messageObject.toString());
                }
            } else if (behavior.equals("stop")) {
                messageObject.put("operator", user);
                // send message
                for (Map.Entry<String,CollaborationUser> participant : participants.entrySet()) {
                    for (int i = 0; i < queue.size(); i++) {
                        if (queue.get(i).equals(participant.getValue().getUserId())) {
                            messageObject.put("waiting", i);
                            participant.getValue().getSession().getBasicRemote().sendText(messageObject.toString());
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}



