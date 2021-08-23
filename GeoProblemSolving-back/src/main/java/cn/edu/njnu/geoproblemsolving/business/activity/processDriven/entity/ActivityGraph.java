package cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName ActivityGraphEntity
 * @Description 每个根项目对应的图
 * @Author zhngzhng
 * @Date 2021/7/22
 **/
@Data
@Document(value = "activityGraph")
public class ActivityGraph {
    /*
    还得考虑和原来内容的兼容
     */
    /**
     * 加权有向图
     * id: root activity id
     *  当选择活动类型为 Multi 的时候才会存在需要连接的情况，实际上连接的是 Multi 中的子活动
     * adjacencyMap: 这里就是点与点之间的关系，需要注意一下重复边的处理（标签的并操作）
     *  0: [1, relation1] -> [2, relation2]
     *  key: 节点A, activityNode
     *  value:
     *      key: 节点B activityNode
     *      value: 限制性条件（权重）
     */
    @Id
    private String id;
    private HashMap<String, HashMap<String, LinkRestriction>> adjacencyMap;
    Date createDate;

    // //这样存储的话，就是将 linkRestriction 做为核心来处理
    // private HashMap<String, HashMap<String, String>> adjacencyMap;
}
