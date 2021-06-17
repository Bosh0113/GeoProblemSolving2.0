package cn.edu.njnu.geoproblemsolving.business.activity.entity;

import cn.edu.njnu.geoproblemsolving.business.activity.enums.ResProtocol;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.RoleProtocol;
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
     * @ Resources
     * type of resource protocol
     * how to update: auto/manual
     * type of resources
     * format
     * sematic (key words/tags)
     * scale
     * spatiotemporal reference
     * unit
     */
    private ResProtocol resProtocol;
    private boolean autoUpdate;
    private ArrayList<String> types;
    private ArrayList<String> formats;
    private ArrayList<String> concepts;
    private ArrayList<String> scales;
    private ArrayList<String> references;
    private ArrayList<String> units;

    /**
     * @ Participants
     * type of user protocol
     * the type of accept roles: manager, decision-maker, researcher, stakeholder, normal member(workers...), visitor(the public)
     * domain of participants
     */
    private RoleProtocol roleProtocol;
    private ArrayList<String> roles;
    private ArrayList<String> domains;
}
