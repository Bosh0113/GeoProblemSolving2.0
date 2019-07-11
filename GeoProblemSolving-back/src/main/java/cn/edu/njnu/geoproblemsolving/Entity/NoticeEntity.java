package cn.edu.njnu.geoproblemsolving.Entity;


import com.alibaba.fastjson.JSONObject;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Notice")
public class NoticeEntity {

    private String noticeId;
    private String recipientId;
    private String type;    //notice|reply|apply
    private JSONObject content;
    private String state;   //read|unread
    private String createTime;

    public void setState(String state) {
        this.state = state;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(JSONObject content) {
        this.content = content;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getState() {
        return state;
    }

    public String getType() {
        return type;
    }

    public JSONObject getContent() {
        return content;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getCreateTime() {
        return createTime;
    }
}
