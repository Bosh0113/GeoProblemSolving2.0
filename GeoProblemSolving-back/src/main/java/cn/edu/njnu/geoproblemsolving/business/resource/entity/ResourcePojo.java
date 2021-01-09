package cn.edu.njnu.geoproblemsolving.business.resource.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Data
@Document(collection = "resources")
public class ResourcePojo {
    @Id
    private String uid;
    private String name;
    //似乎用不到
    private String address;
    private String type;
    private String privacy;
    private String thumbnail;
    private String editToolInfo;
    private String fileSize;
    private String parent;
    private String md5;
    private String suffix;
    private String description;
    private String template;
    private String uploadTime;
    private ArrayList<ResourcePojo> children;

    //....
    private String uploadId;

}
