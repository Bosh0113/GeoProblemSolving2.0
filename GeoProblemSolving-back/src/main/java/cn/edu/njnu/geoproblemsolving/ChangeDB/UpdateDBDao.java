package cn.edu.njnu.geoproblemsolving.ChangeDB;

import cn.edu.njnu.geoproblemsolving.Entity.Resources.FolderEntity;
import cn.edu.njnu.geoproblemsolving.Entity.Resources.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Activity;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Project;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Subproject;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.ActivityType;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.ProjectCategory;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.ProjectPrivacy;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

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
                newProject.setCategory(ProjectCategory.valueOf(oldProject.getCategory()));
                newProject.setCreator(oldProject.getManagerId());
                newProject.setPrivacy(ProjectPrivacy.valueOf(oldProject.getPrivacy()));
                newProject.setTag(oldProject.getTag());
                newProject.setPicture(oldProject.getPicture());
                newProject.setCreatedTime(oldProject.getCreateTime());
                newProject.setActiveTime(oldProject.getCreateTime());
                newProject.setLevel(0);
                // set members
                JSONArray members = new JSONArray();
                JSONObject member = new JSONObject();
                member.put("userId", oldProject.getManagerId());
                member.put("role", "manager");
                members.add(member);
                JSONArray oldMembers = oldProject.getMembers();
                for (Object oldmember : oldMembers) {
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
                        children.add(oldsubproject.getSubProjectId());
                    }
                } else if (oldProject.getType().equals("type1")) {
                    for (Object step : pathway) {
                        String aid = (String) ((HashMap) step).get("aid");
                        children.add(aid);
                    }
                }
                newProject.setChildren(children);

                mongoTemplate.save(newProject);

            } catch (Exception ex) {
                continue;
            }
        }
        return "Success";
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
                newSubproject.setLevel(0);
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
                if (oldSubproject.getType().equals("type0")) {
                    ArrayList<String> children = new ArrayList<>();
                    for (Object step : pathway) {
                        String aid = (String) ((HashMap) step).get("aid");
                        children.add(aid);
                    }
                    newSubproject.setChildren(children);
                }

                mongoTemplate.save(newSubproject);
            } catch (Exception ex) {
                continue;
            }
        }
        return "Success";
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
                activity.setLevel(2);
                activity.setCreator(step.getCreator());
                activity.setCreatedTime(step.getCreateTime());
                activity.setActiveTime(step.getCreateTime());
                // parent
                String subprojectId = step.getSubProjectId();
                String projectId = step.getProjectId();
                if (subprojectId != null && !subprojectId.equals("")) {
                    activity.setParent(subprojectId);
                } else if (projectId != null && !projectId.equals("")) {
                    activity.setParent(projectId);
                } else {
                    return "";
                }
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
        return "Success";
    }

    private JSONArray pathwayMapping(String pathwayStr) {
        JSONArray newPathway = new JSONArray();
        if (pathwayStr != null && !pathwayStr.equals("")) {
            JSONArray pathway = JSONArray.parseArray(pathwayStr);
            JSONObject step = new JSONObject();
            for (Object node : pathway) {
                step.put("id", ((HashMap) node).get("id"));
                step.put("aid", ((HashMap) node).get("stepID"));
                step.put("name", ((HashMap) node).get("name"));
                step.put("category", ((HashMap) node).get("category"));
                step.put("last", ((HashMap) node).get("last"));
                step.put("next", ((HashMap) node).get("next"));
                step.put("x", ((HashMap) node).get("x"));
                step.put("y", ((HashMap) node).get("y"));
                step.put("status", ((HashMap) node).get("activeStatus"));
                newPathway.add(step);
            }
        }
        return newPathway;
    }

    public String userUpdate() {
        try {

            return "Success";
        } catch (Exception e) {
            return "Fail";
        }
    }
}
