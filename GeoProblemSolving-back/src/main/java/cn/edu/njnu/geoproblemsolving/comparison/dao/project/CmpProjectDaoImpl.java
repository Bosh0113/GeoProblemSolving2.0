package cn.edu.njnu.geoproblemsolving.incomparison.dao.project;

import cn.edu.njnu.geoproblemsolving.incomparison.entity.CmpProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 9:50 2019/7/10
 * @Modified By:
 **/
public class CmpProjectDaoImpl implements ICmpProjectDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public CmpProjectDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public String addProject(CmpProject project) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        project.setCreateTime(dateFormat.format(date));
        String projectId = UUID.randomUUID().toString();
        project.setProjectId(projectId);
        mongoTemplate.save(project);
        return project.getProjectId();
    }

    @Override
    public String deleteProject(CmpProject project) {
        return null;
    }

    @Override
    public String updateProject(CmpProject project) {
        return null;
    }

    @Override
    public List<CmpProject> getAllProject() {
        List<CmpProject> all = mongoTemplate.findAll(CmpProject.class);
        return all;
    }

    @Override
    public CmpProject findProjectById(String id){
        return mongoTemplate.findById(id, CmpProject.class);
    }

    @Override
    public List<CmpProject> getProjects(String key, String value) {
        Query query = Query.query(Criteria.where(key).is(value));
        if (mongoTemplate.find(query, CmpProject.class).isEmpty()) {
            return null;
        } else {
            List<CmpProject> projectEntities = mongoTemplate.find(query, CmpProject.class);
            return projectEntities;
        }
    }


}
