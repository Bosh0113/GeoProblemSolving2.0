package cn.edu.njnu.geoproblemsolving.business.collaboration.utils;

import cn.edu.njnu.geoproblemsolving.business.collaboration.entity.CollaborationUser;
import cn.edu.njnu.geoproblemsolving.business.collaboration.enums.CollaborationMode;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @ author: mzy
 * @ date: 2021/4
 * @ description: the mode, origin, scope and destination of collaboration
 */
@Data
public class CollaborationConfig {

    /**
     * Basic
     */
    private String aid;

    /**
     * mode: Free / Semi-free(Occupy/Apply)
     * operator: current person who can send the message
     * applyQueue: people who are waiting for operation - for semi-free mode
     */
    private CollaborationMode mode;
    private String operator;
    private List<String> applyQueue;
    private HashMap<String, CollaborationUser> participants;

    public CollaborationConfig(String aid){
        this.aid = aid;
    }

}
