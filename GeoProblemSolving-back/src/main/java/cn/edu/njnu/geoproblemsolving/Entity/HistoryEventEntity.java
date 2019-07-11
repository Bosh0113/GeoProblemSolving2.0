package cn.edu.njnu.geoproblemsolving.Entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "HistoryEvent")
public class HistoryEventEntity {

    private String historyId;
    private String scopeId; //查询条件Id
    private String description;
    private String userId;
    private String eventType;
    private String createTime;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setScopeId(String scopeId) {
        this.scopeId = scopeId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getDescription() {
        return description;
    }

    public String getScopeId() {
        return scopeId;
    }

    public String getHistoryId() {
        return historyId;
    }

    public String getUserId() {
        return userId;
    }

    public String getEventType() {
        return eventType;
    }
}
