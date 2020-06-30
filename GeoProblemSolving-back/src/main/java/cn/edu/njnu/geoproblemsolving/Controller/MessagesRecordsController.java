package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.MessageRecords.MessageRecordsDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.MessageRecordsEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping("/messages")
public class MessagesRecordsController {

    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/save", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public String saveMessageRecords(@RequestBody MessageRecordsEntity messageRecords) {
        // decode scopeId
        String scopeId = messageRecords.getRoomId();
        messageRecords.setRoomId(scopeId);
        MessageRecordsDaoImpl messageRecordsDao = new MessageRecordsDaoImpl(mongoTemplate);
        return messageRecordsDao.saveMessageRecords(messageRecords);
    }

    @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    public Object inquiryMessageRecords(@RequestParam("type") String type, @RequestParam("key") String key, @RequestParam("value") String value) {
        MessageRecordsDaoImpl historyEventDao = new MessageRecordsDaoImpl(mongoTemplate);
        return historyEventDao.inquiryMessageRecords(type, key, value);
    }
}
