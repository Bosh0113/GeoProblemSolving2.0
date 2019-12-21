package cn.edu.njnu.geoproblemsolving.Entity;

import com.alibaba.fastjson.JSONArray;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "SubProject")
public class SubProjectEntity {

    private String projectId;
    private String subProjectId;
    private String title;
    private String description;
    private String managerId;
    private String managerName;
    private JSONArray members;
    private String createTime;
    private String solvingProcess;
    private String background;
    private String limitation;
    private String type; //type0|type1
    private String stepId;

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBackground() {
        return background;
    }

    public String getLimitation() {
        return limitation;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void setLimitation(String limitation) {
        this.limitation = limitation;
    }

    public String getManagerName() {
        return managerName;
    }

    public String getDescription() {
        return description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public JSONArray getMembers() {
        return members;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getTitle() {
        return title;
    }

    public String getManagerId() {
        return managerId;
    }

    public String getSubProjectId() {
        return subProjectId;
    }

    public String getSolvingProcess() {
        return solvingProcess;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMembers(JSONArray members) {
        this.members = members;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public void setSubProjectId(String subProjectId) {
        this.subProjectId = subProjectId;
    }

    public void setSolvingProcess(String solvingProcess) {
        this.solvingProcess = solvingProcess;
    }
}
