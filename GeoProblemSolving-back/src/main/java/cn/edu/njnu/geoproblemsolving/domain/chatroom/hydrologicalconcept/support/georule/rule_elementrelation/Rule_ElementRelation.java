package cn.edu.njnu.geoproblemsolving.domain.chatroom.hydrologicalconcept.support.georule.rule_elementrelation;

import cn.edu.njnu.geoproblemsolving.domain.chatroom.hydrologicalconcept.support.georule.rule_enum.Type_ElementRelation;
import lombok.Data;

import java.util.List;

@Data
public class Rule_ElementRelation {
    String id;
    String from;
    List<String> to;
    Type_ElementRelation type;
    String description;
}
