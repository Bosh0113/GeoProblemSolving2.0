package cn.edu.njnu.geoproblemsolving.business.tool.chatroom.chatmessage;


import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
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
@RequestMapping("/chatmessage")
public class ChatMessageRecordsController {
    @Autowired
    ChatMessageRecordsService chatMessageRecordsService;

    @Autowired
    MongoTemplate mongoTemplate;

    @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    public JsonResult inquiryMessageRecords(@RequestParam("type") String type, @RequestParam("roomId") String roomId) {
        return ResultUtils.success(chatMessageRecordsService.inquiryTypeAndRoomId(type, roomId));
    }

    @RequestMapping(value = "/inquiryByPage/{roomId}/{page}/{size}", method = RequestMethod.GET)
    public JsonResult inquiryByPage(@PathVariable("roomId") String roomId, @PathVariable("page") int page, @PathVariable("size") int size) throws Exception {
        return ResultUtils.success(chatMessageRecordsService.queryAllByRoomIdAndPage(page, size, roomId));
    }

    @RequestMapping(value = "/totalCount", method = RequestMethod.GET)
    public JsonResult getTotalCount() {
        return ResultUtils.success(chatMessageRecordsService.count());
    }


    @RequestMapping(value = "/contentLike/{roomId}/{key}/{page}/{size}", method = RequestMethod.GET)
    public JsonResult inquiryByContentLike(@PathVariable("roomId") String roomId, @PathVariable("key") String key, @PathVariable("page") int page, @PathVariable("size") int size) {
        return ResultUtils.success(chatMessageRecordsService.findAllByContentLikeAndRoomId(roomId,key, page, size));
    }

    @RequestMapping(value = "/timeLike/{roomId}/{key}/{page}/{size}", method = RequestMethod.GET)
    public JsonResult inquiryByTimeLike(@PathVariable("roomId") String roomId, @PathVariable("key") String key, @PathVariable("page") int page, @PathVariable("size") int size)  throws ParseException {
        return ResultUtils.success(chatMessageRecordsService.findAllByTimeLikeAndRoomId(roomId,key, page, size));
    }
    @RequestMapping(value = "/inquiryByType/{roomId}/{type}", method = RequestMethod.GET)
    public JsonResult inquiryByTimeLike(@PathVariable("roomId") String roomId, @PathVariable("type") String type)  throws ParseException {
        return ResultUtils.success(chatMessageRecordsService.findAllByTypeAndRoomId(roomId,type));
    }
}
