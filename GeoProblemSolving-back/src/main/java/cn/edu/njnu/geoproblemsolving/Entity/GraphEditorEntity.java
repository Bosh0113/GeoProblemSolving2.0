package cn.edu.njnu.geoproblemsolving.Entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "GraphEditor")
@Data
public class GraphEditorEntity {

    private String taskId;
    private String scopeId;
    private String title;
    private String description;
    private String graphXML;
    private String date;
}
