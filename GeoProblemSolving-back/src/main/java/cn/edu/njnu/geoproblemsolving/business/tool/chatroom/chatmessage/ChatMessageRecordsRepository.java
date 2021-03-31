package cn.edu.njnu.geoproblemsolving.business.chatroom.chatmessage;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * @Author Zhiyi
 * @Date 2020/6/15  20:05
 * @Version 1.0.0
 */
public interface ChatMessageRecordsRepository extends MongoRepository<ChatMessageRecords, String> {
    List<ChatMessageRecords> findAllByRoomId(String roomId, PageRequest pageRequest);
    List<ChatMessageRecords> findAllByTypeAndRoomId(String type, String roomId);

    //    Page<MessageRecords> findByRoomId(String roomId, PageRequest pageRequest);
//    List<MessageRecords> findAllByRoomIdAndCreateTime(String roomId, String createTime, PageRequest pageRequest);

    List<ChatMessageRecords> findByContentContainingAndRoomId(String key, String roomId);
    List<ChatMessageRecords> findByCreateTimeBetweenAndRoomId(Date from, Date to, String roomId);
    List<ChatMessageRecords> findAllByRoomIdAndType(String type, String roomId);
}
