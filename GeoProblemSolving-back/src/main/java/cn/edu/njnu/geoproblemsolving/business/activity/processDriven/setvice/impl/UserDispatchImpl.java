package cn.edu.njnu.geoproblemsolving.business.activity.processDriven.setvice.impl;

import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.setvice.UserDispatch;
import cn.edu.njnu.geoproblemsolving.business.user.dao.Impl.UserDaoImpl;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserDispatch
 * @Description 活动间用户调度中心
 * @Author zhngzhng
 * @Date 2021/7/21
 **/
@Service
public class UserDispatchImpl implements UserDispatch {
    @Autowired
    UserDaoImpl userDao;

    @Override
    public Object updateActivityMember(String aid, JSONArray members) {
        for (int i = 0; i < members.size(); i++){
            JSONObject member = JSONObject.parseObject(JSONObject.toJSONString(members.get(i)));
            String userId = member.getString("userId");
            member.getString("role");
        }
        return null;
    }
}
