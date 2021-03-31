package cn.edu.njnu.geoproblemsolving.business.tool;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;

@Document(collection = "Toolset")
@Data
public class ToolsetEntity {
    @Id
    private String tsid;
    private String toolsetName;
    private ArrayList<ToolEntity> toolList; // 创建的时候不添加toolList，在toolCollection页面添加，或者创建tool 选择 toolset时，自动添加
    private ArrayList<String> categoryTag;
    private ArrayList<String> recomStep; // step类型 or general
    private String description;
    private String provider;
    private String privacy;
    private String toolsetImg;
    private String createdTime;
}