package cn.edu.njnu.geoproblemsolving.business.chatroom.chatmessage;

import cn.edu.njnu.geoproblemsolving.Entity.ModelTools.CModel.support.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ChatMessageRecords")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRecords extends BaseEntity {
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
