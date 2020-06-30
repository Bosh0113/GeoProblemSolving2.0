package cn.edu.njnu.geoproblemsolving.domain.chatmessage.dto;

import cn.edu.njnu.geoproblemsolving.Dto.ToDomainConverter;
import cn.edu.njnu.geoproblemsolving.domain.chatmessage.MessageRecords;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @Author Zhiyi
 * @Date 2020/6/15  22:54
 * @Version 1.0.0
 */
@Data
public class AddMessageRecordsDTO implements ToDomainConverter<MessageRecords> {
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
