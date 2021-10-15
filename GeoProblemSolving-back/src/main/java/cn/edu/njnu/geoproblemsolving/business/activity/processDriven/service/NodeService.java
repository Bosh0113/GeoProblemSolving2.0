package cn.edu.njnu.geoproblemsolving.business.activity.processDriven.service;

import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity.ActivityNode;

import java.util.HashMap;

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

    ActivityNode createActivityNode(String aid, Integer level);

    HashMap<String, String> getActivityResourceTag(String aid);

    ActivityNode nodeIsPresent(String aid);

    String getUserTag(String userId, String role);


    //活动与节点之间同步的内容，只关心节点层的内容

    //用户申请加入活动 或 活动中用户状态发生改变的时候
    void addOrPutUserToNode(String aid, String userId, String userRole);

    void userExitActivity(String aid, String userId);

    //用户流动到活动
    void userFlowToNode(String aid, String userId);

}
