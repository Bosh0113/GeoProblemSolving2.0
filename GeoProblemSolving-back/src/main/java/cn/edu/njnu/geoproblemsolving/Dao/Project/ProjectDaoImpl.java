package cn.edu.njnu.geoproblemsolving.Dao.Project;

import cn.edu.njnu.geoproblemsolving.Dao.Email.EmailDaoImpl;
import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import cn.edu.njnu.geoproblemsolving.Dao.SubProject.SubProjectDaoImpl;
import cn.edu.njnu.geoproblemsolving.Dao.User.UserDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.EmailEntity;
import cn.edu.njnu.geoproblemsolving.Entity.Resources.FolderEntity;
import cn.edu.njnu.geoproblemsolving.Entity.Activities.Project;
import cn.edu.njnu.geoproblemsolving.Entity.User;
import cn.edu.njnu.geoproblemsolving.View.StaticPagesBuilder;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ProjectDaoImpl implements IProjectDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ProjectDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

//    @Override
//    public Object createProject(Project project) {
//
//        return project;
//    }

//    @Override
//    public Object readAllProjects() {
//        try {
//            Criteria criteriaPublic = Criteria.where("privacy").is("Public");
//            Criteria criteriaDiscoverable = Criteria.where("privacy").is("Discoverable");
//            Query queryProjects = Query.query(new Criteria().orOperator(criteriaPublic,criteriaDiscoverable));
//            List<Project> projectEntityList = mongoTemplate.find(queryProjects, Project.class);
//            if (!projectEntityList.isEmpty()) {
//                return projectEntityList;
//            } else {
//                return "None";
//            }
//        } catch (Exception e) {
//            return "Fail";
//        }
//    }

//    @Override
//    public Object readByType(String key, String value) {
//        try {
//            Query queryProjects = new Query();
//            Criteria criteria = new Criteria();
//            criteria.andOperator(Criteria.where("privacy").is("Public"), Criteria.where(key).is(value));
//            queryProjects.addCriteria(criteria);
//            List<Project> projectEntityList = mongoTemplate.find(queryProjects, Project.class);
//            if (!projectEntityList.isEmpty()){
//                return projectEntityList;
//            }else {
//                return "None";
//            }
//        } catch (Exception e) {
//            return "Fail";
//        }
//    }

//    @Override
//    public Object readProject(String key, String value) {
//        Query query = Query.query(Criteria.where(key).is(value));
//        if (mongoTemplate.find(query, Project.class).isEmpty()) {
//            return "None";
//        } else {
//            List<Project> projectEntities = mongoTemplate.find(query, Project.class);
//            return projectEntities;
//        }
//    }
//
//    @Override
//    public void deleteProject(String projectId) {
//
//    }
//
//    @Override
//    public Object updateProject(HttpServletRequest request) {
//        try {
//            String projectId = request.getParameter("projectId");
//            Boolean changeFromPrivate = false;
//            if(request.getParameter("privacy")!=null){
//                String newPrivacy = request.getParameter("privacy");
//                if(!newPrivacy.equals("Private")){
//                    changeFromPrivate  = true;
//                }
//            }
//            Boolean updatePage = isUpdateProjectListStaticPage(projectId,changeFromPrivate);
//
//            return null;
//        } catch (Exception e) {
//            return "Fail";
//        }
//    }
//
//    @Override
//    public Object joinProject(String projectId, String userId) {
//        try {
//            Boolean updatePage = isUpdateProjectListStaticPage(projectId,false);
//
//            Query queryProject = new Query(Criteria.where("projectId").is(projectId));
//            if (!mongoTemplate.find(queryProject, Project.class).isEmpty()) {
//
//                Project project = mongoTemplate.findOne(queryProject, Project.class);
//                String managerId = project.getManagerId();
//                JSONArray members = project.getMembers();
//                Query queryUser = Query.query(Criteria.where("userId").is(userId));
//                User user = mongoTemplate.findOne(queryUser, User.class);
//                CommonMethod method = new CommonMethod();
//                Object result = method.joinGroup(members, managerId, userId, user.getUserName());
//                if (result.equals("Exist")) {
//                    return "Exist";
//                } else {
//                    Update update = new Update();
//                    update.set("members", result);
//                    mongoTemplate.updateFirst(queryProject, update, Project.class);
//                    JSONArray joinedProjects = user.getJoinedProjects();
//                    JSONObject newProjectInfo = new JSONObject();
//                    newProjectInfo.put("projectId", project.getProjectId());
//                    newProjectInfo.put("title", project.getTitle());
//                    joinedProjects.add(newProjectInfo);
//                    Update updateUser = new Update();
//                    updateUser.set("joinedProjects", joinedProjects);
//                    mongoTemplate.updateFirst(queryUser, updateUser, User.class);
//
//                    StaticPagesBuilder staticPagesBuilder = new StaticPagesBuilder(mongoTemplate);
//                    staticPagesBuilder.projectDetailPageBuilder(projectId);
//                    if (updatePage){
//                        staticPagesBuilder.projectListPageBuilder();
//                    }
//                    return "Success";
//                }
//            } else {
//                return "None";
//            }
//        } catch (Exception e) {
//            return "Fail";
//        }
//    }
//
//    @Override
//    public String quitProject(String projectId, String userId) {
//        try {
//            Boolean updatePage = isUpdateProjectListStaticPage(projectId,false);
//            Query queryProject = new Query(Criteria.where("projectId").is(projectId));
//            if (!mongoTemplate.find(queryProject, Project.class).isEmpty()) {
//                Project project = mongoTemplate.findOne(queryProject, Project.class);
//                JSONArray members = project.getMembers();
//                CommonMethod method = new CommonMethod();
//                JSONArray newMembers = method.quitGroup(members, userId, "userId");
//                Update updateProject = new Update();
//                updateProject.set("members", newMembers);
//                mongoTemplate.updateFirst(queryProject, updateProject, Project.class);
//
//                Query queryUser = new Query(Criteria.where("userId").is(userId));
//                User user = mongoTemplate.findOne(queryUser, User.class);
//                JSONArray joinedProjects = user.getJoinedProjects();
//                JSONArray newJoinedProjects = method.quitGroup(joinedProjects, projectId, "projectId");
//                Update updateUser = new Update();
//                updateUser.set("joinedProjects", newJoinedProjects);
//                mongoTemplate.updateFirst(queryUser, updateUser, User.class);
//
//                quitSubProjectFromProject(projectId, userId);
//
//                StaticPagesBuilder staticPagesBuilder = new StaticPagesBuilder(mongoTemplate);
//                staticPagesBuilder.projectDetailPageBuilder(projectId);
//                if (updatePage){
//                    staticPagesBuilder.projectListPageBuilder();
//                }
//                return "Success";
//            } else {
//                return "None";
//            }
//
//        } catch (Exception e) {
//            return "Fail";
//        }
//    }
//
//    @Override
//    public Object changeManager(String projectId, String userId) {
//        try {
//            Boolean updatePage = isUpdateProjectListStaticPage(projectId,false);
//            Query query = new Query(Criteria.where("projectId").is(projectId));
//            ProjectEntity project = mongoTemplate.findOne(query, ProjectEntity.class);
//            quitProject(projectId, userId);
//            JSONArray members = project.getMembers();
//            CommonMethod method = new CommonMethod();
//            JSONArray newMembers = method.quitGroup(members, userId, "userId");
//            JSONObject foreManager = new JSONObject();
//            foreManager.put("userId", project.getManagerId());
//            foreManager.put("userName", project.getManagerName());
//            newMembers.add(foreManager);
//            Query oldManagerQuery = Query.query(Criteria.where("userId").is(project.getManagerId()));
//            User oldManagerObject = mongoTemplate.findOne(oldManagerQuery, User.class);
//            JSONArray oldManageProjects = oldManagerObject.getManageProjects();
//            for (int i = 0; i < oldManageProjects.size(); i++) {
//                JSONObject oldManageProject = oldManageProjects.getJSONObject(i);
//                if (oldManageProject.get("projectId").equals(projectId)) {
//                    oldManageProjects.remove(i);
//                    break;
//                }
//            }
//            Update updateOldManager = new Update();
//            updateOldManager.set("manageProjects", oldManageProjects);
//            Query newManagerQuery = Query.query(Criteria.where("userId").is(userId));
//            User newManager = mongoTemplate.findOne(newManagerQuery, User.class);
//            JSONArray newManageProjects = newManager.getManageProjects();
//            JSONObject newManageProject = new JSONObject();
//            newManageProject.put("projectId", project.getProjectId());
//            newManageProject.put("title", project.getTitle());
//            newManageProjects.add(newManageProject);
//            JSONArray oldManageJoinedProjects = oldManagerObject.getJoinedProjects();
//            oldManageJoinedProjects.add(newManageProject);
//            updateOldManager.set("joinedProjects", oldManageJoinedProjects);
//            mongoTemplate.updateFirst(oldManagerQuery, updateOldManager, User.class);
//            Update updateNewManager = new Update();
//            updateNewManager.set("manageProjects", newManageProjects);
//            mongoTemplate.updateFirst(newManagerQuery, updateNewManager, User.class);
//            Update update = new Update();
//            update.set("managerId", newManager.getUserId());
//            update.set("managerName", newManager.getUserName());
//            update.set("members", newMembers);
//            mongoTemplate.updateFirst(query, update, ProjectEntity.class);
//
//            StaticPagesBuilder staticPagesBuilder = new StaticPagesBuilder(mongoTemplate);
//            staticPagesBuilder.projectDetailPageBuilder(projectId);
//            if (updatePage){
//                staticPagesBuilder.projectListPageBuilder();
//            }
//            newProjectManagerBeSubProjectsMember(projectId,userId);
//
//            return mongoTemplate.findOne(query, ProjectEntity.class);
//        } catch (Exception e) {
//            return "Fail";
//        }
//    }
//
//    @Override
//    public String joinByMail(String projectId, String email, String password) {
//        try {
//            UserDaoImpl userDao = new UserDaoImpl(mongoTemplate);
//            User user = new User();
//            if (userDao.isRegistered(email)) {
//                if (userDao.verifyPassword(email, password)) {
//                    Query query = new Query(Criteria.where("email").is(email));
//                    user = mongoTemplate.findOne(query, User.class);
//                } else {
//                    return "Password";
//                }
//            } else {
//                user.setUserId(UUID.randomUUID().toString());
//                user.setUserName(email);
//                user.setEmail(email);
//                user.setPassword(password);
//                user.setMobilePhone("");
//                user.setJobTitle("");
//                user.setJoinedProjects(new JSONArray());
//                user.setManageProjects(new JSONArray());
//                user.setAvatar("");
//                user.setCity("");
//                user.setCountry("");
//                user.setDirection("");
//                user.setGender("");
//                user.setHomePage("");
//                user.setIntroduction("");
//                userDao.saveUser(user);
//            }
//            String userId = user.getUserId();
//            String result = joinProject(projectId, userId).toString();
//            if (!result.equals("Success")) {
//                return result;
//            }
//            return "Success";
//        } catch (Exception e) {
//            return "Fail";
//        }
//    }
//
//    @Override
//    public String applyByEmail(EmailEntity emailEntity) {
//        try {
//            String userId = emailEntity.getRecipient();
//            Query queryUser = Query.query(Criteria.where("userId").is(userId));
//            User manager = mongoTemplate.findOne(queryUser, User.class);
//            emailEntity.setRecipient(manager.getEmail());
//            EmailDaoImpl emailDao = new EmailDaoImpl();
//            emailDao.sendEmail(emailEntity);
//            return "Success";
//        } catch (Exception e) {
//            return "Fail";
//        }
//    }

    @Override
    public Object inquiryByPage(String category, String tag, int page, int pageSize, String userId, String searchConfig, String[] joinedProjects){
        try {
//            Sort sort = new Sort(Sort.Direction.DESC,"createTime");
//
//            Pattern tagPattern = Pattern.compile("^.*"+tag+".*$",Pattern.CASE_INSENSITIVE);
//
//            Pattern searchPatten = Pattern.compile("^.*"+ searchConfig + ".*$", Pattern.CASE_INSENSITIVE);
//
//            // Public
//            Criteria criteriaPublic = Criteria.where("privacy").is("Public");
//
//            // Discoverable
//            Criteria criteriaDiscoverable = Criteria.where("privacy").is("Discoverable");
//
//            // Manager
//            Criteria criteriaManager = new Criteria();
//            Query query;
//            if(!userId.equals("")) {
//                criteriaManager = Criteria.where("managerId").is(userId);
//                if(category.equals("All")) {
//                    query = new Query(
//                            new Criteria().andOperator(Criteria.where("tag").regex(tagPattern),
//                                    new Criteria().orOperator(
//                                            Criteria.where("title").regex(searchPatten),
//                                            Criteria.where("description").regex(searchPatten),
//                                            Criteria.where("introduction").regex(searchPatten)),
//                                    new Criteria().orOperator(criteriaDiscoverable,criteriaPublic,criteriaManager)))
//                            .with(sort);
//                }else {
//                    query = new Query(
//                            new Criteria().andOperator(Criteria.where("category").is(category),
//                                    Criteria.where("tag").regex(tagPattern),
//                                    new Criteria().orOperator(
//                                            Criteria.where("title").regex(searchPatten),
//                                            Criteria.where("description").regex(searchPatten),
//                                            Criteria.where("introduction").regex(searchPatten)),
//                                    new Criteria().orOperator(criteriaDiscoverable,criteriaPublic,criteriaManager)))
//                            .with(sort);
//                }
//            }
//            else {
//                if(category.equals("All")) {
//                    query = new Query(
//                            new Criteria().andOperator(Criteria.where("tag").regex(tagPattern),
//                                    new Criteria().orOperator(
//                                            Criteria.where("title").regex(searchPatten),
//                                            Criteria.where("description").regex(searchPatten),
//                                            Criteria.where("introduction").regex(searchPatten)),
//                                    new Criteria().orOperator(criteriaDiscoverable,criteriaPublic)))
//                            .with(sort);
//                }else {
//                    query = new Query(
//                            new Criteria().andOperator(Criteria.where("category").is(category),
//                                    Criteria.where("tag").regex(tagPattern),
//                                    new Criteria().orOperator(
//                                            Criteria.where("title").regex(searchPatten),
//                                            Criteria.where("description").regex(searchPatten),
//                                            Criteria.where("introduction").regex(searchPatten)),
//                                    new Criteria().orOperator(criteriaDiscoverable,criteriaPublic)))
//                            .with(sort);
//                }
//            }
//            long count = mongoTemplate.count(query,ProjectEntity.class);
//            List<ProjectEntity> projectEntities=mongoTemplate.find(query,ProjectEntity.class);
//
//            // 添加参与的私有项目
//            Criteria criteriaPrivate = new Criteria();
//            if(category.equals("All")){
//                criteriaPrivate = Criteria.where("privacy").is("Private");
//            }
//            else {
//                criteriaPrivate = new Criteria().andOperator(Criteria.where("privacy").is("Private"), Criteria.where("category").is(category));
//            }
//            for(int i = joinedProjects.length-1 ; i >= 0; i--) {
//                Criteria criteria = Criteria.where("projectId").is(joinedProjects[i]);
//                query = new Query(new Criteria().andOperator(criteriaPrivate,criteria));
//                ProjectEntity project = mongoTemplate.findOne(query,ProjectEntity.class);
//                if(project!=null){
//                    count++;
//                    projectEntities.add(0,project);
//                }
//            }
//
//            // 分页
//            int totalPage = (int)Math.ceil((double) count/(double) pageSize);
//            List<ProjectEntity> pagedProjects = new ArrayList<>();
//            for (int i = 0; i < pageSize; i++){
//                int index = (page-1) * pageSize + i;
//                if(projectEntities.size()>index){
//                    pagedProjects.add(projectEntities.get(index));
//                }
//            }
//
//            JSONObject result = new JSONObject();
//            result.fluentPut("totalPage",totalPage);
//            result.fluentPut("count",count);
//            result.fluentPut("projectList",pagedProjects);
            return null;

        }catch (Exception e){
            return "Fail";
        }
    }

    private void quitSubProjectFromProject(String projectId, String userId) {
//        Query querySubProjects = Query.query(Criteria.where("projectId").is(projectId));
//        List<SubProjectEntity> subProjectList = mongoTemplate.find(querySubProjects, SubProjectEntity.class);
//        for (SubProjectEntity subProject : subProjectList) {
//            Query query = Query.query(Criteria.where("subProjectId").is(subProject.getSubProjectId()));
//            JSONArray members = subProject.getMembers();
//            String managerId = subProject.getManagerId();
//            if (managerId.equals(userId)) {
//                if (members.size() > 0) {
//                    JSONObject firstMember = members.getJSONObject(0);
//                    members.remove(0);
//                    Update update = new Update();
//                    update.set("managerId", firstMember.get("userId"));
//                    update.set("managerName", firstMember.get("userName"));
//                    update.set("members", members);
//                    mongoTemplate.updateFirst(query, update, SubProjectEntity.class);
//                } else {
//                    mongoTemplate.remove(query, SubProjectEntity.class);
//                }
//            } else {
//                for (int i = 0; i < members.size(); i++) {
//                    JSONObject member = members.getJSONObject(i);
//                    if (member.getString("userId").equals(userId)) {
//                        members.remove(i);
//                        Update update = new Update();
//                        update.set("members", member);
//                        mongoTemplate.updateFirst(query, update, SubProjectEntity.class);
//                        break;
//                    }
//                }
//            }
//        }
    }

    private void newProjectManagerBeSubProjectsMember(String projectId, String newManagerId){
//        Query querySubProjects = new Query(Criteria.where("projectId").is(projectId));
//        List<SubProjectEntity> subProjectEntities = mongoTemplate.find(querySubProjects,SubProjectEntity.class);
//        for(SubProjectEntity subProject:subProjectEntities){
//            String subProjectId = subProject.getSubProjectId();
//            SubProjectDaoImpl subProjectDao = new SubProjectDaoImpl(mongoTemplate);
//            subProjectDao.joinSubProject(subProjectId,newManagerId);
//        }
    }

    private Boolean isUpdateProjectListStaticPage(String projectId, Boolean noPrivate){
        int pageSize = 18;
        Sort sort = new Sort(Sort.Direction.DESC,"createTime");
        Pageable pageable = PageRequest.of(0,pageSize,sort);
        Criteria criteriaPublic = Criteria.where("privacy").is("Public");
        Criteria criteriaDiscoverable = Criteria.where("privacy").is("Discoverable");
        Query query = new Query(new Criteria().orOperator(criteriaDiscoverable,criteriaPublic)).with(pageable);
        long count = mongoTemplate.count(query,Project.class);
        if (count<pageSize && noPrivate){ //如果首页数量少于页面数量规格且有新的可见条目，则更新
            return true;
        }
        List<Project> projects = mongoTemplate.find(query,Project.class);
        for(Project project : projects){
            if(projectId.equals(project.getAid())){
                return true;
            }
        }
        return false;
    }

}