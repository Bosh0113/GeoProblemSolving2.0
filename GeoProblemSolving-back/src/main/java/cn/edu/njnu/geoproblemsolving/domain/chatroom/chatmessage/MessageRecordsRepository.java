package cn.edu.njnu.geoproblemsolving.domain.chatroom.chatmessage;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * @Author Zhiyi
 * @Date 2020/6/15  20:05
 * @Version 1.0.0
 */
public interface MessageRecordsRepository extends MongoRepository<MessageRecords, String> {
    List<MessageRecords> findAllByRoomId(String roomId, PageRequest pageRequest);
    List<MessageRecords> findAllByTypeAndRoomId(String type, String roomId);

    //    Page<MessageRecords> findByRoomId(String roomId, PageRequest pageRequest);
//    List<MessageRecords> findAllByRoomIdAndCreateTime(String roomId, String createTime, PageRequest pageRequest);

    List<MessageRecords> findByContentContainingAndRoomId(String key,String roomId);
    List<MessageRecords> findByCreateTimeBetweenAndRoomId(Date from, Date to,String roomId);
    List<MessageRecords> findAllByRoomIdAndType(String type,String roomId);
}
