package cn.edu.njnu.geoproblemsolving.Entity;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;

@Document(collection = "Toolset")
@Data
public class ToolsetEntity {
    private String tsid;
    private String toolsetName;
    private JSONArray toolList; // 创建的时候不添加toolList，在toolCollection页面添加，或者创建tool 选择 toolset时，自动添加
    private ArrayList<String> categoryTag;
    private String recomStep; // step类型 or general
    private String provider;
    private String privacy;
    private String createTime;

    public String getTsid() {
        return tsid;
    }

    public String getToolsetName() {
        return toolsetName;
    }

    public JSONArray getToolList() {
        return toolList;
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

    public void setTsid(String tsid) {
        this.tsid = tsid;
    }

    public void setToolsetName(String toolsetName) {
        this.toolsetName = toolsetName;
    }

    public void setToolList(JSONArray toolList) {
        this.toolList = toolList;
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