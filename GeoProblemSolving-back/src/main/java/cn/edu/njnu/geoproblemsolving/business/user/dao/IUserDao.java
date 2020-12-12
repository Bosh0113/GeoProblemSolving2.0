package cn.edu.njnu.geoproblemsolving.business.user.dao;

import cn.edu.njnu.geoproblemsolving.business.user.dto.InquiryUserDto;
import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import cn.edu.njnu.geoproblemsolving.business.user.entity.UserDto;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface IUserDao {
    Object findUserById(String userId);

    Object updateUserInfo(HttpServletRequest req);

    //前端传输用户信息
    Object updateUserInfo(User user);

    Object saveUser(JSONObject user);

    Object saveLocalUser(User user);

    JsonResult getMangeProjectList(String[] projectIdList);

    JsonResult getUserInfo(String key, String value);

    JsonResult addUserInfo(User user);
}
