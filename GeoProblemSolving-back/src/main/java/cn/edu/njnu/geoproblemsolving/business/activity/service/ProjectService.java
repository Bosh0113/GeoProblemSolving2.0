package cn.edu.njnu.geoproblemsolving.domain.activity.service;

import cn.edu.njnu.geoproblemsolving.domain.activity.Project;
import cn.edu.njnu.geoproblemsolving.Entity.EmailEntity;

import java.util.List;

public interface ProjectService {
    public Object createProject(Project project);

    public Project findProject(String aid);

    public List<Project> findProjectsByPage(int page, int size);

    public String deleteProject(String aid);

    public Object updateProject(Project project);

    public Object findParticipants(String aid);

    public Object joinProject(String aid, String userId);

    public Object updateMemberRole(String aid, String userId, String role);

    public Object quitProject(String aid, String userId);

    public Object invitedParticipants(String aid, String email, String password);

    public String applyJoinProject(String aid, EmailEntity emailEntity);

    public Object inquiryByConditions(String category, String tag, String keyword, String userId, int page, int size);
}
