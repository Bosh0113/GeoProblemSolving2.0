package cn.edu.njnu.geoproblemsolving.comparison.entity;

import cn.edu.njnu.geoproblemsolving.comparison.enums.ProjectType;
import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
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
public class CmpProject{
    String projectId;
    String parentId;
    ProjectType projectType;
    String category;
    List<String> subprojects = new ArrayList<>();

    String title;
    String description;
    String managerId;
    String managerName;
    JSONArray members;
    String createTime;
    String privacy;
    String picture;
    String evaluationRules;
    List<String> evaluatedDataIds = new ArrayList<>(); // 评估数据 可以是观测数据、标准数据或者其他
    String standardInputData;
    String evaluatedData;

    Date startTime;
    Date endTime;
    String location;
    String scale;
    String resolution;
    String timeInterval;
    List<String> modelList = new ArrayList<>(); //todo 模型信息 此处不应该是 String 类型
    List<String> requiredInputsId = new ArrayList<>(); //   必要输入数据
    List<String> outputDataList = new ArrayList<>();//   必要输出数据

    List<String> solutionList = new ArrayList<>(); // 解决方案
}
