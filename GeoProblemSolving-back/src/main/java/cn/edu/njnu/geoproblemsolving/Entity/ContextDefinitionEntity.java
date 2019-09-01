package cn.edu.njnu.geoproblemsolving.Entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ContextDefinition")
@Data
public class ContextDefinitionEntity {
    private String stepId;
    private String stepName;
    private String subProjectId;
    private String title;
    private String description;
    private String type;
    private String boundary;
    private String methods;
    private String scale;
    private String difficulties;
    private String goals;
    private String others;
    private String creator;
    private String createTime;
}
