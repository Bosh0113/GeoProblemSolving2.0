package cn.edu.njnu.geoproblemsolving.domain.chatroom.hydrologicalconcept.support.georule.rule_process;

import cn.edu.njnu.geoproblemsolving.domain.chatroom.hydrologicalconcept.support.georule.rule_enum.Type_Process;
import lombok.Data;

import java.util.List;

@Data
public class Rule_Process {
    String id;
    String from;
    List<String> to;
    Type_Process type;
    String description;
}
