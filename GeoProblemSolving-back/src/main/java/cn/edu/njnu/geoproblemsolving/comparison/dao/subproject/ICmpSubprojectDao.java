package cn.edu.njnu.geoproblemsolving.incomparison.dao.subproject;

import cn.edu.njnu.geoproblemsolving.incomparison.entity.CmpProject;
import cn.edu.njnu.geoproblemsolving.incomparison.entity.CmpSubproject;

import java.util.List;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 10:20 2019/7/12
 * @Modified By:
 **/
public interface ICmpSubprojectDao {
    String addProject(CmpSubproject project);

    String deleteProject(CmpSubproject project);

    String updateProject(CmpSubproject project);

    List<CmpSubproject> getAllProject();

    CmpSubproject findProjectById(String id);

    List<CmpProject> getProjects(String key, String value);
}
