package cn.edu.njnu.geoproblemsolving.Entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "Tool")
@Data
public class ToolEntity {
    private String tId;
    private String toolName;
    private String toolUrl;
    private JSONObject modelInfo; //stateId, oid,mdlId,模型条目？
    private String description; // 可在多个不同的toolset内，或没有dataset
    private ArrayList<String> recomStep; // step类型 or general
    private ArrayList<String> categoryTag;
    private String provider;
    private String privacy;
    private String toolImg;
    private String createTime;

    public String getTId(){
        return tId;
    }

    public String getToolName() {
        return toolName;
    }

    public String getToolUrl() {
        return toolUrl;
    }

    public JSONObject getModelInfo() {
        return modelInfo;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getCategoryTag() {
        return categoryTag;
    }

    public ArrayList<String> getRecomStep() {
        return recomStep;
    }

    public String getProvider() {
        return provider;
    }

    public String getPrivacy() {
        return privacy;
    }

    public String getToolImg() {
        return toolImg;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setTId(String tId) {
        this.tId = tId;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public void setToolUrl(String toolUrl) {
        this.toolUrl = toolUrl;
    }

    public void setModelInfo(JSONObject modelInfo) {
        this.modelInfo = modelInfo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategoryTag(ArrayList<String> categoryTag) {
        this.categoryTag = categoryTag;
    }

    public void setRecomStep(ArrayList<String> recomStep) {
        this.recomStep = recomStep;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public void setToolImg(String toolImg) {
        this.toolImg = toolImg;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
