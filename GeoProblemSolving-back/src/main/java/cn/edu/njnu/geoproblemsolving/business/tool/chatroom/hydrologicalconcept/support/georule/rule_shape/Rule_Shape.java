package cn.edu.njnu.geoproblemsolving.business.tool.chatroom.hydrologicalconcept.support.georule.rule_shape;


import cn.edu.njnu.geoproblemsolving.business.tool.chatroom.hydrologicalconcept.support.georule.rule_enum.Type_Shape;
import lombok.Data;

import java.util.List;

@Data
public class Rule_Shape {
    String id;
    String from;
    List<String> to;
    Type_Shape type;
    String description;
}
