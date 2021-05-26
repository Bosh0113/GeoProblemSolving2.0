package cn.edu.njnu.geoproblemsolving.business.collaboration.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @Author mzy
 * @Date 2021/4
 * @Description 文本聊天信息实体
 */
@Data
@Document(collection = "ChatMsg")
public class ChatMsg {

    @Id
    private String messageId;

    private String aid;

    private CollaborationUser sender;

    private List<String> receiver;

    private String content;

    private Date time;
}
