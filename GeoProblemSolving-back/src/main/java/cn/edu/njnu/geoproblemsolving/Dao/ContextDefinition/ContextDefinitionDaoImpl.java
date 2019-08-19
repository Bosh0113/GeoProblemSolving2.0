package cn.edu.njnu.geoproblemsolving.Dao.ContextDefinition;

import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import cn.edu.njnu.geoproblemsolving.Entity.Folder.FolderEntity;
import cn.edu.njnu.geoproblemsolving.Entity.ContextDefinitionEntity;

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
public class ContextDefinitionDaoImpl implements IContextDefinitionDao{
    private final MongoTemplate mongoTemplate;

    @Autowired
    public ContextDefinitionDaoImpl(MongoTemplate mongoTemplate){this.mongoTemplate=mongoTemplate;}

    @Override
    public String createStep(ContextDefinitionEntity contextDefinition) {
        mongoTemplate.save(contextDefinition);
        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setFolders(new ArrayList<>());
        folderEntity.setFiles(new ArrayList<>());
        folderEntity.setFolderName(contextDefinition.getTitle());
        folderEntity.setParentId("");
        folderEntity.setFolderId(contextDefinition.getStepId());
        return contextDefinition.getStepId();
    }

    @Override
    public Object readContextDefinition(String key, String value) {
        Query query=Query.query(Criteria.where(key).is(value));
        if(mongoTemplate.find(query,ContextDefinitionEntity.class).isEmpty()){
            return "None";
        }else {
            List<ContextDefinitionEntity> ContextDefinitionEntities = mongoTemplate.find(query, ContextDefinitionEntity.class);
            return ContextDefinitionEntities;
        }
    }

    @Override
    public void deleteContextDefinition(String key, String value) {
        Query query=Query.query(Criteria.where(key).is(value));
        mongoTemplate.remove(query,ContextDefinitionEntity.class);
    }

    @Override
    public String updateContextDefinition(HttpServletRequest request) {
        try {
            Query query=new Query(Criteria.where("stepId").is(request.getParameter("stepId")));
            CommonMethod method=new CommonMethod();
            Update update=method.setUpdate(request);
            mongoTemplate.updateFirst(query,update,ContextDefinitionEntity.class);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

}
