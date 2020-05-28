package cn.edu.njnu.geoproblemsolving.domain.tool;

import cn.edu.njnu.geoproblemsolving.Entity.ModelTools.CModel.support.BaseEntity;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

/**
 * @ClassName
 * @Description
 * @Author Zhiyi
 * @Date 2020/5/26  20:06
 * @Version 1.0.0
 */
@Document(collection = "Tool")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolEntity extends BaseEntity {
    @Id
    private String id;
    private String tId;
    private String toolName;
    private String toolUrl;//stateId, oid,mdlId,模型条目？
    private JSONObject modelInfo; //stateId, oid,mdlId,模型条目？
    private String description; // 可在多个不同的toolset内，或没有dataset
    private ArrayList<String> recomStep; // step类型 or general
    private ArrayList<String> categoryTag;
    private String provider;
    private String privacy;
    private String toolImg;
    private String detail;
}
