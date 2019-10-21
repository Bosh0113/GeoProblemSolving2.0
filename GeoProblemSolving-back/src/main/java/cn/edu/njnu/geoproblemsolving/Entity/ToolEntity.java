package cn.edu.njnu.geoproblemsolving.Entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "Tool")
@Data
public class ToolEntity {
    private String tid;
    private String toolName;
    private String toolUrl;
    private JSONObject modelInfo; //stateId, oid,mdlId,模型条目？
    private JSONArray toolsetInfo; // 可在多个不同的toolset内，或default toolset内
    private String recomStep; // step类型 or general
    private ArrayList<String> categoryTag;
    private String provider;
    private String privacy;
    private String createTime;

    public String getTid(){
        return tid;
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

    public JSONArray getToolsetInfo() {
        return toolsetInfo;
    }

    public ArrayList<String> getCategoryTag() {
        return categoryTag;
    }

    public String getRecomStep() {
        return recomStep;
    }

    public String getProvider() {
        return provider;
    }

    public String getPrivacy() {
        return privacy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setTid(String tid) {
        this.tid = tid;
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

    public void setToolsetInfo(JSONArray toolsetInfo) {
        this.toolsetInfo = toolsetInfo;
    }

    public void setCategoryTag(ArrayList<String> categoryTag) {
        this.categoryTag = categoryTag;
    }

    public void setRecomStep(String recomStep) {
        this.recomStep = recomStep;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
