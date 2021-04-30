package cn.edu.njnu.geoproblemsolving.business.collaboration.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * @Author mzy
 * @Date 2021/4
 * @Description 文本聊天信息实体
 */
@Data
public class ChatMsg {

    @Id
    private String msgId;

    private String aid;

    private String sender;

    private List<String> receiver;

    private String content;

    private String time;
}
