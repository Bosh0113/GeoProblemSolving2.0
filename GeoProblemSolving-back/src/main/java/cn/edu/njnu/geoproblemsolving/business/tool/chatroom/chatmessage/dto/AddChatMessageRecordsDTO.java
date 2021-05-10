package cn.edu.njnu.geoproblemsolving.business.tool.chatroom.chatmessage.dto;



import cn.edu.njnu.geoproblemsolving.common.utils.ToDomainConverter;
import cn.edu.njnu.geoproblemsolving.business.tool.chatroom.chatmessage.ChatMessageRecords;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @Author Zhiyi
 * @Date 2020/6/15  22:54
 * @Version 1.0.0
 */
@Data
public class AddChatMessageRecordsDTO implements ToDomainConverter<ChatMessageRecords> {
    @Id
    private String messageId;
    private String roomId; //查询条件Id
    private String srcUserId;
    private String srcUserName;
    private String targetUserId;
    private String targetUserName;
    private String target;//all--群聊 or userId--私聊
    private String type;
    private String content;
}
