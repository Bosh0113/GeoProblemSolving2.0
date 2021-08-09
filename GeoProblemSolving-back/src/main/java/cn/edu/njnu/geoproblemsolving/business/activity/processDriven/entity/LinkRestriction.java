package cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity;

import cn.edu.njnu.geoproblemsolving.business.activity.enums.ResProtocol;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.RoleProtocol;
import lombok.Data;

import java.util.ArrayList;

/**
 * @ClassName LinkRestriction
 * @Description 限制性条件
 * @Author zhngzhng
 * @Date 2021/7/26
 **/
@Data
public class LinkRestriction {
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
    private ArrayList<String> organizations;

}
