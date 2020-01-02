package cn.edu.njnu.geoproblemsolving.Dao.User;


//import cn.edu.njnu.geoproblemsolving.Dao.Method.AESUtils;
import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import cn.edu.njnu.geoproblemsolving.Entity.ProjectEntity;
import cn.edu.njnu.geoproblemsolving.Entity.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.Entity.SubProjectEntity;
import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;
import cn.edu.njnu.geoproblemsolving.View.StaticPagesBuilder;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Component
public class UserDaoImpl implements IUserDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public String saveUser(UserEntity user) {
        Query queryEmail = Query.query(Criteria.where("email").is(user.getEmail()));
        if (!mongoTemplate.find(queryEmail, UserEntity.class).isEmpty()) {
            return "Email";
        } else {
            mongoTemplate.save(user);
            return user.getUserId();
        }
    }

    @Override
    public Object readUser(String key, String value) {
        Query query = Query.query(Criteria.where(key).is(value));
        if (mongoTemplate.find(query, UserEntity.class).isEmpty()) {
            return "None";
        } else {
            UserEntity user = mongoTemplate.findOne(query, UserEntity.class);

            user.setPassword("");
            return user;
        }
    }

    @Override

    public void removeUser(String key, String value) {
        Query query = Query.query(Criteria.where(key).is(value));
        mongoTemplate.remove(query, "User");
    }

    @Override
    public Object updateUser(HttpServletRequest request) {
        try {
            String userId = request.getParameter("userId");
            Query query = new Query(Criteria.where("userId").is(userId));
            CommonMethod method = new CommonMethod();
            Update update = method.setUpdate(request);
            mongoTemplate.updateFirst(query, update, UserEntity.class);
            UserEntity newUser = mongoTemplate.findOne(query, UserEntity.class);
            newUser.setPassword("");
            try {
                String newUserName = request.getParameter("userName");
                updateAllAboutUserWithUserName(newUser,newUserName);
            }catch (Exception ignored){}
            return newUser;
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public Object updatePassword(String email, String password) {
        try {
            if (!isRegistered(email)) {
                return "None";
            } else {
                Query query = new Query(Criteria.where("email").is(email));
                Update updatePassword = new Update();
                updatePassword.set("password", password);
                mongoTemplate.updateFirst(query, updatePassword, UserEntity.class);
                return mongoTemplate.findOne(query, UserEntity.class);
            }
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public Object login(String email, String password) {
        if (isRegistered(email)) {
//            AESUtils aesUtils = new AESUtils();
//            password = aesUtils.decrypt(password);
            if (verifyPassword(email, password)) {
                Query query = new Query(Criteria.where("email").is(email));
                UserEntity user = mongoTemplate.findOne(query, UserEntity.class);
                user.setPassword("");
                return user;
            } else {
                return "Password";
            }
        } else {
            return "Email";
        }
    }

    @Override
    public Boolean isRegistered(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        return !mongoTemplate.find(query, UserEntity.class).isEmpty();
    }

    @Override
    public Boolean verifyPassword(String email, String password) {
        Query query = new Query(Criteria.where("email").is(email).and("password").is(password));
        return !mongoTemplate.find(query, UserEntity.class).isEmpty();
    }

    private void updateAllAboutUserWithUserName(UserEntity userEntity,String newUserName){
        String userId = userEntity.getUserId();
        JSONArray joinedProjects = userEntity.getJoinedProjects();
        for (int i=0;i<joinedProjects.size();i++){
            JSONObject joinedProject = joinedProjects.getJSONObject(i);
            String projectId = joinedProject.getString("projectId");
            Query queryProject = Query.query(Criteria.where("projectId").is(projectId));
            ProjectEntity projectEntity = mongoTemplate.findOne(queryProject,ProjectEntity.class);
            JSONArray members = projectEntity.getMembers();
            for (int j=0;j<members.size();j++){
                JSONObject member = members.getJSONObject(j);
                if(member.getString("userId").equals(userId)){
                    member.put("userName",newUserName);
                    members.remove(j);
                    members.add(j,member);
                    Update updateProject = new Update();
                    updateProject.set("members",members);
                    mongoTemplate.updateFirst(queryProject,updateProject,ProjectEntity.class);
                    StaticPagesBuilder staticPagesBuilder = new StaticPagesBuilder(mongoTemplate);
                    staticPagesBuilder.projectDetailPageBuilder(projectId);
                    break;
                }
            }
            updateAllSubProjectForUserName(projectId,userId,newUserName);
        }

        JSONArray manageProjects = userEntity.getManageProjects();
        for (int i=0;i<manageProjects.size();i++){
            JSONObject manageProject = manageProjects.getJSONObject(i);
            String projectId = manageProject.getString("projectId");
            Query queryProject = Query.query(Criteria.where("projectId").is(projectId));
            Update updateProject = new Update();
            updateProject.set("managerName",newUserName);
            mongoTemplate.updateFirst(queryProject,updateProject,ProjectEntity.class);
            StaticPagesBuilder staticPagesBuilder = new StaticPagesBuilder(mongoTemplate);
            staticPagesBuilder.projectDetailPageBuilder(projectId);
            updateAllSubProjectForUserName(projectId,userId,newUserName);
        }

        Query queryResource = Query.query(Criteria.where("uploaderId").is(userId));
        List<ResourceEntity> resourceEntities = mongoTemplate.find(queryResource,ResourceEntity.class);
        for (ResourceEntity resourceEntity:resourceEntities){
            String resourceId = resourceEntity.getResourceId();
            Query query = Query.query(Criteria.where("resourceId").is(resourceId));
            Update update = new Update();
            update.set("uploaderName",newUserName);
            mongoTemplate.updateFirst(query,update,ResourceEntity.class);
        }
    }

    private void updateAllSubProjectForUserName(String projectId,String userId,String newUserName){
        Query querySubProjects =Query.query(Criteria.where("projectId").is(projectId));
        List<SubProjectEntity> subProjectEntities = mongoTemplate.find(querySubProjects,SubProjectEntity.class);
        for (SubProjectEntity subProjectEntity:subProjectEntities){
            String subProjectId = subProjectEntity.getSubProjectId();
            Query querySubProject = Query.query(Criteria.where("subProjectId").is(subProjectId));
            String managerId = subProjectEntity.getManagerId();
            if (managerId.equals(userId)){
                Update update = new Update();
                update.set("managerName",newUserName);
                mongoTemplate.updateFirst(querySubProject,update,SubProjectEntity.class);
            }else {
                JSONArray members = subProjectEntity.getMembers();
                for (int i=0;i<members.size();i++){
                    JSONObject member = members.getJSONObject(i);
                    if (member.getString("userId").equals(userId)){
                        member.put("userName",newUserName);
                        members.remove(i);
                        members.add(i,member);
                        Update update = new Update();
                        update.set("members",members);
                        mongoTemplate.updateFirst(querySubProject,update,SubProjectEntity.class);
                        break;
                    }
                }
            }
        }
    }
}
