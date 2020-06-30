package cn.edu.njnu.geoproblemsolving.Entity.ModelTools.ToolRecords;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @ClassName ToolRecordsEntity
 * @Description
 * @Author Zhiyi
 * @Date 2019/12/19  23:11
 * @Version 1.0.0
 */
@Data
@Document(collection = "ToolRecords")
public class ToolRecordsEntity {
    @Id
    String id;
    String modelInstanceId;
    String userId;
    String stepId;
}
