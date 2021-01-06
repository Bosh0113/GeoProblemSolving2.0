package cn.edu.njnu.geoproblemsolving.business.tool.support.entity;

import cn.edu.njnu.geoproblemsolving.Entity.ModelTools.CModel.support.TaskData;
import cn.edu.njnu.geoproblemsolving.business.tool.integration.entity.Model;
import cn.edu.njnu.geoproblemsolving.business.tool.integration.entity.ModelParam;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document
@Data
public class TaskRecord {
    @Id
    String id;
    String oid;
    String taskId;
    String computableId;
    String computableName;
    String userId;
    String ip;
    String description;
    int port;
    int loadTime;

    Date runTime;

    int status;//Started: 1, Finished: 2, Inited: 0, Error: -1

//    List<String> isPublic;//public ;noPublic ;userNames; public和noPublic都放数组头
    String permission;
    List<TaskData> inputs;
    List<TaskData> outputs;

    boolean flag = true;
    GeoInfoMeta geoInfoMeta;//一般不需要填

    //集成模型
    Boolean integrate;
    List<Model> models;
    String graphXml;
    List<ModelParam> modelParams;

}
