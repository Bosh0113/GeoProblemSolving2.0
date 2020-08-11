package cn.edu.njnu.geoproblemsolving.domain.activity;

import cn.edu.njnu.geoproblemsolving.domain.activity.enums.ActivityType;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;

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

    /**
     * User
     * creator contains: userId,
     * members contains: userId, role
     * role: creator, administrator, decision-maker, researcher, stakeholder, normal member(workers...), visitor(the public)
     */
    private String creator;
    private JSONArray members;

    /**
     * The type of activity
     */
    private ActivityType type;

    /**
     * The horizontal relationships:
     * last, next (list of aid and protocolId)
     * protocol: definition of relationship
     */
    private JSONArray last;
    private JSONArray next;

    /**
     * The vertical relationships:
     * parent, child (list of aid)
     * pathway: the relationships of child activities: []: independent activities; [{},{}]: dependent activities
     */
    private String parent;
    private ArrayList<String> children;
    private JSONObject pathway;

    /**
     * Level (0~3)
     * level=0: project; level=1: sub-project
     */
    private Integer level;

    /**
     * tools
     * toolsets
     */
    private ArrayList<String> toolList;
    private ArrayList<String> toolsetList;

    /**
     * Time
     */
    private String createdTime;
    private String activeTime;
}

