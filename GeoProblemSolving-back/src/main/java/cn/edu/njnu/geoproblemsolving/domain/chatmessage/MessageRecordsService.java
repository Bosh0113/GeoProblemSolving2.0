package cn.edu.njnu.geoproblemsolving.domain.chatmessage;

import cn.edu.njnu.geoproblemsolving.Utils.ResultUtils;
import cn.edu.njnu.geoproblemsolving.domain.chatmessage.dto.AddMessageRecordsDTO;
import cn.edu.njnu.geoproblemsolving.domain.support.JsonResult;
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
public class MessageRecordsService {
    @Autowired
    MessageRecordsRepository messageRecordsRepository;

    public List<MessageRecords> inquiryTypeAndRoomId(String type, String roomId) {
        return messageRecordsRepository.findAllByTypeAndRoomId(type, roomId);
    }

    public List<MessageRecords> queryAllByRoomIdAndPage(int page, int size, String roomId) throws Exception {
        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
        PageRequest pageRequest =PageRequest.of(page - 1, size,sort);
//        PageRequest pageRequest1 = new PageRequest();

        return messageRecordsRepository.findAllByRoomId(roomId,pageRequest);
    }

    public long count() {
        return messageRecordsRepository.count();
    }


    public JsonResult insert(AddMessageRecordsDTO addMessageRecordsDTO) {
        MessageRecords messageRecords = new MessageRecords();
        addMessageRecordsDTO.convertTo(messageRecords);
        return ResultUtils.success(messageRecordsRepository.insert(messageRecords));
    }

//    @SneakyThrows
//    public JsonResult queryAllByRoomIdAndCreateTimeAndPage(String roomId, int page, int size, String createTime) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyt-MM-dd");
//        Date date = simpleDateFormat.parse(createTime);
//        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
//        PageRequest pageRequest =PageRequest.of(page - 1, size,sort);
//        return ResultUtils.success(messageRecordsRepository.findAllByRoomIdAndCreateTime(roomId, createTime,pageRequest));
//    }

    public JsonResult findAllByContentLikeAndRoomId(String roomId, String key, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
        PageRequest pageRequest =PageRequest.of(page - 1, size,sort);
        return ResultUtils.success(messageRecordsRepository.findByContentContainingAndRoomId(key,roomId,pageRequest));
    }
    public JsonResult findAllByTimeLikeAndRoomId(String roomId, String key, int page, int size) throws ParseException {
        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
        PageRequest pageRequest =PageRequest.of(page - 1, size,sort);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//注意月份是MM
        String start =key + " 00:00:00";
        String end =key + " 23:59:59";
        Date dateStart = simpleDateFormat.parse(start);
        Date dateEnd = simpleDateFormat.parse(end);
        return ResultUtils.success(messageRecordsRepository.findByCreateTimeBetweenAndRoomId(dateStart,dateEnd,roomId,pageRequest));
    }
}
