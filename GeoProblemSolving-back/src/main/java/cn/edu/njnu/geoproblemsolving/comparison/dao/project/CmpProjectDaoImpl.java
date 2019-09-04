package cn.edu.njnu.geoproblemsolving.comparison.dao.project;

import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;
import cn.edu.njnu.geoproblemsolving.comparison.dao.dataresource.DataResourceDaoImpl;
import cn.edu.njnu.geoproblemsolving.comparison.dao.modelresource.ModelResourceDaoImpl;
import cn.edu.njnu.geoproblemsolving.comparison.dao.user.CmpUserImpl;
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
        CmpProject cmpProject = DaoUtils.createProject(project,projectId, mongoTemplate);
        mongoTemplate.save(cmpProject);
        //更新父级项目
        if(project.getParentId()!=null){
            updateSubprojects(project.getParentId(),projectId,true);
        }
        //更新用户数据
        CmpUserImpl cmpUserDao = new CmpUserImpl(mongoTemplate);
        cmpUserDao.updateProjects(project.getManagerId(),projectId,true);
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
        List<String> subprojectIds = project.getSubprojects();
        if(subprojectIds==null){
            subprojectIds = new ArrayList<>();
            subprojectIds.add(cmpItemId);
        }else{
            subprojectIds.add(cmpItemId);
        }
        project.setSubprojects(subprojectIds);
        Update update = new Update();
        update.set("cmpItemIds",subprojectIds);
        mongoTemplate.updateFirst(query,update,CmpProject.class);
        return project;
    }

    @Override
    public CmpProject updateSubprojects(String parentId, String projectId,boolean isAdd) {
        Query query = Query.query(Criteria.where("projectId").is(parentId));
        CmpProject parentProject = mongoTemplate.findOne(query, CmpProject.class);
        Update update = new Update();
        if(isAdd == true){
            update.addToSet("subprojects",projectId);
        }else{
            update.pull("subprojects",projectId);
        }
        mongoTemplate.updateFirst(query,update,CmpProject.class);
        return parentProject;
    }

    @Override
    public CmpProject updateSolutionList(String projectId, String solutionId, boolean isAdd) {
        Query query = Query.query(Criteria.where("projectId").is(projectId));
        CmpProject project = mongoTemplate.findOne(query, CmpProject.class);
        Update update = new Update();
        if(isAdd){
            update.addToSet("solutionList",solutionId);
        }else{
            update.pull("solutionList",solutionId);
        }
        mongoTemplate.updateFirst(query,update,CmpProject.class);
        return project;
    }

    @Override
    public void updateModelList(String projectId, String modelId, boolean isAdd) {
        Query query = Query.query(Criteria.where("projectId").is(projectId));
        Update update = new Update();
        if(isAdd){
            update.addToSet("modelList",modelId);
        }else{
            update.pull("modelList",modelId);
        }
        mongoTemplate.updateFirst(query,update,CmpProject.class);
    }

    @Override
    public List<CmpProject> getAllProject() {
        Query query = Query.query(Criteria.where("parentId").exists(false));
        List<CmpProject> cmpProjects = mongoTemplate.find(query, CmpProject.class);
//        List<CmpProject> all = mongoTemplate.findAll(CmpProject.class);
        return cmpProjects;
    }

    @Override
    public CmpProject findProjectById(String id){
        return mongoTemplate.findById(id, CmpProject.class);
    }

    @Override
    public List<CmpProject> findByProjectIdList(List<String> projectIdList) {
        Query query = Query.query(Criteria.where("projectId").in(projectIdList));
        List<CmpProject> cmpProjects = mongoTemplate.find(query, CmpProject.class);
        return cmpProjects;
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
