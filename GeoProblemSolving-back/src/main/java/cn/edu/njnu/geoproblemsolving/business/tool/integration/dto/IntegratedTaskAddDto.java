package cn.edu.njnu.geoproblemsolving.business.tool.integration.dto;

import cn.edu.njnu.geoproblemsolving.business.tool.integration.entity.DataProcessing;
import cn.edu.njnu.geoproblemsolving.business.tool.integration.entity.ModelAction;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class IntegratedTaskAddDto {

    String taskOid;
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
    String mxgraph;
}

