package cn.edu.njnu.geoproblemsolving.ChangeDB;

import cn.edu.njnu.geoproblemsolving.Entity.ModelTools.CModel.ComputableModelEntity;
import cn.edu.njnu.geoproblemsolving.Entity.Resources.FolderEntity;
import cn.edu.njnu.geoproblemsolving.Entity.Resources.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Activity;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Project;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Subproject;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.ActivityType;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.ProjectCategory;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.ProjectPrivacy;
import cn.edu.njnu.geoproblemsolving.business.user.StaticParams;
import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import cn.edu.njnu.geoproblemsolving.business.user.enums.UserTitle;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sun.security.provider.MD5;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class UpdateDBDao {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public UpdateDBDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public String projectUpdate() {
        List<ProjectEntity> projects = mongoTemplate.findAll(ProjectEntity.class);
        for (ProjectEntity oldProject : projects) {
            try {
                Project newProject = new Project();

                newProject.setAid(oldProject.getProjectId());
                newProject.setName(oldProject.getTitle());
                newProject.setDescription(oldProject.getDescription());
                newProject.setCreator(oldProject.getManagerId());
                newProject.setPrivacy(ProjectPrivacy.valueOf(oldProject.getPrivacy()));
                newProject.setCreatedTime(oldProject.getCreateTime());
                newProject.setActiveTime(oldProject.getCreateTime());
                newProject.setLevel(0);
                // picture url
                String url = oldProject.getPicture();
                url = url.replace("project/picture", "resource/images");
                newProject.setPicture(url);
                // tags and category
                String tags = oldProject.getTag();
                try {
                    newProject.setCategory(ProjectCategory.valueOf(oldProject.getCategory()));
                } catch (Exception ex) {
                    newProject.setCategory(ProjectCategory.valueOf("Investigational"));
                    String category = oldProject.getCategory();
                    if (tags != "") {
                        tags = tags + "," + category;
                    } else {
                        tags = category;
                    }
                }
                newProject.setTag(tags);
                // set members
                JSONArray members = new JSONArray();
                JSONObject member = new JSONObject();
                member.put("userId", oldProject.getManagerId());
                member.put("role", "manager");
                members.add(member);
                JSONArray oldMembers = oldProject.getMembers();
                for (Object oldmember : oldMembers) {
                    member = new JSONObject();
                    member.put("userId", ((HashMap) oldmember).get("userId"));
                    member.put("role", "ordinary-member");
                    members.add(member);
                }
                newProject.setMembers(members);
                // project type
                if (oldProject.getType().equals("")) {
                    newProject.setType(ActivityType.Activity_Default);
                } else {
                    newProject.setType(ActivityType.Activity_Group);
                }
                // pathway
                String pathwayStr = oldProject.getSolvingProcess();
                JSONArray pathway = new JSONArray();
                if (pathwayStr != null && !pathwayStr.equals("")) {
                    pathway = pathwayMapping(pathwayStr);
                    newProject.setPathway(pathway);
                }
                // children
                ArrayList<String> children = new ArrayList<>();
                if (oldProject.getType().equals("type0")) {
                    List<SubProjectEntity> subproject = mongoTemplate.findAll(SubProjectEntity.class);
                    for (SubProjectEntity oldsubproject : subproject) {
                        if (oldsubproject.getProjectId().equals(oldProject.getProjectId())) {
                            children.add(oldsubproject.getSubProjectId());
                        }
                    }
                } else if (oldProject.getType().equals("type1")) {
                    for (Object step : pathway) {
                        String aid = (String) ((JSONObject) step).get("aid");
                        children.add(aid);
                    }
                } else if (oldProject.getType().equals("type2")) {
                    List<StepEntity> steps = mongoTemplate.findAll(StepEntity.class);
                    for (StepEntity step : steps) {
                        String projectId = step.getProjectId();
                        String subprojectId = step.getSubProjectId();
                        if((subprojectId != null && subprojectId.equals("")) && (projectId != null && projectId.equals(oldProject.getProjectId()))) {
                            children.add(step.getStepId());
                        }
                    }
                }
                newProject.setChildren(children);

                mongoTemplate.save(newProject);

            } catch (Exception ex) {
                continue;
            }
        }
        return "Finished";
    }

    public String subprojectUpdate() {
        List<SubProjectEntity> subprojects = mongoTemplate.findAll(SubProjectEntity.class);
        for (SubProjectEntity oldSubproject : subprojects) {
            try {
                Subproject newSubproject = new Subproject();

                newSubproject.setAid(oldSubproject.getSubProjectId());
                newSubproject.setName(oldSubproject.getTitle());
                newSubproject.setDescription(oldSubproject.getDescription());
                newSubproject.setParent(oldSubproject.getProjectId());
                newSubproject.setLevel(1);
                newSubproject.setCreatedTime(oldSubproject.getCreateTime());
                newSubproject.setCreator(oldSubproject.getManagerId());
                // set members
                JSONArray members = new JSONArray();
                JSONObject member = new JSONObject();
                member.put("userId", oldSubproject.getManagerId());
                member.put("role", "manager");
                members.add(member);
                JSONArray oldMembers = oldSubproject.getMembers();
                if (oldMembers == null) oldMembers = new JSONArray();
                for (Object oldMember : oldMembers) {
                    member = new JSONObject();
                    member.put("userId", ((HashMap) oldMember).get("userId"));
                    member.put("role", "ordinary-member");
                    members.add(member);
                }
                newSubproject.setMembers(members);
                // subproject type
                if (oldSubproject.getType().equals("")) {
                    newSubproject.setType(ActivityType.Activity_Default);
                } else {
                    newSubproject.setType(ActivityType.Activity_Group);
                }
                // pathway
                String pathwayStr = oldSubproject.getSolvingProcess();
                JSONArray pathway = pathwayMapping(pathwayStr);
                newSubproject.setPathway(pathway);
                // children
                ArrayList<String> children = new ArrayList<>();
                if (oldSubproject.getType().equals("type0")) {
                    for (Object step : pathway) {
                        String aid = (String) ((JSONObject) step).get("aid");
                        children.add(aid);
                    }
                } else if (oldSubproject.getType().equals("type1")){
                    List<StepEntity> steps = mongoTemplate.findAll(StepEntity.class);
                    for (StepEntity step : steps) {
                        String subprojectId = step.getSubProjectId();
                        if(subprojectId != null && subprojectId.equals(oldSubproject.getSubProjectId())) {
                            children.add(step.getStepId());
                        }
                    }
                }
                newSubproject.setChildren(children);

                mongoTemplate.save(newSubproject);
            } catch (Exception ex) {
                continue;
            }
        }
        return "Finished";
    }

    public String activityUpdate() {
        List<StepEntity> steps = mongoTemplate.findAll(StepEntity.class);
        for (StepEntity step : steps) {
            try {
                Activity activity = new Activity();

                activity.setAid(step.getStepId());
                activity.setName(step.getName());
                activity.setDescription(step.getDescription());
                activity.setType(ActivityType.Activity_Unit);
                activity.setPurpose(step.getType());
                activity.setCreator(step.getCreator());
                activity.setCreatedTime(step.getCreateTime());
                activity.setActiveTime(step.getCreateTime());
                // parent
                String subprojectId = step.getSubProjectId();
                String projectId = step.getProjectId();
                if (subprojectId != null && !subprojectId.equals("")) {
                    activity.setParent(subprojectId);
                    activity.setLevel(2);
                } else if (projectId != null && !projectId.equals("")) {
                    activity.setParent(projectId);
                    activity.setLevel(1);
                } else {
                    return "";
                }
                // members
                JSONArray members = new JSONArray();
                if(activity.getLevel().equals(1)){
                    Query query = Query.query(Criteria.where("aid").is(projectId));
                    List<Project> parent = mongoTemplate.find(query, Project.class);
                    members = parent.get(0).getMembers();

                } else if(activity.getLevel().equals(2)){
                    Query query = Query.query(Criteria.where("aid").is(subprojectId));
                    List<Subproject> parent = mongoTemplate.find(query, Subproject.class);
                    members = parent.get(0).getMembers();
                }
                activity.setMembers(members);
                // tools and toolsets
                ArrayList<String> toolList = step.getToolList();
                ArrayList<String> toolsetList = step.getToolsetList();
                if (toolList != null) {
                    activity.setToolList(toolList);
                }
                if (toolsetList != null) {
                    activity.setToolsetList(toolsetList);
                }
                mongoTemplate.save(activity);
            } catch (Exception e) {
                continue;
            }
        }
        return "Finished";
    }

    private JSONArray pathwayMapping(String pathwayStr) {
        JSONArray newPathway = new JSONArray();
        if (pathwayStr != null && !pathwayStr.equals("")) {
            JSONArray pathway = JSONArray.parseArray(pathwayStr);
            for (Object node : pathway) {
                JSONObject step = new JSONObject();
                step.put("id", ((JSONObject) node).get("id"));
                step.put("aid", ((JSONObject) node).get("stepID"));
                step.put("name", ((JSONObject) node).get("name"));
                step.put("category", ((JSONObject) node).get("category"));
                step.put("last", ((JSONObject) node).get("last"));
                step.put("next", ((JSONObject) node).get("next"));
                step.put("x", ((JSONObject) node).get("x"));
                step.put("y", ((JSONObject) node).get("y"));
                step.put("status", ((JSONObject) node).get("activeStatus"));
                newPathway.add(step);
            }
        }
        return newPathway;
    }

    public String userUpdate() {
        List<UserEntity> users = mongoTemplate.findAll(UserEntity.class);
        for (UserEntity oldUser : users) {
            try {
                User user = new User();

                user.setUserId(oldUser.getUserId());
                user.setName(oldUser.getUserName());
                user.setEmail(oldUser.getEmail());
                user.setAvatar(oldUser.getAvatar());
                user.setPhone(oldUser.getMobilePhone());
                user.setCountry(oldUser.getCountry());
                user.setCity(oldUser.getCity());
                user.setHomePage(oldUser.getHomePage());
                user.setIntroduction(oldUser.getIntroduction());
                //title
                try {
                    user.setTitle(UserTitle.valueOf(oldUser.getJobTitle()));
                } catch (Exception ex) {
                    user.setTitle(UserTitle.valueOf("Mr"));
                }
                // organizations 字符串转数组
                ArrayList<String> organizations = new ArrayList<>();
                String organization = oldUser.getOrganization();
                if (organization != null) {
                    organizations.add(organization);
                    user.setOrganizations(organizations);
                }


//                // 存用户服务器
//                // password 加密处理
//                String oldpw = oldUser.getPassword();
//                String password = DigestUtils.md5DigestAsHex(oldpw.getBytes());
//                user.setPassword(password);
//
//                RestTemplate restTemplate = new RestTemplate();
//                String updateUrl  = "http://106.14.78.235/AuthServer/user/tempAdd";
//                restTemplate.postForEntity(updateUrl, user, User.class);


                // 存本地
                // CreatedProject and JoinedProject
                JSONArray managedProjects = oldUser.getManageProjects();
                ArrayList<String> createdProjects = new ArrayList<>();
                for (Object project : managedProjects) {
                    String aid = (String) ((HashMap) project).get("projectId");
                    createdProjects.add(aid);
                }
                user.setCreatedProjects(createdProjects);

                JSONArray joinedProjects = oldUser.getJoinedProjects();
                ArrayList<String> newJoinedProjects = new ArrayList<>();
                for (Object project : joinedProjects) {
                    String aid = (String) ((HashMap) project).get("projectId");
                    newJoinedProjects.add(aid);
                }
                user.setJoinedProjects(newJoinedProjects);

                // email 重复注册用户处理
                Query query = Query.query(Criteria.where("email").is(oldUser.getEmail()));
                List<User> userCollection = mongoTemplate.find(query, User.class);
                if (!userCollection.isEmpty()) {
                    mongoTemplate.remove(userCollection);
                }

                mongoTemplate.save(user);

            } catch (Exception e) {
                continue;
            }
        }
        return "Finished";
    }
}
