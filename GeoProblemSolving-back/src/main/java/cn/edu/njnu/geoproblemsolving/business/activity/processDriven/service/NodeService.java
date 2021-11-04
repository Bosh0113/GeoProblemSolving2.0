package cn.edu.njnu.geoproblemsolving.business.activity.processDriven.service;

import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity.ActivityNode;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

//节点层操作
public interface NodeService {
    // void putResTagInNode(String aid, ResourceEntity res);
    //
    // void delResourceFromNode(String aid, String uid);
    //
    //
    // void addUserToNode(String aid, String userId);
    //
    // void userExitFromNode(String aid, String userId);
    //
    // void putUserTagInNode(String aid, HashMap<String, String> putInfo);
    // HashMap<String, String> putNodeResource(String aid, ResourceEntity res, String operationType);


    ActivityNode createActivityNode(String aid, Integer level);

    HashMap<String, String> getActivityResourceTag(String aid);

    ActivityNode nodeIsPresent(String aid);

    String getUserTag(String userId, String role);


    //活动与节点之间同步的内容，只关心节点层的内容

    //用户申请加入活动 或 活动中用户状态发生改变的时候
    void addOrPutUserToNode(String aid, String userId, String userRole);

    void addUserToNodeBatch(String aid, HashSet<String> userIds);

    void addUserToNode(String aid, String userId, String role);

    void putUserInfoToNode(String userId, ArrayList<String> organizations, ArrayList<String> domains);

    void putUserInfoToNode(String userId,
                           ArrayList<String> localOrg,
                           ArrayList<String> serverOrg,
                           ArrayList<String> localDomain,
                           ArrayList<String> serverDomain);


    void userExitActivity(String aid, String userId);

    //用户流动到活动
    void userFlowToNode(String aid, String userId);


    //资源相关内容，将可能异常的地方在此层完成屏蔽
    void addResToNode(String aid, ResourceEntity res);

    void addResToNodeBatch(String aid, HashMap<String, String> resInfo);

    void delResInNode(String aid, String uid);

    void delResInNodeBatch(String aid, HashSet<String> uid);

    void putResInNode(String aid, ResourceEntity res);


    //资源的更新

}
