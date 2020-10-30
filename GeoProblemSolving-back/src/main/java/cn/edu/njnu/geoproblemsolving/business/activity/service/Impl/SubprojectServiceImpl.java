package cn.edu.njnu.geoproblemsolving.business.activity.service.Impl;

import cn.edu.njnu.geoproblemsolving.Dao.Folder.FolderDaoImpl;
import cn.edu.njnu.geoproblemsolving.business.activity.dto.UpdateActivityDTO;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Activity;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.ActivityType;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ActivityRepository;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Project;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Subproject;
import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ProjectRepository;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.SubprojectRepository;
import cn.edu.njnu.geoproblemsolving.business.activity.service.SubprojectService;
import cn.edu.njnu.geoproblemsolving.business.user.repository.UserRepository;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SubprojectServiceImpl implements SubprojectService {

    private final SubprojectRepository subprojectRepository;
    private final ProjectRepository projectRepository;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final FolderDaoImpl folderDao;

    public SubprojectServiceImpl(SubprojectRepository subprojectRepository, ProjectRepository projectRepository, ActivityRepository activityRepository, UserRepository userRepository, FolderDaoImpl folderDao) {
        this.subprojectRepository = subprojectRepository;
        this.projectRepository = projectRepository;
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.folderDao = folderDao;
    }

    private User findByUserId(String userId) {
        Optional optional = userRepository.findById(userId);
        if (optional.isPresent()) {
            Object user = optional.get();
            return (User) user;
        } else {
            return null;
        }
    }

    @Override
    public JsonResult createSubproject(Subproject subproject) {
        try {
            Date data = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String subprojectId = UUID.randomUUID().toString();

            // update project info
            String projectId = subproject.getParent();
            Project project = projectRepository.findById(projectId).get();
            if (project == null) return ResultUtils.error(-1, "Fail: project does not exist.");
            ArrayList<String> children;
            if (project.getChildren() == null) {
                children = new ArrayList<>();
            } else {
                children = project.getChildren();
            }
            children.add(subprojectId);
            project.setChildren(children);

            // Created time
            subproject.setCreatedTime(dateFormat.format(data));
            subproject.setActiveTime(dateFormat.format(data));

            // Aid
            subproject.setAid(subprojectId);

            // members
            String creatorId = project.getCreator();
            JSONObject creator = new JSONObject();
            JSONArray members = new JSONArray();

            creator.put("userId", creatorId);
            creator.put("role", "manager");
            members.add(creator);

            subproject.setMembers(members);

            // set type
            subproject.setType(subproject.getType());
            if (subproject.getType().equals(ActivityType.Activity_Group)) {
                subproject.setChildren(new ArrayList<>());
            }

            // folder
            folderDao.createFolder(subproject.getName(), "", subprojectId);

            // Save
            subprojectRepository.save(subproject);
            projectRepository.save(project);

            return ResultUtils.success(subproject);
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult inquirySubproject(String aid) {
        try {
            Optional result = subprojectRepository.findById(aid);
            if (result.isPresent())
                return ResultUtils.success(result.get());
            else
                return ResultUtils.error(-1, "Fail: subproject does not exist.");
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult updateSubproject(String aid, UpdateActivityDTO update) {
        try {
            // confirm
            Optional result = subprojectRepository.findById(aid);
            if (!result.isPresent()) return ResultUtils.error(-1, "Fail: subproject does not exist.");

            Subproject subproject = (Subproject) result.get();
            update.updateTo(subproject);

            return ResultUtils.success(subprojectRepository.save(subproject));
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult deleteSubproject(String aid) {
        try {
            Optional optional = subprojectRepository.findById(aid);
            if (!optional.isPresent()) return ResultUtils.error(-1, "Fail: subproject does not exist.");
            Subproject subproject = (Subproject) optional.get();
            optional = projectRepository.findById(subproject.getParent());
            if (!optional.isPresent()) return ResultUtils.error(-1, "Fail: subproject does not exist.");
            Project project = (Project) optional.get();

            // delete from parent
            if (project.getChildren().contains(aid))
                project.getChildren().remove(aid);

            // delete children
            if (subproject.getChildren() != null) {
                deleteChildren(subproject);
            }

            projectRepository.save(project);
            subprojectRepository.deleteById(aid);
            return ResultUtils.success("Success");
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    private void deleteChildren(Activity activity) {
        for (String childId : activity.getChildren()) {
            Optional optional = activityRepository.findById(childId);
            if (optional.isPresent()) {
                Activity child = (Activity) optional.get();
                if (child.getChildren() == null) {
                    activityRepository.deleteById(childId);
                } else {
                    deleteChildren(child);
                }
            }
        }
    }

    @Override
    public JsonResult findChildren(String aid) {
        try {
            Optional optional = subprojectRepository.findById(aid);
            if (!optional.isPresent()) return ResultUtils.error(-1, "Fail: subproject does not exist");
            Subproject subproject = (Subproject) optional.get();

            JSONArray children = new JSONArray();
            if (subproject.getChildren() != null) {
                for (String childId : subproject.getChildren()) {
                    optional = activityRepository.findById(childId);
                    if (optional.isPresent()) {
                        Activity childActivity = (Activity) optional.get();
                        children.add(childActivity);
                    }
                }
            }

            return ResultUtils.success(children);
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult findParticipants(String aid) {
        try {
            Optional optional = subprojectRepository.findById(aid);
            if (!optional.isPresent()) return ResultUtils.error(-1, "Fail: subproject does not exist.");
            Subproject subproject = (Subproject) optional.get();

            // creator
            JSONObject participants = new JSONObject();
            User creator = findByUserId(subproject.getCreator());
            participants.put("creator", creator);

            // members
            JSONArray members = subproject.getMembers();
            JSONArray memberinfos = new JSONArray();
            for (Object member : members) {
                if (member instanceof JSONObject) {
                    User user = findByUserId(((JSONObject) member).getString("userId"));
                    JSONObject userInfo = (JSONObject) member;
                    userInfo.put("name", user.getName());
                    userInfo.put("avatar", user.getAvatar());
                    userInfo.put("email", user.getEmail());
                    userInfo.put("title", user.getTitle());
                    memberinfos.add(userInfo);
                }
            }
            participants.put("members", memberinfos);

            return ResultUtils.success(participants);
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult findLineage(String aid) {
        try {
            Optional optional = subprojectRepository.findById(aid);
            if (!optional.isPresent()) return ResultUtils.error(-1, "Fail: subproject does not exist.");
            Activity subproject = (Activity) optional.get();

            // children
            JSONArray children = new JSONArray();
            if (subproject.getChildren() != null) {
                for (String childId : subproject.getChildren()) {
                    optional = activityRepository.findById(childId);
                    if (optional.isPresent()) {
                        Activity childActivity = (Activity) optional.get();
                        children.add(childActivity);
                    }
                }
            }

            // ancestors
            ArrayList<Activity> ancestors = new ArrayList<>();
            ancestors.add(subproject);

            optional = projectRepository.findById(aid);
            if (!optional.isPresent()) return ResultUtils.error(-1, "Fail: project does not exist.");
            subproject = (Activity) optional.get();
            ancestors.add(subproject);

            // result
            JSONObject lineage = new JSONObject();
            lineage.put("ancestors", ancestors);
            lineage.put("children", children);

            return ResultUtils.success(lineage);
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult joinSubproject(String aid, String userId) {
        try {
            // confirm
            Optional optional = subprojectRepository.findById(aid);
            if (!optional.isPresent()) return ResultUtils.error(-1, "Fail: subproject does not exist.");
            User user = findByUserId(userId);
            if (user == null) return ResultUtils.error(-1, "Fail: user does not exist.");

            // add user info to subproject
            // if user exist in subproject?
            Subproject subproject = (Subproject) optional.get();
            JSONArray members = subproject.getMembers();
            for (Object member : members) {
                if (member instanceof JSONObject) {
                    if (((JSONObject) member).get("userId").equals(userId)) {
                        return ResultUtils.error(-3, "Fail: member already exists in the subproject");
                    }
                }
            }

            JSONObject newUser = new JSONObject();
            newUser.put("userId", userId);
            newUser.put("role", "");
            members.add(newUser);
            subproject.setMembers(members);
            subprojectRepository.save(subproject);

            return ResultUtils.success("Success");
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult updateMemberRole(String aid, String userId, String role) {
        try {
            // check
            Optional optional = subprojectRepository.findById(aid);
            if (!optional.isPresent()) return ResultUtils.error(-1, "Fail: subproject does not exist.");
            Subproject subproject = (Subproject) optional.get();

            // Update roles
            JSONArray members = subproject.getMembers();
            for (Object member : members) {
                if (member instanceof JSONObject) {
                    if (((JSONObject) member).get("userId").equals(userId)) {
                        members.remove(member);
                        JSONObject userInfo = (JSONObject) member;
                        userInfo.put("role", role);
                        members.add(userInfo);
                    }
                }
            }
            subproject.setMembers(members);
            subprojectRepository.save(subproject);

            return ResultUtils.success("Success");
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult quitSubproject(String aid, String userId) {
        try {
            // check
            Optional optional = subprojectRepository.findById(aid);
            if (!optional.isPresent()) return ResultUtils.error(-1, "Fail: subproject does not exist.");
            Subproject subproject = (Subproject) optional.get();

            // remove the user from the project
            JSONArray members = subproject.getMembers();
            for (Object member : members) {
                if (member instanceof JSONObject) {
                    if (((JSONObject) member).get("userId").equals(userId)) {
                        members.remove(member);
                    }
                }
            }
            subproject.setMembers(members);
            subprojectRepository.save(subproject);

            return ResultUtils.success("Success");
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

}
