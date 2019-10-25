package cn.edu.njnu.geoproblemsolving.Entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "Step")
@Data
public class StepEntity {
    // 基本信息
    private String stepId;
    private String name;
    private String type;
    private String description;
    private String subProjectId;
    private String creator;
    private String createTime;
    // 功能模块
    private ArrayList<String> toolList;
    private ArrayList<String> toolsetList;
    // 专有功能
    private JSONObject content;

    public String getStepId() {
        return stepId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getSubProjectId() {
        return subProjectId;
    }

    public String getCreator() {
        return creator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public ArrayList<String> getToolList() {
        return toolList;
    }

    public ArrayList<String> getToolsetList() {
        return toolsetList;
    }

    public JSONObject getContent() {
        return content;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSubProjectId(String subProjectId) {
        this.subProjectId = subProjectId;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setToolList(ArrayList<String> toolList) {
        this.toolList = toolList;
    }

    public void setToolsetList(ArrayList<String> toolsetList) {
        this.toolsetList = toolsetList;
    }

    public void setContent(JSONObject content) {
        this.content = content;
    }
}
