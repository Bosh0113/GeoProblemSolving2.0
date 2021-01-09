package cn.edu.njnu.geoproblemsolving.business.resource.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Data
@Component
@Document(collection = "Resource")
public class IResourceEntity {
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
    private String editToolInfo;
}
