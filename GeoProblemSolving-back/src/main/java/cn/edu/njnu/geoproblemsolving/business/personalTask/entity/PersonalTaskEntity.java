package cn.edu.njnu.geoproblemsolving.business.personalTask.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Document(collection = "PersonalTask")
@Component
public class PersonalTaskEntity {
    @Id
    private String ptId;
    private String userId;
    private String content;
    //0, 1
    private String importance;
    //doing, done
    private String state;
    private String startTime;
    //
    private String endTime;


}
