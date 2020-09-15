package cn.edu.njnu.geoproblemsolving.Entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "Task")
public class TaskEntity {
    private String aid;
    private String taskId;
    private String taskName;
    private String description;
    private Date startTime;
    private Date endTime;
    private String state;   //_todo; doing; done.
    private int importance;      //0;1.
    private String creatorId;
    private String creatorName;
    private String managerName;
    private String createTime;
    private int order;
}
