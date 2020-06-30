package cn.edu.njnu.geoproblemsolving.domain.hydrologicalconcept.support.georule;

import cn.edu.njnu.geoproblemsolving.domain.hydrologicalconcept.support.georule.rule_enum.Aspect;
import lombok.Data;

import java.util.List;

@Data
public class GeoRule {
    String from;
    List<String> to;
    String type;
    String description;
    Aspect aspect;
}
