package cn.edu.njnu.geoproblemsolving.business.activity.service;

import cn.edu.njnu.geoproblemsolving.business.activity.dto.UpdateActivityDTO;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Project;
import cn.edu.njnu.geoproblemsolving.Entity.EmailEntity;

import java.util.List;

public interface ProjectService {
    public JsonResult createProject(Project project);

    public JsonResult findProject(String aid);

    public List<Project> findProjectsByPage(int page, int size);

    public JsonResult deleteProject(String aid);

    public JsonResult updateProject(String aid, UpdateActivityDTO project);

    public JsonResult findParticipants(String aid);

    public JsonResult findChildren(String aid);

    public JsonResult joinProject(String aid, String userId);

    public JsonResult updateMemberRole(String aid, String userId, String role);

    public JsonResult quitProject(String aid, String userId);

    public JsonResult invitedParticipants(String aid, String email, String password);

    public JsonResult applyJoinProject(String aid, EmailEntity emailEntity);

    public JsonResult inquiryByConditions(String category, String tag, String keyword, String userId, int page, int size);
}
