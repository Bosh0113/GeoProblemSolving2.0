package cn.edu.njnu.geoproblemsolving.comparison.entity;

import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 10:12 2019/7/12
 * @Modified By:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class CmpSubproject {
    @Id
    String id;
    private String projectId;
    private String subProjectId;
    private String title;
    private String description;
    private String managerId;
    private String managerName;
    private JSONArray members;
    private String createTime;
    private Constraints constraints;
}
