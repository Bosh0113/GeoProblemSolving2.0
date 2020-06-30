package cn.edu.njnu.geoproblemsolving.domain.hydrologicalconcept.support.georule.rule_spaceposition;

import cn.edu.njnu.geoproblemsolving.domain.hydrologicalconcept.support.georule.rule_enum.Type_SpacePosition;
import lombok.Data;

import java.util.List;

@Data
public class Rule_SpacePosition {
    String id;
    String from;
    List<String> to;
    Type_SpacePosition type;
    String description;
}
