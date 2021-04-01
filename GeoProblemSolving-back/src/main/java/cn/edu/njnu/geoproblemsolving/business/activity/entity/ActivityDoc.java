package cn.edu.njnu.geoproblemsolving.business.activity.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "ActivityDocs")
public class ActivityDoc {

    /**
     * aid: activity id
     * taskId: task id
     * document: activity document content
     */
    @Id
    private String aid;
    private String document;
}
