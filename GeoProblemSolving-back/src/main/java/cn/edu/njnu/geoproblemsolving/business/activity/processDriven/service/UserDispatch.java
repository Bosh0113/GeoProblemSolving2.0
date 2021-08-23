package cn.edu.njnu.geoproblemsolving.business.activity.processDriven.service;

import com.alibaba.fastjson.JSONArray;

import java.util.HashSet;

public interface UserDispatch {
    // ArrayList<UserEntity> getRelationUser(ArrayList<String> aids);
    //
    // ArrayList<String> getUserTag(ArrayList<UserEntity> users, String type);

    Object updateActivityMember(String aid, JSONArray members);

    Object getNodeUserTag(HashSet<String> nodeIds);

    Object getUserTag(String userId);

    boolean checkUserIsApproved(String graphId, String nodeId, String userId);

}
