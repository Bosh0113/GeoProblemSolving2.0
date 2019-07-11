package cn.edu.njnu.geoproblemsolving.Entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "LogicalTask")
public class LogicalTaskEntity {

    private String taskId;
    private String taskName;
    private String description;
    private String graphXML;
    private String logicalXML;
    private String collaborativeId;
    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public String getDescription() {
        return description;
    }

    public String getCollaborativeId() {
        return collaborativeId;
    }

    public String getLogicalXML() {
        return logicalXML;
    }

    public String getGraphXML() {
        return graphXML;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCollaborativeId(String collaborativeId) {
        this.collaborativeId = collaborativeId;
    }

    public void setLogicalXML(String logicalXML) {
        this.logicalXML = logicalXML;
    }

    public void setGraphXML(String graphXML) {
        this.graphXML = graphXML;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
