package cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity;

import lombok.Data;

import java.util.ArrayList;

/**
 * @ClassName ActivityRelation
 * @Description 用来记录关系，可用于简化操作
 * @Author zhngzhng
 * @Date 2021/7/23
 **/
@Data
public class ActivityRelation {
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
    String graphId;
    String type;
    ArrayList<String> nodes;
}
