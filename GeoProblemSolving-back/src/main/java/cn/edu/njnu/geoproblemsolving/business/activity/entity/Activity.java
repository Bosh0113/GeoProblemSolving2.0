package cn.edu.njnu.geoproblemsolving.business.activity.entity;

import cn.edu.njnu.geoproblemsolving.business.activity.enums.ActivityType;
import cn.edu.njnu.geoproblemsolving.business.tool.generalTool.entity.Tool;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Data
@Document(collection = "Activities")
public class Activity implements Serializable {

    /**
     * Basic information
     */
    @Id
    private String aid;
    private String name;

    /**
     * Description information
     */
    private String description;
    private String purpose;

    /**
     * UserEntity
     * creator contains: userId,
     * members contains: userId, role
     * role: (Leader) manager // (Technique) researcher, expert(e.g. engineer), decision-maker, core-member(students, workers...) // (Awareness) stakeholder, consultant, ordinary-member // visitor(the public)
     */
    private String creator;
    private JSONArray members;

    /**
     * The type of activity
     */
    private ActivityType type;

    /**
     * The vertical relationships:
     * parent, child (list of aid)
     * pathway: the relationships of child activities: []: independent activities; [{},{}]: dependent activities
     */
    private String parent;
    private ArrayList<String> children;
    private JSONArray pathway;

    /**
     * Permission Manager
     */
    private String permission;

    /**
     * Level (0~3)
     * level=0: project; level=1: sub-project
     */
    private Integer level;

    /**
     * Time
     */
    private String createdTime;
    private String activeTime;


//    /**
//     * The horizontal relationships:
//     * last, next (list of aid and protocolId)
//     * protocol: definition of relationship
//     */
//    private JSONArray last;
//    private JSONArray next;
//    private String protocolId;

    private HashSet<String> toolList;
}

