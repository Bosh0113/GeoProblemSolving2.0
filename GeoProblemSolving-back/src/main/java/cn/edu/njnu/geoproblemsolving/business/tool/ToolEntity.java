package cn.edu.njnu.geoproblemsolving.business.tool;

import cn.edu.njnu.geoproblemsolving.Entity.ModelTools.CModel.support.BaseEntity;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;

/**
 * @ClassName
 * @Description
 * @Author Zhiyi, mzy
 * @Date 2020/5/26  20:06; 2021/4/2
 * @Version 2.0.0
 */
@Document(collection = "Tool")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolEntity {
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
    private ArrayList<String> tags;

    /**
     * others
     */
    private String provider;
    private String privacy;
    private String scope; // 适用于panel打开的内部工具和不适用panel打开的外部工具：inner/outer

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createdTime;

    /**
     * is a toolset?
     */
    private Boolean isToolset;
    private ArrayList<String> toolList;

//    private JSONObject modelInfo; //stateId, oid,mdlId,模型条目？

}
