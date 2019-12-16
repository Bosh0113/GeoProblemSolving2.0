package cn.edu.njnu.geoproblemsolving.Entity.Model;

import cn.edu.njnu.geoproblemsolving.Entity.Model.support.TaskData;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "CTask")
@Data
@Builder
public class TaskEntity {
    @Id
    String id;
    String oid;
    String taskId;
    String computableId;
    String computableName;
    String userId;
    String ip;
    int port;

    Date runTime;

    int status;//Started: 1, Finished: 2, Inited: 0, Error: -1

    List<TaskData> inputs;
    List<TaskData> outputs;

}
