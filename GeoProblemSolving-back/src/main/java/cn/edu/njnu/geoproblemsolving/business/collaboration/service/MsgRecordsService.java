package cn.edu.njnu.geoproblemsolving.business.collaboration.service;

import cn.edu.njnu.geoproblemsolving.business.collaboration.entity.ChatMsg;
import cn.edu.njnu.geoproblemsolving.business.collaboration.entity.MsgRecords;
import cn.edu.njnu.geoproblemsolving.business.collaboration.repository.MsgRecordsRepository;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MsgRecordsService {

    @Autowired
    MsgRecordsRepository msgRecordsRepository;

    public void msgRecordsCreate(String aid, ArrayList<ChatMsg> records) {
        try {
            MsgRecords msgRecords = new MsgRecords();

            msgRecords.setMessageId(UUID.randomUUID().toString());
            msgRecords.setAid(aid);
            msgRecords.setRecords(records);
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            msgRecords.setCreatedTime(dateFormat.format(date));

            msgRecordsRepository.save(msgRecords);
        } catch (Exception ex){
            ex.printStackTrace();
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
}
