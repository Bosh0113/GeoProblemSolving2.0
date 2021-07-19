package cn.edu.njnu.geoproblemsolving.business.activity.entity;

import cn.edu.njnu.geoproblemsolving.business.activity.enums.ResProtocol;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.RoleProtocol;
import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@Document(collection = "LinkProtocols")
public class LinkProtocol implements Serializable {
    @Id
    private String pid;

    /**
     * @ Resources
     * type of resource protocol
     * how to update: auto/manual
     * type of resources
     * format
     * sematic (key words/tags)
     * scale
     * spatiotemporal reference
     * unit
     */
    private ResProtocol resProtocol;
    private boolean autoUpdate;
    private ArrayList<String> types;
    private ArrayList<String> formats;
    private ArrayList<String> concepts;
    private ArrayList<String> scales;
    private ArrayList<String> references;
    private ArrayList<String> units;

    /**
     * @ Participants
     * type of user protocol
     * the type of accept roles: manager, decision-maker, researcher, stakeholder, normal member(workers...), visitor(the public)
     * domain of participants
     */
    private RoleProtocol roleProtocol;
    private ArrayList<String> roles;
    private ArrayList<String> domains;
    private ArrayList<String> organizations;


    /**
     * 上面是用户与资源具体的限制
     * 下面内容用于描述协议关系
     * type: 汇聚(n:1)、分散(1:n)、环状(n)、顺序(n)
     * nodes: 用于存储与协议相关联的节点，存储形式如下：
     * 汇聚与分散：第一个元素均存 1 节点，其他节点无关顺序
     * 环状：无关顺序
     * 顺序：按顺序存储即可
     */
    private ProtocolType type;
    private JSONArray nodes;

}
