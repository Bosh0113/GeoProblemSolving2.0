package cn.edu.njnu.geoproblemsolving.business.activity.service;

import cn.edu.njnu.geoproblemsolving.business.activity.entity.Activity;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;
import org.dom4j.DocumentException;

import java.util.HashMap;
import java.util.HashSet;

public interface ActivityDocParser {
    //

    /*
        Document level operation
        default: public
     */
    void initActivityDoc(Activity activity);

    void initActivityDoc(String aid, String level);

    void changeActivityType(String aid, String type);


    //=================Generic operation==============================================================
    /*
    Generic operation
    Common item between multi and signal activity:
    Participants
    ResourceCollection
    OperationRecords(Multi: process, activity, communication; Signal: resource, tool, communication, geo-analysis)
     */

    /*
        1. Participants behaviors
     */
    //domain & organization,do not read from the node
    //the more the number of reads and writes, the easier it is to cause out of synchronization.
    Object inActivity(String aid, HashMap<String, String> userInfo) throws DocumentException;

    //Generate domain and organization form user server.
    Object outActivity(String aid, String userId);

    Object rolePut(String aid, String userId, String role);

    Object userDomainPut(String aid, String userId, HashSet<String> domains);

    Object userOrganizationPut(String aid, String userId, HashSet<String> organizations);
    /*
        2.Resources related operations
     */
    Object uploadResource(String aid, HashMap<String, String> resInfo) throws DocumentException;

    Object uploadResource(String aid, ResourceEntity res) throws DocumentException;

    Object putRes(String aid, HashMap<String, String> putInfo);

    Object removeRes(String aid, String rid);

    //========================Multi activity operation===================================================
    /*
        1.Operation records
     */



    /*
        2. ChildActivities and activity dependencies
     */

    //=======================Signal activity operation===================================================
    /*
        1.Tool related operation, include toolbox
     */

    /*
        2.Task list
     */
    Object bindOperation(String aid, String oid);

    Object unbindOperation(String aid, String oid);

    /*
        3. Task dependency
     */
    Object linkTask(String aid, String fromId, String toId);

    /*
        3.Operation records, remain geo-analysis.
     */




    //service

}
