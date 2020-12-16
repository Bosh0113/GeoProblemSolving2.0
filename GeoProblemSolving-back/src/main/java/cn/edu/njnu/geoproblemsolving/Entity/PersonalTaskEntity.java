package cn.edu.njnu.geoproblemsolving.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "PersonalTask")
public class PersonalTaskEntity {
    @Id
    private String ptId;
    private String userId;
    private String content;
    //0, 1
    private String importance;
    //doing, done
    private String state;
    private Date startTime;
    //
    private Date endTime;


}
