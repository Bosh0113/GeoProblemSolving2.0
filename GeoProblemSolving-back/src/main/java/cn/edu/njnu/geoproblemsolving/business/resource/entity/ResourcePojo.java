package cn.edu.njnu.geoproblemsolving.business.resource.entity;

import cn.edu.njnu.geoproblemsolving.Entity.Resources.ResourceEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;

@Data
@Document(collection = "resources")
public class ResourcePojo {
    // @Id
    // private String uid;
    // private String name;
    // //似乎用不到
    // private String address;
    // private String type;
    // private String privacy;
    // private String thumbnail;
    // private String editToolInfo;
    // private String fileSize;
    // private String parent;
    // private String md5;
    // private String suffix;
    // private String description;
    // private String template;
    // private String uploadTime;
    // private ArrayList<ResourcePojo> children;
    //
    // //....
    // private String uploadId;
    @Id
    private String uid;
    private String name;
    //数据在数据容器中的链接
    private String address;
    private Boolean isFolder;
    private String type;
    private String privacy;
    private String thumbnail;
    private String editToolInfo;
    private long fileSize;
    //存储父资源的 uuid
    private String parent;
    private String md5;
    private String suffix;
    private String description;
    private String template;
    private Date uploadTime;
    private ArrayList<ResourceEntity> children;

}
