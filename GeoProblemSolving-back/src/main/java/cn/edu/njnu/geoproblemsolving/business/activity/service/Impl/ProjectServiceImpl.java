package cn.edu.njnu.geoproblemsolving.business.activity.service.Impl;

import cn.edu.njnu.geoproblemsolving.Dao.Email.EmailDaoImpl;
import cn.edu.njnu.geoproblemsolving.Dao.Folder.FolderDaoImpl;
import cn.edu.njnu.geoproblemsolving.business.activity.dto.UpdateActivityDTO;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Subproject;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.ActivityType;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Activity;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Project;
import cn.edu.njnu.geoproblemsolving.Entity.EmailEntity;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ActivityRepository;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.SubprojectRepository;
import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ProjectRepository;
import cn.edu.njnu.geoproblemsolving.business.activity.service.ProjectService;
import cn.edu.njnu.geoproblemsolving.business.user.repository.UserRepository;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final SubprojectRepository subprojectRepository;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final FolderDaoImpl folderDao;

    public ProjectServiceImpl(ProjectRepository projectRepository, SubprojectRepository subprojectRepository, ActivityRepository activityRepository, UserRepository userRepository, FolderDaoImpl folderDao) {
        this.projectRepository = projectRepository;
        this.subprojectRepository = subprojectRepository;
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
    public JsonResult findProject(String aid) {
        Optional optional = projectRepository.findById(aid);

        if (optional.isPresent()) {
            Project project = (Project) optional.get();

            // Update active time
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            project.setActiveTime(dateFormat.format(new Date()));
            projectRepository.save(project);

            return ResultUtils.success(project);
        } else {
            return ResultUtils.error(-1, "None");
        }
    }

    @Override
    public List<Project> findProjectsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return projectRepository.findAll(pageable).getContent();
    }

    @Override
    public JsonResult inquiryByConditions(String category, String tag, String keyword, String userId, int page, int size) {
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "createdTime");
            Pageable pageable = PageRequest.of(page - 1, size, sort);

            List<Project> projects = new ArrayList<>();
            if (!keyword.equals("")) {
                if (userId.equals("")) {
                    if (category.equals("All")) {
                        projects = projectRepository.findProjectsByPrivacyIsNot("Private", pageable).getContent();
                    } else {
                        if (tag.equals("")) {
                            projects = projectRepository.findProjectsByPrivacyIsNotAndCategoryEquals("Private", category, pageable).getContent();
                        } else {
                            projects = projectRepository.findProjectsByPrivacyIsNotAndCategoryEqualsAndTagContaining("Private", category, tag, pageable).getContent();
                        }
                    }
                } else {
                    if (category.equals("All")) {
                        projects = projectRepository.findProjectsByPrivacyIsAndCreatorIsOrPrivacyIsNot("Private", userId, "Private", pageable).getContent();
                    } else {
                        if (tag.equals("")) {
                            projects = projectRepository.findProjectsByPrivacyIsAndCreatorIsOrPrivacyIsNotAndCategoryEquals("Private", userId, "Private", category, pageable).getContent();
                        } else {
                            projects = projectRepository.findProjectsByPrivacyIsAndCreatorIsOrPrivacyIsNotAndCategoryEqualsAndTagContaining("Private", userId, "Private", category, tag, pageable).getContent();
                        }
                    }
                }
            } else {
                if (userId.equals("")) {
                    projects = projectRepository.findProjectsByNameLikeOrDescriptionLikeAndPrivacyIsNot(keyword, keyword, "Private", pageable).getContent();
                } else {
                    projects = projectRepository.findProjectsByNameLikeOrDescriptionLikeAndPrivacyIsAndCreatorIsOrPrivacyIsNot(keyword, keyword, "Private", userId, "Private", pageable).getContent();
                }
            }


            if (projects.isEmpty())
                return ResultUtils.error(-1, "None");
            else
                return ResultUtils.success(projects);

        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult createProject(Project project) {
        try {
            /**
             * Save project info
             */
            // Created time and active time
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            project.setCreatedTime(dateFormat.format(date));
            project.setActiveTime(dateFormat.format(date));

            // Aid
            String projectId = UUID.randomUUID().toString();
            project.setAid(projectId);

            // Set project member
            String creatorId = project.getCreator();
            JSONArray members = new JSONArray();
            JSONObject creator = new JSONObject();

            creator.put("userId", creatorId);
            creator.put("role", "manager");
            members.add(creator);

            project.setMembers(members);

            // set type
            project.setType(project.getType());
            if(project.getType().equals(ActivityType.Activity_Group)){
                project.setChildren(new ArrayList<>());
            }

            // children
            ArrayList<String> children = new ArrayList<>();
            project.setChildren(children);


            /**
             * Update user info
             */
            // Update user information
            User user = findByUserId(creator.getString("userId"));
            if (user == null) return ResultUtils.error(-1, "Fail: user does not exist");

            // ArrayList<String> manageProjects = user.getManageProjects();
            ArrayList<String> managedProjects = new ArrayList<>();
            managedProjects.add(projectId);
            user.setManageProjects(managedProjects);
            /**
             * Resource folder
             */
            folderDao.createFolder(project.getName(), "", projectId);

            projectRepository.save(project);
            userRepository.save(user);

            return ResultUtils.success(project);
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    public JsonResult updateProject(String aid, UpdateActivityDTO update) {
        try {
            Optional optional = projectRepository.findById(aid);
            if (!optional.isPresent())
                return ResultUtils.error(-1, "Fail: no this project");

            Project project = (Project) optional.get();
            update.updateTo(project);

            // Update active time
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            project.setActiveTime(dateFormat.format(new Date()));

            projectRepository.save(project);

            return ResultUtils.success(project);
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult deleteProject(String aid) {
        try {
            // Update user information
            Optional optional = projectRepository.findById(aid);
            if (!optional.isPresent()) return ResultUtils.error(-1, "Fail: does not exist");
            Project project = (Project) optional.get();

            // joined project
            JSONArray members = project.getMembers();
            for (Object member : members) {
                if (member instanceof JSONObject) {
                    String userId = ((JSONObject) member).getString("userId");
                    quitProject(aid, userId);
                }
            }

            // created project
            String userId = project.getCreator();
            User user = findByUserId(userId);
            if (user == null) return ResultUtils.error(-1, "Fail: user does not exist");
            ArrayList<String> projectIds = user.getManageProjects();
            projectIds.removeIf(projectId -> projectId.equals(aid));
            user.setManageProjects(projectIds);
            userRepository.save(user);

            // delete children
            if (project.getChildren() != null) {
                deleteChildren(project);
            }

            // delete project
            projectRepository.deleteById(aid);

            return ResultUtils.success("Success");

        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    private void deleteChildren(Activity activity) {
        for (String childId : activity.getChildren()) {
            if(activity.getLevel() == 1) {
                Optional optional = activity.getLevel() > 1 ? activityRepository.findById(childId) : subprojectRepository.findById(childId);
                if (optional.isPresent()) {
                    Activity child = (Activity) optional.get();
                    if (child.getChildren() == null) {
                        if (activity.getLevel() > 1) {
                            activityRepository.deleteById(childId);
                        } else {
                            subprojectRepository.deleteById(childId);
                        }
                    } else {
                        deleteChildren(child);
                    }
                }
            }
        }
    }

    @Override
    public JsonResult findChildren(String aid) {
        try {
            Optional optional = projectRepository.findById(aid);
            if (!optional.isPresent()) return ResultUtils.error(-1, "Fail: project does not exist");
            Project project = (Project) optional.get();

            JSONArray children = new JSONArray();
            if(project.getChildren() != null) {
                for (String childId : project.getChildren()) {
                    optional = subprojectRepository.findById(childId);
                    if (optional.isPresent()) {
                        Subproject childActivity = (Subproject) optional.get();
                        children.add(childActivity);
                    }
                }
            }

            // Update active time
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            project.setActiveTime(dateFormat.format(new Date()));
            projectRepository.save(project);

            return ResultUtils.success(children);
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult findParticipants(String aid) {
        try {
            Optional optional = projectRepository.findById(aid);
            if (!optional.isPresent()) return ResultUtils.error(-1, "Fail: project does not exist");
            Project project = (Project) optional.get();

            JSONObject participants = new JSONObject();
            User creator = findByUserId(project.getCreator());
            participants.put("creator", creator);

            JSONArray members = project.getMembers();
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

            // Update active time
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            project.setActiveTime(dateFormat.format(new Date()));
            projectRepository.save(project);

            return ResultUtils.success(participants);
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult joinProject(String aid, String userId) {
        try {
            // confirm
            User user = findByUserId(userId);
            if (user == null) return ResultUtils.error(-1, "Fail: user does not exist");
            Optional optional = projectRepository.findById(aid);
            if (!optional.isPresent()) return ResultUtils.error(-1, "Fail: project does not exist");
            Project project = (Project) optional.get();

            // add user info to project
            // if user exist in project?
            JSONArray members = project.getMembers();
            for (Object member : members) {
                if (member instanceof JSONObject) {
                    if (((JSONObject) member).get("userId").equals(userId)) {
                        return ResultUtils.error(-3, "Fail: member already exists in the project");
                    }
                }
            }

            JSONObject newUser = new JSONObject();
            newUser.put("userId", userId);
            newUser.put("role", "");
            members.add(newUser);
            project.setMembers(members);

            // update user info
            // if user exist in project?
            ArrayList<String> joinedProjects = user.getJoinedProjects();
            for (String projectId : joinedProjects) {
                if (projectId.equals(aid))
                    return ResultUtils.error(-3, "Fail: project already exists in personal joined projects");
            }

            joinedProjects.add(aid);
            user.setManageProjects(joinedProjects);

            // Update active time
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            project.setActiveTime(dateFormat.format(new Date()));

            //save
            projectRepository.save(project);
            userRepository.save(user);

            return ResultUtils.success(project);
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult updateMemberRole(String aid, String userId, String role) {
        try {
            // check
            User user = findByUserId(userId);
            if (user == null) return ResultUtils.error(-1, "Fail: user does not exist");

            Optional optional = projectRepository.findById(aid);
            if (!optional.isPresent()) return ResultUtils.error(-1, "Fail: project does not exist");
            Project project = (Project) optional.get();

            // Update roles
            JSONArray members = project.getMembers();
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
            project.setMembers(members);

            // Update active time
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            project.setActiveTime(dateFormat.format(new Date()));

            projectRepository.save(project);

            return ResultUtils.success(project);
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult quitProject(String aid, String userId) {
        try {
            // check
            User user = findByUserId(userId);
            if (user == null) return ResultUtils.error(-1, "Fail: user does not exist");

            Optional optional = projectRepository.findById(aid);
            if (!optional.isPresent()) return ResultUtils.error(-1, "Fail: project does not exist");
            Project project = (Project) optional.get();

            // remove the user from the project
            JSONArray members = project.getMembers();
            for (Object member : members) {
                if (member instanceof JSONObject) {
                    if (((JSONObject) member).get("userId").equals(userId)) {
                        members.remove(member);
                    }
                }
            }
            project.setMembers(members);

            // update user info
            ArrayList<String> joinedProjects = user.getJoinedProjects();
            for (String projectId : joinedProjects) {
                if (projectId.equals(aid))
                    joinedProjects.remove(projectId);
            }
            user.setJoinedProjects(joinedProjects);

            // Update active time
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            project.setActiveTime(dateFormat.format(new Date()));

            //save
            projectRepository.save(project);
            userRepository.save(user);

            return ResultUtils.success("Success");
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult invitedParticipants(String aid, String email, String password) {
        try {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                user = new User();
                user.setUserId(UUID.randomUUID().toString());
                user.setEmail(email);
                user.setPassword(password);
                userRepository.save(user);
                return joinProject(aid, user.getUserId());
            } else {
                if (user.getPassword().equals(password)) {
                    return joinProject(aid, user.getUserId());
                } else {
                    return ResultUtils.error(-2, "Fail: Wrong password");
                }
            }
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult applyJoinProject(String aid, EmailEntity emailEntity) {
        try {
            // check
            Optional optional = projectRepository.findById(aid);
            if (!optional.isPresent()) return ResultUtils.error(-1, "Fail: project does not exist");
            Project project = (Project) optional.get();

            String addresses = "";
            JSONArray members = project.getMembers();
            for (Object member : members)
                if (member instanceof JSONObject) {
                    if (((JSONObject) member).get("role").equals("manager")) {
                        String userId = ((JSONObject) member).getString("userId");

                        User user = findByUserId(userId);
                        if (user == null) return ResultUtils.error(-1, "Fail: user does not exist");
                        addresses += user.getEmail();
                        addresses += ",";
                    }
                }
            addresses = addresses.substring(0, addresses.length() - 1);
            emailEntity.setRecipient(addresses);
            EmailDaoImpl emailDao = new EmailDaoImpl();
            emailDao.sendEmail(emailEntity);

            return ResultUtils.success("Success");
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }
}
