package cn.edu.njnu.geoproblemsolving.incomparison.entity;

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
    private String title;
    private String description;
    private String category;
    private String managerId;
    private String managerName;
    private JSONArray members;
    private String createTime;
    private String privacy;
    private String picture;
    private Constraints constraints;
    private String evaluationRules;
    private List<String> evaluatedDataIds; // 评估数据 可以是观测数据、标准数据或者其他
    private List<String> subprojectIds;
}
