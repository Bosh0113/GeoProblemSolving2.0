package cn.edu.njnu.geoproblemsolving.business.activity.processDriven.setvice;

import cn.edu.njnu.geoproblemsolving.business.user.entity.UserEntity;

import java.util.ArrayList;

public interface ResourceDispatch {
    ArrayList<UserEntity> getRelationResource(ArrayList<String> aids);

    ArrayList<String> getResourceTag(ArrayList<UserEntity> users, String tyge);



}
