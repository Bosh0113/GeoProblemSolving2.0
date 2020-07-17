package cn.edu.njnu.geoproblemsolving.Service.Impl;

import cn.edu.njnu.geoproblemsolving.Dao.Folder.FolderDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.Activities.Project;
import cn.edu.njnu.geoproblemsolving.Entity.Activities.Subproject;
import cn.edu.njnu.geoproblemsolving.Entity.User;
import cn.edu.njnu.geoproblemsolving.Repository.ProjectRepository;
import cn.edu.njnu.geoproblemsolving.Repository.SubprojectRepository;
import cn.edu.njnu.geoproblemsolving.Repository.UserRepository;
import cn.edu.njnu.geoproblemsolving.Service.SubprojectService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SubprojectServiceImpl implements SubprojectService {

    private final SubprojectRepository subprojectRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final FolderDaoImpl folderDao;

    public SubprojectServiceImpl(SubprojectRepository subprojectRepository, ProjectRepository projectRepository, UserRepository userRepository, FolderDaoImpl folderDao) {
        this.subprojectRepository = subprojectRepository;
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

    public Object createSubproject(Subproject subproject){
        try {
            Date data = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String subprojectId = UUID.randomUUID().toString();

            // update project info
            String projectId = subproject.getParent();
            Project project = projectRepository.findById(projectId).get();
            if(project == null) return "Fail: project does not exist";
            ArrayList<String> children = project.getChildren();
            children.add(subprojectId);
            project.setChildren(children);
            projectRepository.save(project);

            // Created time
            subproject.setCreatedTime(dateFormat.format(data));

            // Aid
            subproject.setAid(subprojectId);

            // members
            String creatorId = project.getCreator();
            JSONObject creator = new JSONObject();
            JSONObject member = new JSONObject();
            JSONArray members = new JSONArray();

            creator.put("userId", creatorId);
            creator.put("role", "creator");
            members.add(creator);
            creator.put("userId", project.getCreator());
            creator.put("role", "administrator");
            members.add(member);

            subproject.setMembers(members);
            subprojectRepository.save(subproject);

            // folder
            folderDao.createFolder(subproject.getName(), "", subprojectId);

            return subproject;
        }
        catch (Exception ex){
            return "Fail: Exception";
        }
    }

    public Object inquirySubprojects(String projectId){
        try {
            List<Subproject> result =  subprojectRepository.findByParent(projectId);
            if(result.isEmpty())
                return "None";
            else
                return result;
        }
        catch (Exception ex){
            return "Fail: Exception";
        }
    }

    public Object findParticipants(String aid){
        try {
            Optional optional = subprojectRepository.findById(aid);
            if (!optional.isPresent()) return "Fail: subproject does not exist";
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

            return participants;
        }
        catch (Exception ex){
            return "Fail: Exception";
        }
    }

    public String joinSubproject(String aid, String userId){
        try {
            Optional optional = subprojectRepository.findById(aid);
            if (!optional.isPresent()) return "Fail: subproject does not exist";
            Subproject subproject = (Subproject) optional.get();

            // add user info to subproject
            // if user exist in subproject?
            JSONArray members = subproject.getMembers();
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
            subproject.setMembers(members);
            subprojectRepository.save(subproject);

            return "Success";
        }
        catch (Exception ex){
            return "Fail: Exception";
        }
    }

    public String updateMemberRole(String aid, String userId, String role){
        try {
            // check
            Optional optional = subprojectRepository.findById(aid);
            if (!optional.isPresent()) return "Fail: subproject does not exist";
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

            return "Success";
        }
        catch (Exception ex){
            return "Fail: Exception";
        }
    }

    public String quitSubproject(String aid, String userId){
        try {
            // check
            Optional optional = subprojectRepository.findById(aid);
            if (!optional.isPresent()) return "Fail: subproject does not exist";
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

            return "Success";
        }
        catch (Exception ex){
            return "Fail: Exception";
        }
    }

}
