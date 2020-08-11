package cn.edu.njnu.geoproblemsolving.domain.activity;

import cn.edu.njnu.geoproblemsolving.domain.activity.enums.ProtocolType;
import cn.edu.njnu.geoproblemsolving.domain.user.enums.RoleProtocol;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@Document(collection = "LinkProtocols")
public class LinkProtocol implements Serializable {
    @Id
    private String pid;

    /**
     * Behavior
     * type of protocol: Siblings, Twins and Cousins
     * last and next: contains aid and name (for Siblings)
     */
    private ProtocolType type;
    private JSONObject last;
    private JSONObject next;

    /**
     * Resources
     * type of link resources
     * how to update: auto/manual
     */
    private ArrayList<String> linkRes;
    private boolean autoUpdate;

    /**
     * Participants
     * type of user engagement
     * the type of accept roles: administrator, decision-maker, researcher, stakeholder, normal member(workers...), visitor(the public)
     */
    private RoleProtocol roles;
    private ArrayList<String> acceptRoles;
}
