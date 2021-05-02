package cn.edu.njnu.geoproblemsolving.business.user.dao;

import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourcePojo;
import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.query.Update;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public interface IUserDao {
    Object findUserById(String userId);

    Object updateUserInfo(HttpServletRequest req);

    //前端传输用户信息
    Object updateUserInfo(User user);

    Object saveUser(JSONObject user);

    Object saveLocalUser(User user);

    JsonResult getJoinedProjectList(String[] projectIdList);

    JsonResult getUserInfo(String key, String value);

    JsonResult addUserInfo(JSONObject user);

    User findByUserEmail(String email);


    // 用户资源字段 curd 处理
    JsonResult uploadUserRes(String uploaderId, ResourceEntity res);

    JsonResult delUserRes(String userId, String[] rids);

    JsonResult sharedUserRes(String email, ArrayList<ResourceEntity> res);


    //用户项目字段相关
    JsonResult deleteUserProject(HttpServletRequest req);

    //
    // //修改版本内容3.31
    JsonResult saveUser(User user);
    // //此处id可以为 userId 也可以为 email
    // JsonResult updateInfo(String id, Update update);
    //
    // JsonResult findUserByIdOrEmail(String filed);


    User updateInfo(String id, Update update);

    User findUserByIdOrEmail(String filed);

}
