package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.HistoryEvent.HistoryEventDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.HistoryEventEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping("/history")
public class HistoryEventController {

    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/save", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public String saveHistoryEvent(@RequestBody HistoryEventEntity historyEvent) {
        // decode scopeId
        String scopeId = historyEvent.getScopeId();
        historyEvent.setScopeId(scopeId);
        HistoryEventDaoImpl historyEventDao = new HistoryEventDaoImpl(mongoTemplate);
        return historyEventDao.saveHistoryEvent(historyEvent);
    }

    @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    public Object inquiryHistoryEvent(@RequestParam("eventType") String eventType, @RequestParam("key") String key, @RequestParam("value") String value) {
        HistoryEventDaoImpl historyEventDao = new HistoryEventDaoImpl(mongoTemplate);
        return historyEventDao.inquiryHistoryEvent(eventType, key, value);
    }
}
