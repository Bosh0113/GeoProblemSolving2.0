package cn.edu.njnu.geoproblemsolving.business.activity.processDriven.setvice;

import cn.edu.njnu.geoproblemsolving.business.user.entity.UserEntity;
import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;

public interface UserDispatch {
    // ArrayList<UserEntity> getRelationUser(ArrayList<String> aids);
    //
    // ArrayList<String> getUserTag(ArrayList<UserEntity> users, String type);

    Object updateActivityMember(String aid, JSONArray members);


}
