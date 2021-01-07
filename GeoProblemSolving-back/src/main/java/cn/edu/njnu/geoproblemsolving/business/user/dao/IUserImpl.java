package cn.edu.njnu.geoproblemsolving.business.user.dao;

import cn.edu.njnu.geoproblemsolving.business.activity.entity.Project;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourcePojo;
import cn.edu.njnu.geoproblemsolving.business.user.dto.InquiryUserDto;
import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import cn.edu.njnu.geoproblemsolving.business.user.entity.UserDto;
import cn.edu.njnu.geoproblemsolving.business.user.util.ICommonUtil;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

@Repository
public class IUserImpl implements IUserDao {
    @Autowired
    MongoTemplate mongoTemplate;
    @Value("${authServerIp}")
    String authServerIp;
    @Override
    public User findUserById(String userId) {
        //错误处理
        try {
            Query query = new Query(Criteria.where("userId").is(userId));
            User user = mongoTemplate.findOne(query, User.class);
            return user;
        }catch (Exception e){
            throw e;
        }
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
    public JsonResult addUserInfo(JSONObject jsonObject){
        try {
            RestTemplate restTemplate = new RestTemplate();
//            LinkedMultiValueMap<String, Object> valueMap = new LinkedMultiValueMap<>();
//            for (Map.Entry entry : jsonObject.entrySet()){
//                String filedName =  (String)entry.getKey();
//                valueMap.add(filedName, entry.getValue());
//            }
            String url = "http://" + authServerIp + "/AuthServer/user/add";
            HttpHeaders httpHeaders = new HttpHeaders();
            MediaType mediaType = MediaType.parseMediaType("application/json;charset=UTF-8");
            httpHeaders.setContentType(mediaType);
            HttpEntity<Object> httpEntity = new HttpEntity<>(jsonObject.toString(), httpHeaders);
            ResponseEntity<JSONObject> registerResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, JSONObject.class);
            int resCode = (int)registerResult.getBody().get("code");
            if (resCode == 0){
                User user = JSONObject.toJavaObject(jsonObject, User.class);
                Query query = new Query(Criteria.where("userId").is(user.getUserId()));
                User user1 = mongoTemplate.findOne(query, User.class);
                return ResultUtils.success(mongoTemplate.save(user));
            }else if (resCode == -3){
                return ResultUtils.error(-3, "Fail: user already exists in the database.");
            }else {
                return ResultUtils.error(-2, (String) registerResult.getBody().get("msg"));
            }

            // Confirm
//            User user = JSONObject.toJavaObject(jsonObject, User.class);
//            Query query = new Query(Criteria.where("userId").is(user.getUserId()));
//            User user1 = mongoTemplate.findOne(query, User.class);
//            if(user1 != null) return ResultUtils.error(-3, "Fail: user already exists in the database.");

//            return ResultUtils.success(mongoTemplate.save(user));

        } catch (Exception ex){
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult uploadUserRes(String uploaderId, ResourcePojo res) {
        Query query = new Query(Criteria.where("userId").is(uploaderId));
        Update update = new Update();
        // update.set("resources", res);
        try {
            User user = mongoTemplate.findOne(query, User.class);
            ArrayList<ResourcePojo> resources = user.getResources();
            if (resources == null){
                resources = new ArrayList<ResourcePojo>();
            }
            resources.add(res);
            update.set("resources", resources);
            mongoTemplate.updateFirst(query, update, User.class);
            return ResultUtils.success(mongoTemplate.findOne(query, User.class));
        }catch (Exception e){
            return ResultUtils.error(-2, "Failed to update user resource filed.");
        }
    }

    @Override
    public JsonResult delUserRes(String userId, String[] rids) {
        Query query = new Query(Criteria.where("userId").is(userId));
        User user = mongoTemplate.findOne(query, User.class);
        try {
            ArrayList<ResourcePojo> resources = user.getResources();
            //ArrayList 的动态删除
            ArrayList<Integer> indexArray = new ArrayList<>();
            for (int i = 0; i < rids.length; i++){
                for (int index =0; index < resources.size(); index++){
                    if (resources.get(index).getUid().equals(rids[i])){
                        indexArray.add(index);
                        break;
                    }
                }
            }
            indexArray.sort(Comparator.naturalOrder());
            for (int j=0; j < indexArray.size(); j++){
                resources.remove(indexArray.get(j) - j);
            }
            Update update = new Update();
            update.set("resources", resources);
            mongoTemplate.updateFirst(query ,update, User.class);
            return ResultUtils.success();

        }catch (Exception e){
            return ResultUtils.error(-2, "Delete local user field failed.");
        }
    }
}
