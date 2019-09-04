package cn.edu.njnu.geoproblemsolving.comparison.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 21:39 2019/8/1
 * @Modified By:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class CmpSolution {
    String solutionId;
    String projectId;
    String name;
    String description;
    String ownerId;
    String ownerName;
    String createTime;

    //todo 取个什么名字好呢？
    String objType;
    String objId;
    String privacy;
    List<String> instances; // 结果实例
}
