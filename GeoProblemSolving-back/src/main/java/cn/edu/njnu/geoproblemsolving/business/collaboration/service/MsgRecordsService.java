package cn.edu.njnu.geoproblemsolving.business.collaboration.service;

import cn.edu.njnu.geoproblemsolving.business.collaboration.entity.ChatMsg;
import cn.edu.njnu.geoproblemsolving.business.collaboration.entity.MsgRecords;
import cn.edu.njnu.geoproblemsolving.business.collaboration.repository.ChatMsgRepository;
import cn.edu.njnu.geoproblemsolving.business.collaboration.repository.MsgRecordsRepository;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import com.alibaba.fastjson.JSONObject;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MsgRecordsService {

    @Autowired
    MsgRecordsRepository msgRecordsRepository;

    @Autowired
    ChatMsgRepository chatMsgRepository;

    public JsonResult msgRecordsCreate(String aid, ArrayList<String> records, ArrayList<String> participants) {
        try {
            MsgRecords msgRecords = new MsgRecords();

            msgRecords.setRecordId(UUID.randomUUID().toString());
            msgRecords.setAid(aid);
            msgRecords.setRecords(records);
            msgRecords.setParticipants(participants);
            msgRecords.setCreatedTime(new Date());

            return ResultUtils.success(msgRecordsRepository.save(msgRecords));
        } catch (Exception ex){
            return ResultUtils.error(-2, ex.toString());
        }
    }

    public JsonResult findMsgRecords(String aid){
        try {
            List<MsgRecords> result = msgRecordsRepository.findByAid(aid);
            if (result.isEmpty()) return ResultUtils.error(-1, "Fail: Message records do not exist.");

            return ResultUtils.success(result);
        } catch (Exception ex){
            return ResultUtils.error(-2, ex.toString());
        }
    }

    public JsonResult findChatRecord(String recordId){
        try {
            Optional optional = msgRecordsRepository.findById(recordId);
            if(!optional.isPresent()) return ResultUtils.error(-1, "Fail: Message record do not exist.");

            MsgRecords msgRecords = (MsgRecords)optional.get();
            List<ChatMsg> result = new ArrayList<>();
            for(String messageId: msgRecords.getRecords()) {
                Optional message = chatMsgRepository.findById(messageId);
                if(message.isPresent()) result.add((ChatMsg) message.get());
            }

            return ResultUtils.success(result);
        } catch (Exception ex){
            return ResultUtils.error(-2, ex.toString());
        }
    }
}
