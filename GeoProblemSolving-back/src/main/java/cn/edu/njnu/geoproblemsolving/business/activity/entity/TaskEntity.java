package cn.edu.njnu.geoproblemsolving.business.activity.entity;

import cn.edu.njnu.geoproblemsolving.business.activity.entity.Activity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "Task")
public class TaskEntity {
    private String taskId;
    private String aid; //task 所属 activity
    private String name;
    private String description;
    private Date startTime;
    private Date endTime;
    private String state;   //_todo; doing; done.
    private Integer importance;      //0;1.
    private String type;
    private String taskActivityId; //task 安排的 activity
    private String creatorId;
    private String creatorName;
    private String managerName;
    private String createdTime;
    private int order;
}
