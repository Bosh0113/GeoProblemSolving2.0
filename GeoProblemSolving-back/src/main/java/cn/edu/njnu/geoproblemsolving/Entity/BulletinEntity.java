package cn.edu.njnu.geoproblemsolving.Entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "Bulletin")
public class BulletinEntity {
    private String bulletinId;
    private String moduleId;
    private String title;
    private String description;
    private String time;

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBulletinId(String bulletinId) {
        this.bulletinId = bulletinId;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getBulletinId() {
        return bulletinId;
    }

    public String getModuleId() {
        return moduleId;
    }
}
