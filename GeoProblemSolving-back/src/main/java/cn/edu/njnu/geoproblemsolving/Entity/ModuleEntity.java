package cn.edu.njnu.geoproblemsolving.Entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Module")
public class ModuleEntity {

    private String moduleId;
    private String subProjectId;
    private String title;
    private String description;
    private String type;
    private String creator;
    private String createTime;


    public void setSubProjectId(String subProjectId) {
        this.subProjectId = subProjectId;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getModuleId() {
        return moduleId;
    }

    public String getCreator() {
        return creator;
    }

    public String getTitle() {
        return title;
    }

    public String getSubProjectId() {
        return subProjectId;
    }

    public String getType() {
        return type;
    }
}
