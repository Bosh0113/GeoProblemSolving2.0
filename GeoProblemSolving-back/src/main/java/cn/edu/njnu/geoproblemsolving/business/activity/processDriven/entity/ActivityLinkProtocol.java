package cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity;

import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity.LinkRestriction;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

/**
 * @ClassName ActivityLinkProtocol
 * @Description 活动连接层内容
 * 关系可以重叠，但是图的边不能重叠
 * 该节点属于那个关系并不重要，重要的是边
 *
 * 看来还是得入库，这里有有点问题
 *
 * 不入库的话，模式就是
 * 删除可以随便删除，但是新建只能选择关系进行删除
 *
 * 几类关系只是为了简化构建过程
 *
 * @Author zhngzhng
 * @Date 2021/7/23
 **/
@Data
@Document(collection = "activityLinkProtocol")
//使用这个对象去接收前端传过来的数据，就不做具体的存储了
public class ActivityLinkProtocol {
    /**
     * Description the node relation.
     * 下面内容用于描述协议关系
     * graphId: root activity's id.
     * type: 汇聚(n:1)、分散(1:n)、环状(n)、顺序(n)
     * nodes: 用于存储与协议相关联的节点，存储形式如下：
     * 汇聚与分散：第一个元素均存 1 节点，其他节点无关顺序
     * 环状：无关顺序
     * 顺序：按顺序存储即可
     * 上述四种类型就已经包含了流动方向了
     * 双向就不考虑进来了，只考虑上述的四种类型
     * 后台可以考虑进行优化存储
     */
    @Id
    String id;
    private String graphId;
    private String type;
    private ArrayList<String> nodes;
    private LinkRestriction restriction;
}
