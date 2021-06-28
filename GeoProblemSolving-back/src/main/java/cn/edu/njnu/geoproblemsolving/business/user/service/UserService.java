package cn.edu.njnu.geoproblemsolving.business.user.service;

import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourcePojo;
import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import cn.edu.njnu.geoproblemsolving.business.user.entity.UserVo;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

public interface UserService {

    User findUser(String userId);

    Object updataUserInfo(User user);

    Object register(User user);

    String changePassword(String email, String password);

    Object login(String email, String password);

    JsonResult uploadResourceField(String email, ArrayList<ResourceEntity> res);



    Object resetPwd(String email, String oldPwd, String newPwd);


    //2021.03.31第二次改写，流程及逻辑都更加合理
    JsonResult registerService(User user);

    JsonResult sendResetPwdEmail(String email);

    JsonResult resetPwdByCode(String email, String code, String newPwd);

    JsonResult resetPwdByOldPwd(String email, String oldPwd, String newPwd);

    JsonResult loginAndAcquireInfo(String email, String password, String ipAddress);

    JsonResult updateUserInfo(JSONObject userInfo);

    void logout(String userId);

    UserVo getUserVo(String userId);

    JsonResult deleteUserProjectService(HttpServletRequest req);

    // User registerService(User user);

    //上传头像，返回头像链接，前端给base64
    Object uploadAvatar(String email, String baseStr);


}
