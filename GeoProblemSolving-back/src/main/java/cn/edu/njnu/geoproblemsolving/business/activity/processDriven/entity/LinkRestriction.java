package cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity;

import cn.edu.njnu.geoproblemsolving.business.activity.enums.ResProtocol;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.RoleProtocol;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;

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
    private ResProtocol resProtocol = ResProtocol.None;
    private boolean autoUpdate;
    private HashSet<String> types;
    private HashSet<String> formats;
    private HashSet<String> concepts;
    private HashSet<String> scales;
    private HashSet<String> references;
    private HashSet<String> units;


    /**
     * @ Participants
     * type of user protocol
     * the type of accept roles: manager, decision-maker, researcher, stakeholder, normal member(workers...), visitor(the public)
     * domain of participants
     */
    private RoleProtocol roleProtocol = RoleProtocol.None;
    private HashSet<String> roles;
    private HashSet<String> domains;
    private HashSet<String> organizations;

}
