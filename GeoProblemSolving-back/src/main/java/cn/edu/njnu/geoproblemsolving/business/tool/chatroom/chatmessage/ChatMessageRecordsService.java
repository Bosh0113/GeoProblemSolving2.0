package cn.edu.njnu.geoproblemsolving.business.tool.chatroom.chatmessage;

import cn.edu.njnu.geoproblemsolving.business.collaboration.entity.ChatMsg;
import cn.edu.njnu.geoproblemsolving.business.collaboration.repository.ChatMsgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author Zhiyi
 * @Date 2020/6/16  11:00
 * @Version 1.0.0
 */
@Service
public class ChatMessageRecordsService {
    @Autowired
    ChatMsgRepository chatMsgRepository;

//    public List<MsgRecords> inquiryTypeAndRoomId(String type, String roomId) {
//        return chatMessageRecordsRepository.findAllByTypeAndAid(type, roomId);
//    }

    public List<ChatMsg> queryAllByRoomIdAndPage(int page, int size, String roomId) throws Exception {
        Sort sort = Sort.by(Sort.Direction.DESC,"time");
        PageRequest pageRequest =PageRequest.of(page - 1, size, sort);

        return chatMsgRepository.findAllByAid(roomId, pageRequest);
    }

    public long count() {
        return chatMsgRepository.count();
    }


//    public JsonResult insert(AddChatMessageRecordsDTO addChatMessageRecordsDTO) {
//        ChatMessageRecords chatMessageRecords = new ChatMessageRecords();
//        addChatMessageRecordsDTO.convertTo(chatMessageRecords);
//        return ResultUtils.success(chatMessageRecordsRepository.insert(chatMessageRecords));
//    }

    public List<ChatMsg> findAllByContentLikeAndRoomId(String roomId, String key, int page, int size) {
        return chatMsgRepository.findByContentContainingAndAid(key, roomId);
    }
    public List<ChatMsg> findAllByTimeLikeAndRoomId(String roomId, String key, int page, int size) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//注意月份是MM
        String start =key + " 00:00:00";
        String end =key + " 23:59:59";
        Date dateStart = simpleDateFormat.parse(start);
        Date dateEnd = simpleDateFormat.parse(end);
        return chatMsgRepository.findByTimeBetweenAndAid(dateStart,dateEnd,roomId);
    }

//    public List<MsgRecords> findAllByTypeAndRoomId(String roomId, String type)  {
//        return chatMessageRecordsRepository.findAllByRoomIdAndType(roomId,type);
//    }
}
