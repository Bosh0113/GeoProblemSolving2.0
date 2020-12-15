package cn.edu.njnu.geoproblemsolving.business.chatroom.chatmessage;



import cn.edu.njnu.geoproblemsolving.business.chatroom.chatmessage.dto.AddChatMessageRecordsDTO;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
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
    ChatMessageRecordsRepository chatMessageRecordsRepository;

    public List<ChatMessageRecords> inquiryTypeAndRoomId(String type, String roomId) {
        return chatMessageRecordsRepository.findAllByTypeAndRoomId(type, roomId);
    }

    public List<ChatMessageRecords> queryAllByRoomIdAndPage(int page, int size, String roomId) throws Exception {
        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
        PageRequest pageRequest =PageRequest.of(page - 1, size,sort);
//        PageRequest pageRequest1 = new PageRequest();

        return chatMessageRecordsRepository.findAllByRoomId(roomId,pageRequest);
    }

    public long count() {
        return chatMessageRecordsRepository.count();
    }


    public JsonResult insert(AddChatMessageRecordsDTO addChatMessageRecordsDTO) {
        ChatMessageRecords chatMessageRecords = new ChatMessageRecords();
        addChatMessageRecordsDTO.convertTo(chatMessageRecords);
        return ResultUtils.success(chatMessageRecordsRepository.insert(chatMessageRecords));
    }

//    @SneakyThrows
//    public JsonResult queryAllByRoomIdAndCreateTimeAndPage(String roomId, int page, int size, String createTime) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyt-MM-dd");
//        Date date = simpleDateFormat.parse(createTime);
//        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
//        PageRequest pageRequest =PageRequest.of(page - 1, size,sort);
//        return ResultUtils.success(messageRecordsRepository.findAllByRoomIdAndCreateTime(roomId, createTime,pageRequest));
//    }

    public List<ChatMessageRecords> findAllByContentLikeAndRoomId(String roomId, String key, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
        PageRequest pageRequest =PageRequest.of(page - 1, size,sort);
        return chatMessageRecordsRepository.findByContentContainingAndRoomId(key,roomId);
    }
    public List<ChatMessageRecords> findAllByTimeLikeAndRoomId(String roomId, String key, int page, int size) throws ParseException {
        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
        PageRequest pageRequest =PageRequest.of(page - 1, size,sort);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//注意月份是MM
        String start =key + " 00:00:00";
        String end =key + " 23:59:59";
        Date dateStart = simpleDateFormat.parse(start);
        Date dateEnd = simpleDateFormat.parse(end);
        return chatMessageRecordsRepository.findByCreateTimeBetweenAndRoomId(dateStart,dateEnd,roomId);
    }
    public List<ChatMessageRecords> findAllByTypeAndRoomId(String roomId, String type)  {
        return chatMessageRecordsRepository.findAllByRoomIdAndType(roomId,type);
    }
}
