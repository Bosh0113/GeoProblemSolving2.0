package cn.edu.njnu.geoproblemsolving.business.tool.IToll;

import cn.edu.njnu.geoproblemsolving.business.collaboration.compute.entity.ToolTestData;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;

/**
 * @ClassName Tool
 * @Description
 * @Author zhngzhng
 * @Date 2021/5/13
 **/
@Data
@Document(collection = "Tool")
@AllArgsConstructor
@NoArgsConstructor
public class Tool {
    //新的工具字段，会涉及到工具前端页面的修改
            /*
            共有字段
            工具集
            模型工具
            数据方法工具
             */
    //Id 与 MongoId 注解的不同？？？
    //不同的地方，后面完成了过后 mapping 过来
    //个人空间部分还需要有一定的小修改
    @Id
    String tid;
    String toolName;
    String description;
    //creator 为了和上个版本工具兼容
    String provider;
    String privacy;
    ArrayList<String> recommendation;
    //尽量和上个版本保持一致
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    Date createdTime;
    Boolean toolSet = false;

    //工具集特有，里面存储ids
    ArrayList<String> toolList;
    /*
    工具共有字段
     */
    //计算部分内容提供者
    String backendProvider;
    String backendName;
    //工具属于哪种类型 modelItem,dataService,webTool
    String backendType;
    ArrayList<String> tags;
    //采用和 avatar 一样的存储策略
    String toolImage;
    /*
    适用于panel打开的内部工具和不适用panel打开的外部工具：inner/outer
    ????? 有点问题
    */
    String scope;



    //工具-计算模型特有
    String computableModelId;
    String computableModelMd5;
    JSONObject mdlJson;

    //工具-数据方法特有
    String token;
    String dataMethodId;
    String dataMethodType;
    //数据方法元数据，生成页面
    JSONObject dataMethodMeta;

    // String contributor;
    ArrayList<ToolTestData> toolTestData;

    //Web 工具特有???还有点什么内容喃？
    String toolUrl;

}
