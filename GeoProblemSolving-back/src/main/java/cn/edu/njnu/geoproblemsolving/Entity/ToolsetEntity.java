package cn.edu.njnu.geoproblemsolving.Entity;

import cn.edu.njnu.geoproblemsolving.domain.tool.Tool;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;

@Document(collection = "Toolset")
@Data
public class ToolsetEntity {
    private String tsId;
    private String toolsetName;
    private ArrayList<Tool> toolList; // 创建的时候不添加toolList，在toolCollection页面添加，或者创建tool 选择 toolset时，自动添加
    private ArrayList<String> categoryTag;
    private ArrayList<String> recomStep; // step类型 or general
    private String description;
    private String provider;
    private String privacy;
    private String toolsetImg;
    private String createTime;

    public String getTsId() {
        return tsId;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getToolsetName() {
        return toolsetName;
    }

    public void setToolList(ArrayList<Tool> toolList) {
        this.toolList = toolList;
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

    public String getToolsetImg() {
        return toolsetImg;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setTsId(String tsId) {
        this.tsId = tsId;
    }

    public void setToolsetName(String toolsetName) {
        this.toolsetName = toolsetName;
    }

    public ArrayList<Tool> getToolList() {
        return toolList;
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

    public void setToolsetImg(String toolsetImg) {
        this.toolsetImg = toolsetImg;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}