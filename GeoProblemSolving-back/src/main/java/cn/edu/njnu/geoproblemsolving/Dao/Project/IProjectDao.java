package cn.edu.njnu.geoproblemsolving.Dao.Project;

import cn.edu.njnu.geoproblemsolving.Entity.EmailEntity;
import cn.edu.njnu.geoproblemsolving.Entity.ProjectEntity;

import javax.servlet.http.HttpServletRequest;

public interface IProjectDao {
    Object createProject(ProjectEntity project);

    Object readProject(String key, String value);

    Object readAllProjects();

    Object readByType(String key, String value);

    void deleteProject(String value);

    Object updateProject(HttpServletRequest request);

    Object joinProject(String projectId, String userId);

    String quitProject(String projectId, String userId);

    Object changeManager(String projectId, String userId);

    String joinByMail(String projectId, String email, String password);

    String applyByEmail(EmailEntity emailEntity);

    String uploadPicture(HttpServletRequest request);

    Object inquiryByPage(String category, String tag, int page, int pageSize, String userId, String[] joinedProjects);
}
