package cn.edu.njnu.geoproblemsolving.Entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "Project")
public class ProjectEntity {

    private String projectId;
    private String title;
    private String description;
    private String category;
    private String managerId;
    private String managerName;
    private String introduction;
    private JSONArray members;
    private String privacy;
    private String tag;
    private String picture;
    private String createTime;
    private String endTime;
    private String type; //type0|type1|type2
    private String stepId;
    private String solvingProcess;
    private JSONObject permissionManager;

    public String getSolvingProcess() {
        return solvingProcess;
    }

    public String getStepId() {
        return stepId;
    }

    public String getType() {
        return type;
    }

    public String getManagerName() {
        return managerName;
    }

    public String getCategory() {
        return category;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getDescription() {
        return description;
    }

    public String getEndTime() {
        return endTime;
    }

    public JSONArray getMembers() {
        return members;
    }

    public String getPicture() {
        return picture;
    }

    public String getPrivacy() {
        return privacy;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getTag() {
        return tag;
    }

    public String getTitle() {
        return title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getManagerId() {
        return managerId;
    }

    public JSONObject getPermissionManager() {
        return permissionManager;
    }

    public void setSolvingProcess(String solvingProcess) {
        this.solvingProcess = solvingProcess;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setMembers(JSONArray members) {
        this.members = members;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public void setPermissionManager(JSONObject permissionManager) {
        this.permissionManager = permissionManager;
    }
}
