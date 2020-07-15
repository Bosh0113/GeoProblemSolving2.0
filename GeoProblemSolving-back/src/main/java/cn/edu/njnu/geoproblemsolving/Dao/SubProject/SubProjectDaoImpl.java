package cn.edu.njnu.geoproblemsolving.Dao.SubProject;

import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import cn.edu.njnu.geoproblemsolving.Entity.Resources.FolderEntity;
import cn.edu.njnu.geoproblemsolving.Entity.ProjectEntity;
import cn.edu.njnu.geoproblemsolving.Entity.SubProjectEntity;
import cn.edu.njnu.geoproblemsolving.Entity.User;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class SubProjectDaoImpl implements ISubProjectDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public SubProjectDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Object createSubProject(SubProjectEntity subProject) {
        try {
            Date data = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String subProjectId = UUID.randomUUID().toString();
            subProject.setSubProjectId(subProjectId);
            subProject.setMembers(new JSONArray());
            subProject.setCreateTime(dateFormat.format(data));

            Query queryUser = Query.query(Criteria.where("userId").is(subProject.getManagerId()));
            User managerInfo = mongoTemplate.findOne(queryUser, User.class);
            subProject.setManagerName(managerInfo.getUserName());
            mongoTemplate.save(subProject);

            FolderEntity folderEntity = new FolderEntity();
            folderEntity.setScopeId(subProjectId);
            folderEntity.setFolders(new ArrayList<>());
            folderEntity.setFiles(new ArrayList<>());
            folderEntity.setFolderName(subProject.getTitle());
            folderEntity.setParentId("");
            folderEntity.setFolderId(subProject.getSubProjectId());
            mongoTemplate.save(folderEntity);

            Query query = new Query(Criteria.where("projectId").is(subProject.getProjectId()));
            ProjectEntity projectEntity = mongoTemplate.findOne(query,ProjectEntity.class);
            if(!projectEntity.getManagerId().equals(subProject.getManagerId())){
                joinSubProject(subProject.getSubProjectId(),projectEntity.getManagerId());
            }

            return subProject;
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public Object readSubProject(String key, String value) {
        try {
            Query query = new Query(Criteria.where(key).is(value));
            if (mongoTemplate.find(query, SubProjectEntity.class).isEmpty()) {
                return "None";
            } else {
                List<SubProjectEntity> subProjectEntities = mongoTemplate.find(query, SubProjectEntity.class);
                return subProjectEntities;
            }
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public String deleteSubProject(String key, String value) {
        try {
            Query query = new Query(Criteria.where(key).is(value));
            mongoTemplate.remove(query, SubProjectEntity.class);
            return "Success";
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public Object updateSubProject(HttpServletRequest request) {
        try {
            String subProjectId = request.getParameter("subProjectId");
            Query query = new Query(Criteria.where("subProjectId").is(subProjectId));
            CommonMethod method = new CommonMethod();
            Update update = method.setUpdate(request);
            mongoTemplate.updateFirst(query, update, SubProjectEntity.class);
            return mongoTemplate.findOne(query,SubProjectEntity.class);
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public Object joinSubProject(String subProjectId, String userId) {
        try {
            Query query = new Query(Criteria.where("subProjectId").is(subProjectId));
            if (!mongoTemplate.find(query, SubProjectEntity.class).isEmpty()) {
                SubProjectEntity subProject = mongoTemplate.findOne(query, SubProjectEntity.class);
                String managerId = subProject.getManagerId();
                JSONArray members = subProject.getMembers();
                CommonMethod method = new CommonMethod();
                Query queryUser=Query.query(Criteria.where("userId").is(userId));
                User user =mongoTemplate.findOne(queryUser, User.class);
                Object result = method.joinGroup(members, managerId, userId, user.getUserName());
                if (result.equals("Exist")) {
                    return "Exist";
                } else {
                    Update update = new Update();
                    update.set("members", result);
                    mongoTemplate.updateFirst(query, update, SubProjectEntity.class);
                    return mongoTemplate.findOne(query, SubProjectEntity.class);
                }
            } else {
                return "None";
            }
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public String quitSubProject(String subProjectId, String userId) {
        try {

            Query query = new Query(Criteria.where("subProjectId").is(subProjectId));
            if (!mongoTemplate.find(query, SubProjectEntity.class).isEmpty()) {
                SubProjectEntity subProject = mongoTemplate.findOne(query, SubProjectEntity.class);
                JSONArray members = subProject.getMembers();
                CommonMethod method = new CommonMethod();
                JSONArray newMembers = method.quitGroup(members, userId, "userId");
                Update update = new Update();
                update.set("members", newMembers);
                mongoTemplate.updateFirst(query, update, SubProjectEntity.class);
                return "Success";
            } else {
                return "None";
            }
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public Object changeManager(String subProjectId, String userId) {
        try {
            Query query = new Query(Criteria.where("subProjectId").is(subProjectId));
            SubProjectEntity subProject = mongoTemplate.findOne(query, SubProjectEntity.class);
            String foreManagerId = subProject.getManagerId();
            String foreManagerName=subProject.getManagerName();
            JSONArray members = subProject.getMembers();
            CommonMethod method = new CommonMethod();
            JSONArray newMembers = method.quitGroup(members, userId, "userId");
            JSONArray newMembers1 = (JSONArray) method.joinGroup(newMembers, userId, foreManagerId,foreManagerName);
            Query queryUser =Query.query(Criteria.where("userId").is(userId));
            User newManager=mongoTemplate.findOne(queryUser, User.class);
            Update update = new Update();
            update.set("members", newMembers1);
            update.set("managerId", newManager.getUserId());
            update.set("managerName",newManager.getUserName());
            mongoTemplate.updateFirst(query, update, SubProjectEntity.class);
            return mongoTemplate.findOne(query, SubProjectEntity.class);
        } catch (Exception e) {
            return "Fail";
        }
    }
}
