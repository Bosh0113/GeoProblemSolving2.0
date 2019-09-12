package cn.edu.njnu.geoproblemsolving.Dao.Step;

import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import cn.edu.njnu.geoproblemsolving.Entity.Folder.FolderEntity;
import cn.edu.njnu.geoproblemsolving.Entity.StepEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Component
public class StepDaoImpl implements IStepDao{
    private final MongoTemplate mongoTemplate;

    @Autowired
    public StepDaoImpl(MongoTemplate mongoTemplate){this.mongoTemplate=mongoTemplate;}

    @Override
    public String createStep(StepEntity step) {
        mongoTemplate.save(step);
        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setFolders(new ArrayList<>());
        folderEntity.setFiles(new ArrayList<>());
        folderEntity.setFolderName(step.getTitle());
        folderEntity.setParentId("");
        folderEntity.setFolderId(step.getStepId());
        return step.getStepId();
    }

    @Override
    public Object readStep(String key, String value) {
        Query query=Query.query(Criteria.where(key).is(value));
        if(mongoTemplate.find(query,StepEntity.class).isEmpty()){
            return "None";
        }else {
            List<StepEntity> StepEntities = mongoTemplate.find(query, StepEntity.class);
            return StepEntities;
        }
    }

    @Override
    public void deleteStep(String key, String value) {
        Query query=Query.query(Criteria.where(key).is(value));
        mongoTemplate.remove(query,StepEntity.class);
    }

    @Override
    public String updateStep(HttpServletRequest request) {
        try {
            Query query=new Query(Criteria.where("stepId").is(request.getParameter("stepId")));
            CommonMethod method=new CommonMethod();
            Update update=method.setUpdate(request);
            mongoTemplate.updateFirst(query,update,StepEntity.class);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

}
