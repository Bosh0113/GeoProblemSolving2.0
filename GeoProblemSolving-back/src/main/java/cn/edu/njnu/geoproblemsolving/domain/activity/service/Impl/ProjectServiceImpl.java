package cn.edu.njnu.geoproblemsolving.Service.Impl;

import cn.edu.njnu.geoproblemsolving.Dao.Email.EmailDaoImpl;
import cn.edu.njnu.geoproblemsolving.Dao.Folder.FolderDaoImpl;
import cn.edu.njnu.geoproblemsolving.domain.activity.Project;
import cn.edu.njnu.geoproblemsolving.Entity.EmailEntity;
import cn.edu.njnu.geoproblemsolving.Entity.User;
import cn.edu.njnu.geoproblemsolving.domain.activity.repository.ProjectRepository;
import cn.edu.njnu.geoproblemsolving.domain.user.repository.UserRepository;
import cn.edu.njnu.geoproblemsolving.Service.ProjectService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final FolderDaoImpl folderDao;

    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository, FolderDaoImpl folderDao) {
        this.projectRepository = projectRepository;
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
    public List<Project> findProjectsByPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return projectRepository.findAll(pageRequest).getContent();
    }

    @Override
    public Object inquiryByConditions(String category, String tag, String keyword, int page, int size) {
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "createdTime");
            PageRequest pageRequest = PageRequest.of(page, size, sort);

            List<Project> projects = new ArrayList<>();
            if (category.equals("All")) {
                projects = projectRepository.findProjectsByPrivacyIsNotOrTagContainingOrNameLikeOrDescriptionLike("Private", tag, keyword, keyword, pageRequest).getContent();
            } else {
                projects = projectRepository.findProjectsByPrivacyIsNotAndCategoryEqualsOrTagContainingOrNameLikeOrDescriptionLike("Private", category, tag, keyword, keyword, pageRequest).getContent();
            }

            if (projects.isEmpty())
                return "None";
            else
                return projects;

        } catch (Exception ex) {
            return "Fail: Exception";
        }
    }

    @Override
    public Object createProject(Project project) {
        try {
            /**
             * Save project info
             */
            // Created time
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            project.setCreatedTime(dateFormat.format(date));

            // Aid
            String projectId = UUID.randomUUID().toString();
            project.setAid(projectId);

            // Set project member
            String creatorId = project.getCreator();
            JSONArray members = new JSONArray();
            JSONObject creator = new JSONObject();
            creator.put("userId", creatorId);
            creator.put("role", "creator");
            members.add(creator);
            project.setMembers(members);

            // children
            ArrayList<String> children = new ArrayList<>();
            project.setChildren(children);

            projectRepository.save(project);

            /**
             * Update user info
             */
            // Update user information
            User user = findByUserId(creator.getString("userId"));
            if (user == null) return "Fail: user does not exist";

            ArrayList<String> managedProjects = new ArrayList<>();
            managedProjects.add(projectId);
            user.setManageProjects(managedProjects);
            userRepository.save(user);

            /**
             * Resource folder
             */
            folderDao.createFolder(project.getName(), "", projectId);

            return project;
        } catch (Exception ex) {
            return "Fail: Exception";
        }
    }

    public Object updateProject(Project project) {
        try {
            String aid = project.getAid();
            if (aid.isEmpty()) {
                return "Fail: no aid";
            } else if (!projectRepository.existsById(aid)) {
                return "Fail: no this project";
            }
            projectRepository.save(project);
            return project;
        } catch (Exception ex) {
            return "Fail: exception";
        }
    }

    @Override
    public String deleteProject(String aid) {
        try {
            // Update user information
            Optional optional = projectRepository.findById(aid);
            if (!optional.isPresent()) return "Fail: does not exist";
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
            if (user == null) return "Fail: user does not exist";
            ArrayList<String> projectIds = user.getManageProjects();
            projectIds.removeIf(projectId -> projectId.equals(aid));
            user.setManageProjects(projectIds);
            userRepository.save(user);

            // delete project
            projectRepository.deleteById(aid);

            return "Success";
        } catch (Exception ex) {
            return "Fail";
        }
    }

    public Object findParticipants(String aid) {
        try {
            Optional optional = projectRepository.findById(aid);
            if (!optional.isPresent()) return "Fail: project does not exist";
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

            return participants;
        } catch (Exception ex) {
            return "Fail: Exception";
        }
    }

    @Override
    public Object joinProject(String aid, String userId) {
        try {
            User user = findByUserId(userId);
            if (user == null) return "Fail: user does not exist";

            Optional optional = projectRepository.findById(aid);
            if (!optional.isPresent()) return "Fail: project does not exist";
            Project project = (Project) optional.get();

            // add user info to project
            // if user exist in project?
            JSONArray members = project.getMembers();
            for (Object member : members) {
                if (member instanceof JSONObject) {
                    if (((JSONObject) member).get("userId").equals(userId)) {
                        return "Exist";
                    }
                }
            }

            JSONObject newUser = new JSONObject();
            newUser.put("userId", userId);
            newUser.put("role", "");
            members.add(newUser);
            project.setMembers(members);
            projectRepository.save(project);

            // update user info
            // if user exist in project?
            ArrayList<String> joinedProjects = user.getJoinedProjects();
            for (String projectId : joinedProjects) {
                if (projectId.equals(aid))
                    return "Exist";
            }

            joinedProjects.add(aid);
            user.setManageProjects(joinedProjects);
            userRepository.save(user);

            return project;
        } catch (Exception ex) {
            return "Fail";
        }
    }

    public Object updateMemberRole(String aid, String userId, String role) {
        try {
            // check
            User user = findByUserId(userId);
            if (user == null) return "Fail: user does not exist";

            Optional optional = projectRepository.findById(aid);
            if (!optional.isPresent()) return "Fail: project does not exist";
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
            projectRepository.save(project);

            return project;
        } catch (Exception ex) {
            return "Fail: Exception";
        }
    }

    @Override
    public Object quitProject(String aid, String userId) {
        try {
            // check
            User user = findByUserId(userId);
            if (user == null) return "Fail: user does not exist";

            Optional optional = projectRepository.findById(aid);
            if (!optional.isPresent()) return "Fail: project does not exist";
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
            projectRepository.save(project);

            // update user info
            ArrayList<String> joinedProjects = user.getJoinedProjects();
            for (String projectId : joinedProjects) {
                if (projectId.equals(aid))
                    joinedProjects.remove(projectId);
            }
            user.setJoinedProjects(joinedProjects);
            userRepository.save(user);

            return "Success";
        } catch (Exception ex) {
            return "Fail";
        }
    }

    public Object invitedParticipants(String aid, String email, String password) {
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
                    return "Fail: Wrong password";
                }
            }
        } catch (Exception ex) {
            return "Fail: Exception";
        }
    }

    public String applyJoinProject(String aid, EmailEntity emailEntity) {
        try {
            // check
            Optional optional = projectRepository.findById(aid);
            if (!optional.isPresent()) return "Fail: project does not exist";
            Project project = (Project) optional.get();

            String addresses = "";
            JSONArray members = project.getMembers();
            for (Object member : members)
                if (member instanceof JSONObject) {
                    if (((JSONObject) member).get("role").equals("administrator")) {
                        String userId = ((JSONObject) member).getString("userId");

                        User user = findByUserId(userId);
                        if (user == null) return "Fail: user does not exist";
                        addresses += user.getEmail();
                        addresses += ",";
                    }
                }
            addresses = addresses.substring(0, addresses.length() - 1);
            emailEntity.setRecipient(addresses);
            EmailDaoImpl emailDao = new EmailDaoImpl();
            emailDao.sendEmail(emailEntity);

            return "Success";
        } catch (Exception ex) {
            return "Fail: Exception";
        }
    }
}
