package cn.edu.njnu.geoproblemsolving.business.user.dao;

import cn.edu.njnu.geoproblemsolving.business.activity.entity.Project;
import cn.edu.njnu.geoproblemsolving.business.user.dto.InquiryUserDto;
import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import cn.edu.njnu.geoproblemsolving.business.user.entity.UserDto;
import cn.edu.njnu.geoproblemsolving.business.user.util.ICommonUtil;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Repository
public class IUserImpl implements IUserDao {
    @Autowired
    MongoTemplate mongoTemplate;
    @Override
    public User findUserById(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        User user = mongoTemplate.findOne(query, User.class);
        return user;
    }

    @Override
    public Object updateUserInfo(HttpServletRequest req) {
        try {
            String userId = req.getParameter("userId");
            Query query = new Query(Criteria.where("userId").is(userId));
            ICommonUtil commonUtil = new ICommonUtil();
            Update update = commonUtil.setUpdate(req);
            mongoTemplate.updateFirst(query, update, User.class);
            User newUser = mongoTemplate.findOne(query, User.class);
            return newUser;
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object updateUserInfo(User user) {

        return null;
    }

    @Override
    public Object saveUser(JSONObject remoteUser) {
        //JSONObject to Local User
        //只会把相同字段内容写入，多余字段不会写入
        String userName = (String)remoteUser.get("name");
        remoteUser.remove("name");
        remoteUser.put("userName", userName);
        String organizations = remoteUser.get("organizations").toString();
        remoteUser.remove("organizations");
        remoteUser.put("organization", organizations);
        UserDto localUser = JSONObject.toJavaObject(remoteUser, UserDto.class);
        try{
            mongoTemplate.save(localUser);
        }catch (Exception e){
            return e;
        }
        return localUser;
    }

    @Override
    public Object saveLocalUser(User user) {
        try {
            User localUser = mongoTemplate.save(user);
            return localUser;
        }catch (Exception e){
            return e;
        }
    }

    @Override
    public JsonResult getMangeProjectList(String[] projectIdList) {
        ArrayList<Project> projects = new ArrayList<>();
        for (int i=0; i<projectIdList.length; i++){
            try {
                Query query = new Query(Criteria.where("_id").is(projectIdList[i]));
                Project project = mongoTemplate.findOne(query, Project.class);
                projects.add(project);
            }catch (Exception e){
                return ResultUtils.error(-1,"Fail");
            }
        }
        return ResultUtils.success(projects);
    }

    @Override
    public JsonResult getUserInfo(String key, String value){
        try{
            Query query = new Query(Criteria.where(key).is(value));
            InquiryUserDto user = mongoTemplate.findOne(query, InquiryUserDto.class);
            if(user == null){
                return ResultUtils.error(-1, "Fail: user does not exist.");
            } else {
                return ResultUtils.success(user);
            }
        } catch (Exception ex){
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult addUserInfo(User user){
        try {
            // Confirm
            Query query = new Query(Criteria.where("userId").is(user.getUserId()));
            User user1 = mongoTemplate.findOne(query, User.class);
            if(user1 != null) return ResultUtils.error(-3, "Fail: user already exists in the database.");

            return ResultUtils.success(mongoTemplate.save(user));

        } catch (Exception ex){
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }
}
