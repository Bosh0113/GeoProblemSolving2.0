package cn.edu.njnu.geoproblemsolving.domain.chatmessage;

import cn.edu.njnu.geoproblemsolving.Utils.ResultUtils;
import cn.edu.njnu.geoproblemsolving.domain.support.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 * @Author Zhiyi
 * @Date 2020/6/15  20:05
 * @Version 1.0.0
 */
@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping("/message")
public class MessageRecordsController {
    @Autowired
    MessageRecordsService messageRecordsService;

    @Autowired
    MongoTemplate mongoTemplate;

    @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    public JsonResult inquiryMessageRecords(@RequestParam("type") String type, @RequestParam("roomId") String roomId) {
        return ResultUtils.success(messageRecordsService.inquiryTypeAndRoomId(type, roomId));
    }

    @RequestMapping(value = "/inquiryByPage/{roomId}/{page}/{size}", method = RequestMethod.GET)
    public JsonResult inquiryByPage(@PathVariable("roomId") String roomId, @PathVariable("page") int page, @PathVariable("size") int size) throws Exception {
        return ResultUtils.success(messageRecordsService.queryAllByRoomIdAndPage(page, size, roomId));
    }

    @RequestMapping(value = "/totalCount", method = RequestMethod.GET)
    public JsonResult getTotalCount() {
        return ResultUtils.success(messageRecordsService.count());
    }

//    @RequestMapping(value = "/inquiryByTime/{roomId}/{page}/{size}/{time}", method = RequestMethod.GET)
//    public JsonResult inquiryByTime(@PathVariable("roomId") String roomId, @PathVariable("time") String createTime, @PathVariable("page") int page, @PathVariable("size") int size) {
//        return ResultUtils.success(messageRecordsService.queryAllByRoomIdAndCreateTimeAndPage(roomId, page, size, createTime));
//    }

    @RequestMapping(value = "/contentLike/{roomId}/{key}/{page}/{size}", method = RequestMethod.GET)
    public JsonResult inquiryByContentLike(@PathVariable("roomId") String roomId, @PathVariable("key") String key, @PathVariable("page") int page, @PathVariable("size") int size) {
        return ResultUtils.success(messageRecordsService.findAllByContentLikeAndRoomId(roomId,key, page, size));
    }

    @RequestMapping(value = "/timeLike/{roomId}/{key}/{page}/{size}", method = RequestMethod.GET)
    public JsonResult inquiryByTimeLike(@PathVariable("roomId") String roomId, @PathVariable("key") String key, @PathVariable("page") int page, @PathVariable("size") int size)  throws ParseException {
        return ResultUtils.success(messageRecordsService.findAllByTimeLikeAndRoomId(roomId,key, page, size));
    }
}
