package cn.edu.njnu.geoproblemsolving.comparison.dao.project;

import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;
import cn.edu.njnu.geoproblemsolving.comparison.dao.dataresource.DataResourceDaoImpl;
import cn.edu.njnu.geoproblemsolving.comparison.dao.modelresource.ModelResourceDaoImpl;
import cn.edu.njnu.geoproblemsolving.comparison.entity.BaseCmpInfo;
import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpItem;
import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpProject;
import cn.edu.njnu.geoproblemsolving.comparison.utils.DaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public CmpProject addProject(CmpProject project) {
        String projectId = UUID.randomUUID().toString();
        CmpProject cmpProject = (CmpProject) DaoUtils.createCmpInfo(project,projectId, mongoTemplate);
        mongoTemplate.save(cmpProject);
        return cmpProject;
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
    public CmpProject updateCmpItems(String projectId, String cmpItemId) {
        Query query = Query.query(Criteria.where("projectId").is(projectId));
        CmpProject project = mongoTemplate.findOne(query, CmpProject.class);
        List<String> cmpItemIds = project.getCmpItemIds();
        if(cmpItemIds==null){
            cmpItemIds = new ArrayList<>();
            cmpItemIds.add(cmpItemId);
        }else{
            cmpItemIds.add(cmpItemId);
        }
        project.setCmpItemIds(cmpItemIds);
        Update update = new Update();
        update.set("cmpItemIds",cmpItemIds);
        mongoTemplate.updateFirst(query,update,CmpProject.class);
        return project;
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
    public CmpProject findByProjectId(String projectId) {
        Query query = Query.query(Criteria.where("projectId").is(projectId));
        CmpProject cmpProject = mongoTemplate.findOne(query, CmpProject.class);
        return cmpProject;
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
