package cn.edu.njnu.geoproblemsolving.business.user.dao;

import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import cn.edu.njnu.geoproblemsolving.business.user.entity.UserDto;
import cn.edu.njnu.geoproblemsolving.business.user.util.ICommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;

@Repository
public class IUserDtoImpl implements IUserDtoDao{
    @Autowired
    MongoTemplate mongoTemplate;
    @Override
    public UserDto findUserById(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        UserDto user = mongoTemplate.findOne(query, UserDto.class);
        return user;
    }

    @Override
    public Object updateUserInfo(HttpServletRequest req) {
        try {
            String userId = req.getParameter("userId");
            Query query = new Query(Criteria.where("userId").is(userId));
            ICommonUtil commonUtil = new ICommonUtil();
            Update update = commonUtil.setUpdate(req);
            mongoTemplate.updateFirst(query, update, UserDto.class);
            UserDto newUser = mongoTemplate.findOne(query, UserDto.class);
            return newUser;
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object updateUserInfo(UserDto userDto) {

        return null;
    }
}
