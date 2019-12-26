package cn.edu.njnu.geoproblemsolving.Entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Resource")
public class ResourceEntity {

    private String resourceId;
    private String name;
    private String description;
    private String type;
    private String fileSize;
    private String pathURL;
    private String uploaderId;
    private String uploaderName;
    private String uploadTime;
    private String privacy;     //public/private
    private String thumbnail; //缩略图地址

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPathURL(String pathURL) {
        this.pathURL = pathURL;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getPathURL() {
        return pathURL;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public String getPrivacy() {
        return privacy;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
