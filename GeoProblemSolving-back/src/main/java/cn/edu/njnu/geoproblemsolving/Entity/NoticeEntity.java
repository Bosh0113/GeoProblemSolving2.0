package cn.edu.njnu.geoproblemsolving.Entity;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Notice")
public class NoticeEntity {

    private String noticeId;
    private String recipientId;
    private String type;    //notice|reply|apply
    private JSONObject content;
    private String state;   //read|unread
    private String createdTime;

}
