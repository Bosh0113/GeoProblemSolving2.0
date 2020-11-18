package cn.edu.njnu.geoproblemsolving.business.user.dao;

import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import cn.edu.njnu.geoproblemsolving.business.user.entity.UserDto;
import cn.edu.njnu.geoproblemsolving.business.user.util.ICommonUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;

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
}
