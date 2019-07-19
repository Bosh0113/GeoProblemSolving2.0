package cn.edu.njnu.geoproblemsolving.incomparison.dao.project;

import cn.edu.njnu.geoproblemsolving.incomparison.entity.CmpProject;

import java.util.List;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 9:46 2019/7/10
 * @Modified By:
 **/
public interface ICmpProjectDao {
    String addProject(CmpProject project);

    String deleteProject(CmpProject project);

    String updateProject(CmpProject project);

    List<CmpProject> getAllProject();

    CmpProject findProjectById(String id);

    List<CmpProject> getProjects(String key, String value);

}
