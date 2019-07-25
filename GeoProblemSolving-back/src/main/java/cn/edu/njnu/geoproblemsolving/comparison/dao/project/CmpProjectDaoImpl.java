package cn.edu.njnu.geoproblemsolving.comparison.dao.project;

import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;
import cn.edu.njnu.geoproblemsolving.comparison.dao.dataresource.DataResourceDaoImpl;
import cn.edu.njnu.geoproblemsolving.comparison.dao.modelresource.ModelResourceDaoImpl;
import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpProject;
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
    public CmpProject addProject(CmpProject project) {
        // 1.createdTime
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        project.setCreateTime(dateFormat.format(date));
        // 2.projectId
        String projectId = UUID.randomUUID().toString();
        project.setProjectId(projectId);
        // 3.设置 memberName:
        Query query = Query.query(Criteria.where("userId").is(project.getManagerId()));
        UserEntity userInfo = mongoTemplate.findOne(query, UserEntity.class);
        project.setManagerName(userInfo.getUserName());
        // 4.设置默认 public
        project.setPrivacy("Public");
        // 5.设置outputList
        if(project.getOutputDataList()!=null && project.getOutputDataList().size()>0){
            DataResourceDaoImpl dataResourceDao = new DataResourceDaoImpl(mongoTemplate);
            List<String> dataIdList = dataResourceDao.createDataResByNameList(project.getOutputDataList(), userInfo);
            project.setOutputDataList(dataIdList);
        }
        // 6.设置modelList
        if(project.getModelList()!=null&&project.getModelList().size()>0){
            ModelResourceDaoImpl modelResourceDao = new ModelResourceDaoImpl(mongoTemplate);
            List<String> modelIdList = modelResourceDao.createModelResByNameList(project.getModelList(), userInfo);
            project.setModelList(modelIdList);
        }
        mongoTemplate.save(project);
        return project;
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
