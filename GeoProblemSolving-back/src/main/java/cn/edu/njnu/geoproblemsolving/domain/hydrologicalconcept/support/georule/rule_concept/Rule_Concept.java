package cn.edu.njnu.geoproblemsolving.domain.hydrologicalconcept.support.georule.rule_concept;

import cn.edu.njnu.geoproblemsolving.domain.hydrologicalconcept.support.georule.rule_enum.Type_Concept;
import lombok.Data;

import java.util.List;

@Data
public class Rule_Concept {
    String id;
    String from;
    List<String> to;
    Type_Concept type;
    String description;
}
