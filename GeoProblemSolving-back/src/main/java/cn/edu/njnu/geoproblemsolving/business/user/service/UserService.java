package cn.edu.njnu.geoproblemsolving.business.user.service;

import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourcePojo;
import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

public interface UserService {

    User findUser(String userId);

    Object updataUserInfo(User user);

    Object register(User user);

    String changePassword(String email, String password);

    Object login(String email, String password);

    JsonResult uploadResourceField(String email, ArrayList<ResourcePojo> res);

    Object sendResetPwdEmail(String email);

    Object resetPwd(String email, String oldPwd, String newPwd);
}
