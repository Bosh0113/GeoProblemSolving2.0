package cn.edu.njnu.geoproblemsolving.Entity.Resources;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Resource")
@Data
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
    private String editToolInfo;
}
