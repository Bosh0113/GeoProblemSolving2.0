package cn.edu.njnu.geoproblemsolving.business.user.dao;

import cn.edu.njnu.geoproblemsolving.business.activity.entity.Project;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourcePojo;
import cn.edu.njnu.geoproblemsolving.business.user.dto.InquiryUserDto;
import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import cn.edu.njnu.geoproblemsolving.business.user.entity.UserDto;
import cn.edu.njnu.geoproblemsolving.business.user.util.ICommonUtil;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import com.alibaba.fastjson.JSONArray;
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
import java.util.LinkedHashMap;
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
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        String userName = (String) remoteUser.get("name");
        remoteUser.remove("name");
        remoteUser.put("userName", userName);
        String organizations = remoteUser.get("organizations").toString();
        remoteUser.remove("organizations");
        remoteUser.put("organization", organizations);
        UserDto localUser = JSONObject.toJavaObject(remoteUser, UserDto.class);
        try {
            mongoTemplate.save(localUser);
        } catch (Exception e) {
            return e;
        }
        return localUser;
    }

    @Override
    public Object saveLocalUser(User user) {
        try {
            User localUser = mongoTemplate.save(user);
            return localUser;
        } catch (Exception e) {
            return e;
        }
    }

    @Override
    public JsonResult getJoinedProjectList(String[] projectIdList) {
        ArrayList<Project> projects = new ArrayList<>();
        for (int i = 0; i < projectIdList.length; i++) {
            try {
                Query query = new Query(Criteria.where("_id").is(projectIdList[i]));
                Project project = mongoTemplate.findOne(query, Project.class);
                projects.add(project);
            } catch (Exception e) {
                return ResultUtils.error(-1, "Fail");
            }
        }
        return ResultUtils.success(projects);
    }

    @Override
    public JsonResult getUserInfo(String key, String value) {
        try {
            Query query = new Query(Criteria.where(key).is(value));
            InquiryUserDto user = mongoTemplate.findOne(query, InquiryUserDto.class);
            if (user == null) {
                return ResultUtils.error(-1, "Fail: user does not exist.");
            } else {
                return ResultUtils.success(user);
            }
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult addUserInfo(JSONObject jsonObject) {
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
            int resCode = (int) registerResult.getBody().get("code");
            if (resCode == 0) {
                User user = registerResult.getBody().getJSONObject("data").toJavaObject(User.class);
                return ResultUtils.success(mongoTemplate.save(user));
            } else if (resCode == -3) {
                return ResultUtils.error(-3, "Fail: user already exists in the database.");
            } else {
                return ResultUtils.error(-2, (String) registerResult.getBody().get("msg"));
            }

            // Confirm
//            User user = JSONObject.toJavaObject(jsonObject, User.class);
//            Query query = new Query(Criteria.where("userId").is(user.getUserId()));
//            User user1 = mongoTemplate.findOne(query, User.class);
//            if(user1 != null) return ResultUtils.error(-3, "Fail: user already exists in the database.");

//            return ResultUtils.success(mongoTemplate.save(user));

        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public User findByUserEmail(String email) {
        return mongoTemplate.findOne(new Query(Criteria.where("email").is(email)), User.class);
    }

    @Override
    public JsonResult uploadUserRes(String uploaderId, ResourcePojo res) {
        Query query = new Query(Criteria.where("userId").is(uploaderId));
        Update update = new Update();
        try {
            User user = mongoTemplate.findOne(query, User.class);
            ArrayList<ResourcePojo> resources = user.getResources();
            if (resources == null) {
                resources = new ArrayList<ResourcePojo>();
            }
            resources.add(res);
            update.set("resources", resources);
            mongoTemplate.updateFirst(query, update, User.class);
            return ResultUtils.success(mongoTemplate.findOne(query, User.class));
        } catch (Exception e) {
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
            for (int i = 0; i < rids.length; i++) {
                for (int index = 0; index < resources.size(); index++) {
                    String address = resources.get(index).getAddress();
                    String rid = address.split("//data//")[1];
                    if (rid.equals(rids[i])) {
                        indexArray.add(index);
                        break;
                    }
                }
            }
            indexArray.sort(Comparator.naturalOrder());
            for (int j = 0; j < indexArray.size(); j++) {
                resources.remove(indexArray.get(j) - j);
            }
            Update update = new Update();
            update.set("resources", resources);
            mongoTemplate.updateFirst(query, update, User.class);
            return ResultUtils.success();

        } catch (Exception e) {
            return ResultUtils.error(-2, "Delete local user field failed.");
        }
    }

    @Override
    public JsonResult sharedUserRes(String email, ArrayList<ResourcePojo> res) {
        Query query = new Query(Criteria.where("email").is(email));
        User user = mongoTemplate.findOne(query, User.class);
        if (user == null) {
            return ResultUtils.error(-3, "No such user, please check if the email is correct.");
        }
        ArrayList<ResourcePojo> resources = user.getResources();
        if (resources == null) {
            resources = new ArrayList<ResourcePojo>();
        }
        for (ResourcePojo resourcePojo : res) {
            resources.add(resourcePojo);
        }
        Update update = new Update();
        update.set("resources", resources);
        mongoTemplate.updateFirst(query, update, User.class);
        //还得更新远端内容，暂时不写

        return ResultUtils.success();
    }

    @Override
    public JsonResult deleteUserProject(HttpServletRequest req) {
        String userId = req.getParameter("userId");
        String projectId = req.getParameter("projectId");
        Update update = new Update();
        Query query = new Query(Criteria.where("userId").is(userId));
        Query projectQuery = new Query(Criteria.where("aid").is(projectId));
        try {
            User user = mongoTemplate.findOne(query, User.class);
            ArrayList<String> joinedProjects = user.getJoinedProjects();
            for (int i = 0; i < joinedProjects.size(); i++) {
                if (joinedProjects.get(i).equals(projectId)) {
                    joinedProjects.remove(i);
                }
            }
            update.set("joinedProjects", joinedProjects);
            mongoTemplate.updateFirst(query, update, User.class);

            //更新项目成员信息
            Project updateProject = mongoTemplate.findOne(projectQuery, Project.class);
            JSONArray members = updateProject.getMembers();
            for (int i=0; i<members.size(); i++){
                String quitUserId = members.getJSONObject(i).getString("userId");
                if (quitUserId.equals(userId)){
                    members.remove(i);
                }
            }
            Update projectUpdate = new Update();
            projectUpdate.set("members", members);
            mongoTemplate.updateFirst(projectQuery, projectUpdate, Project.class);

            return ResultUtils.success();
        }catch (Exception e){
            return ResultUtils.error(-2, "Fail");
        }

    }
}
