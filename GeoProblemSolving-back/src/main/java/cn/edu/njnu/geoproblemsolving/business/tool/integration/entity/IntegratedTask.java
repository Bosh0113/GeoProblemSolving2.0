package cn.edu.njnu.geoproblemsolving.business.tool.integration.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * wzh
 * 集成模型对象，与普通task不同
 * modified by mzy
 */
@Data
@Document(collection = "IntegratedTasks")
public class IntegratedTask {
    @Id
    String id;

    String oid;
    String taskId;
    String taskName;
    String description;

    List<Map<String,String>> models;
    List<Map<String,String>> processingTools;
    List<ModelAction> modelActions;
    List<DataProcessing> dataProcessings;
    List<Map<String,Object>> dataItems;
    List<Map<String,String>> dataLinks;

    String xml;
    String mxGraph;

    String userId;

    int status;//Started: 1, Finished: 2, Inited: 0, Error: -1

    Date createTime;
    Date lastModifiedTime;

    Date lastRunTime;

    /**
     * In activity
     * @author: mzy
     */
    String aid;
}
