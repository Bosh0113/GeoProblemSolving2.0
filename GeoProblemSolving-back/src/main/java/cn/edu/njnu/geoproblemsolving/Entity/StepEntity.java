package cn.edu.njnu.geoproblemsolving.Entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Step")
@Data
public class StepEntity {
    private String stepId;
    private String stepName;
    private String subProjectId;
    private String title;
    private JSONObject content;
    private String type;
//    private String methods;
    private String creator;
    private String createTime;
}
