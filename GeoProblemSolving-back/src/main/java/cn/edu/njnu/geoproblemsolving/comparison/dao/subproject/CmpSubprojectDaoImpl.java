package cn.edu.njnu.geoproblemsolving.comparison.dao.subproject;

import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpProject;
import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpSubproject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 11:24 2019/7/12
 * @Modified By:
 **/
public class CmpSubprojectDaoImpl implements ICmpSubprojectDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public CmpSubprojectDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public String addProject(CmpSubproject project) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        project.setCreateTime(dateFormat.format(date));
        String projectId = UUID.randomUUID().toString();
        project.setProjectId(projectId);
        mongoTemplate.save(project);
        return project.getProjectId();
    }

    @Override
    public String deleteProject(CmpSubproject project) {
        return null;
    }

    @Override
    public String updateProject(CmpSubproject project) {
        return null;
    }

    @Override
    public List<CmpSubproject> getAllProject() {
        return null;
    }

    @Override
    public CmpSubproject findProjectById(String id) {
        return null;
    }

    @Override
    public List<CmpProject> getProjects(String key, String value) {
        return null;
    }
}
