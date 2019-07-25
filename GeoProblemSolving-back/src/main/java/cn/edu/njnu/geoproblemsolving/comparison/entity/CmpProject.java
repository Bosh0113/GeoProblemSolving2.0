package cn.edu.njnu.geoproblemsolving.comparison.entity;

import cn.edu.njnu.geoproblemsolving.comparison.enums.ProjectType;
import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 19:42 2019/7/8
 * @Modified By:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class CmpProject {
    @Id
    private String id;
    private String projectId;
    private ProjectType projectType;
    private String title;
    private String description;
    private String category;
    private String managerId;
    private String managerName;
    private JSONArray members;
    private String createTime;
    private String privacy;
    private String picture;
    private String evaluationRules;
    private List<String> evaluatedDataIds; // 评估数据 可以是观测数据、标准数据或者其他
    private List<String> subprojectIds;
    private String standardInputData;
    private String evaluatedData;

    private Date startTime;
    private Date endTime;
    private String location;
    private String scale;
    private String resolution;
    private String timeInterval;
    private List<String> modelList; //todo 模型信息 此处不应该是 String 类型
    private List<String> requiredInputsId; //   必要输入数据
    private List<String> outputDataList;//   必要输出数据
}
