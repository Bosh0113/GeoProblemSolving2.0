package cn.edu.njnu.geoproblemsolving.comparison.dao.project;

import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpProject;

import java.util.List;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 9:46 2019/7/10
 * @Modified By:
 **/
public interface ICmpProjectDao {
    CmpProject addProject(CmpProject project);

    String deleteProject(CmpProject project);

    String updateProject(CmpProject project);

    CmpProject updateCmpItems(String projectId,String cmpItemId);

    List<CmpProject> getAllProject();

    CmpProject findProjectById(String id);

    CmpProject findByProjectId(String projectId);

    List<CmpProject> getProjects(String key, String value);

}
