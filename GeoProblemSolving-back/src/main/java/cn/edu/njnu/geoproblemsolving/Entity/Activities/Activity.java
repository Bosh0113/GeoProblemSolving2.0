package cn.edu.njnu.geoproblemsolving.Entity.Activities;

import cn.edu.njnu.geoproblemsolving.Entity.Activities.Enums.ActivityPrivacy;
import cn.edu.njnu.geoproblemsolving.Entity.Activities.Enums.ActivityType;
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
     * Privacy control
     */
    private ActivityPrivacy privacy;

    /**
     * Description information
     * picture: descriptive picture
     */
    private String description;
    private String tag;
    private String picture;

    /**
     * The type of activity
     */
    private ActivityType type;

    /**
     * User
     * contains: userId, name, avatar, role
     * role: administrator, decision-maker, researcher, stakeholder, normal member(workers...), visitor(the public)
     */
    private JSONObject creator;
    private JSONArray members;

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
    private JSONObject solvingProcess;

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
     * Others
     * supplement:introduction(level 0), background, limitation, methodology
     */
    private String createTime;
    private String activeTime;
    private JSONObject supplement;
}

