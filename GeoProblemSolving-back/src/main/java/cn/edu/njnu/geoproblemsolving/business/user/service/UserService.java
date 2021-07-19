package cn.edu.njnu.geoproblemsolving.business.user.service;

import cn.edu.njnu.geoproblemsolving.business.user.entity.UserEntity;
import cn.edu.njnu.geoproblemsolving.business.user.entity.UserVo;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.alibaba.fastjson.JSONObject;
import org.apache.catalina.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface UserService {

    UserEntity findUser(String userId);

    Object updataUserInfo(UserEntity user);

    Object register(UserEntity user);

    String changePassword(String email, String password);

    Object login(String email, String password);

    Object resetPwd(String email, String oldPwd, String newPwd);


    //2021.03.31第二次改写，流程及逻辑都更加合理
    JsonResult registerService(UserEntity user);

    JsonResult sendResetPwdEmail(String email);

    JsonResult resetPwdByCode(String email, String code, String newPwd);

    JsonResult resetPwdByOldPwd(String email, String oldPwd, String newPwd);

    JsonResult loginAndAcquireInfo(String email, String password, String ipAddress);

    JsonResult updateUserInfo(JSONObject userInfo);

    void logout(String userId);

    UserVo getUserVo(String userId);

    JsonResult deleteUserProjectService(HttpServletRequest req);

    // UserEntity registerService(UserEntity user);


    JsonResult uploadAvatar(HttpServletRequest req) throws IOException, ServletException;

    Integer validEmailRegistered(String email);

}
