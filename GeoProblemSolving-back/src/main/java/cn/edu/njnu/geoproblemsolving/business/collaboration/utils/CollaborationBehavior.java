package cn.edu.njnu.geoproblemsolving.business.collaboration.utils;

import cn.edu.njnu.geoproblemsolving.business.collaboration.entity.ChatMsg;
import cn.edu.njnu.geoproblemsolving.business.collaboration.utils.CollaborationConfig;
import cn.edu.njnu.geoproblemsolving.business.user.dao.IUserDao;
import cn.edu.njnu.geoproblemsolving.business.user.dto.InquiryUserDto;
import cn.edu.njnu.geoproblemsolving.business.collaboration.entity.CollaborationUser;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class CollaborationBehavior {

    @Autowired
    IUserDao iUserDao;

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

    public void sendParticipantsInfo(ArrayList<CollaborationUser> participants, CollaborationUser user, String behavior) {
        try {
            JSONObject messageObject = new JSONObject();
            messageObject.put("type", "members");
            messageObject.put("participants", participants);

            for (CollaborationUser participant : participants) {

                if (!participant.getUserId().equals(user.getUserId())) {
                    messageObject.put("behavior", behavior);
                    messageObject.put("activeUser", user);
                }
                participant.getSession().getBasicRemote().sendText(messageObject.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void transferMessage(ArrayList<CollaborationUser> participants, String sender, List<String> receivers, String message) {
        try {
            JSONObject messageObject = new JSONObject();
            messageObject.put("type", "operation");
            messageObject.put("sender", sender);
            messageObject.put("content", message);

            if (receivers.size() == 0) {
                messageObject.put("receivers", "");
                // send message
                for (CollaborationUser user : participants) {
                    user.getSession().getBasicRemote().sendText(messageObject.toString());
                }
            } else {
                messageObject.put("receivers", receivers);
                // send message
                for (String receiever : receivers) {
                    for (CollaborationUser user : participants) {
                        if (user.getUserId().equals(receiever)) {
                            user.getSession().getBasicRemote().sendText(messageObject.toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void transferOperation(ArrayList<CollaborationUser> participants, String sender, List<String> receivers, String behavior, String object) {
        try {
            JSONObject messageObject = new JSONObject();
            messageObject.put("type", "operation");
            messageObject.put("sender", sender);
            messageObject.put("behavior", behavior);
            messageObject.put("object", object);

            if (receivers.size() == 0) {
                messageObject.put("receivers", "");
                // send message
                for (CollaborationUser user : participants) {
                    user.getSession().getBasicRemote().sendText(messageObject.toString());
                }
            } else {
                messageObject.put("receivers", receivers);
                // send message
                for (String receiever : receivers) {
                    for (CollaborationUser user : participants) {
                        if (user.getUserId().equals(receiever)) {
                            user.getSession().getBasicRemote().sendText(messageObject.toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送协同模式
     * @param participants
     * @param mode
     */
    public void sendModeType(ArrayList<CollaborationUser> participants, String mode) {
        try {
            for (CollaborationUser participant : participants) {
                JSONObject messageObject = new JSONObject();
                messageObject.put("type", "mode");
                messageObject.put("content", mode);

                participant.getSession().getBasicRemote().sendText(messageObject.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送控制信息
     * @param participants
     * @param queue
     * @param user
     * @param behavior
     */
    public void sendControlInfo(ArrayList<CollaborationUser> participants, List<String> queue, String user, String behavior) {
        try {
            JSONObject messageObject = new JSONObject();
            messageObject.put("type", "message");
            messageObject.put("behavior", behavior);

            if(behavior.equals("apply")) {
                messageObject.put("sender", user);
                messageObject.put("waiting", queue.size());
                // send message
                for (CollaborationUser participant : participants) {
                        participant.getSession().getBasicRemote().sendText(messageObject.toString());
                }
            } else if(behavior.equals("stop")) {
                messageObject.put("operator", user);
                // send message
                for (CollaborationUser participant : participants) {
                    for (int i = 0; i < queue.size(); i++) {
                        if (queue.get(i).equals(participant.getUserId())) {
                            messageObject.put("waiting", i);
                            participant.getSession().getBasicRemote().sendText(messageObject.toString());
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cacheStore(ArrayList<ChatMsg> msgRecords){

    }
}



