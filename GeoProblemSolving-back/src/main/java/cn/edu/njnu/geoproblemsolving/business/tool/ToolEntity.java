package cn.edu.njnu.geoproblemsolving.business.tool;

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
    /**
     * basic info
     */
    @Id
    private String tid;
    private String toolName;
    private String toolUrl;//stateId, oid,mdlId,模型条目？

    /**
     * description
     */
    private String toolImg;
    private String description;
    private ArrayList<String> recommendation; // activity类型 or general
    private ArrayList<String> tag;

    /**
     * others
     */
    private String provider;
    private String privacy;
    private String scope; // 适用于panel打开的内部工具和不适用panel打开的外部工具：inner/outer

//    private JSONObject modelInfo; //stateId, oid,mdlId,模型条目？
}
