package cn.edu.njnu.geoproblemsolving.business.activity.processDriven.service;

import java.util.HashMap;
import java.util.HashSet;

public interface ResourceDispatch {
    // ArrayList<UserEntity> getRelationResource(ArrayList<String> aids);
    //
    // ArrayList<String> getResourceTag(ArrayList<UserEntity> users, String tyge);

    HashMap<String, HashSet<String>> getResourceTagInActivity(HashSet<String> nodeIds);



}
