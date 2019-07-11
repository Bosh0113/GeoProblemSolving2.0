package cn.edu.njnu.geoproblemsolving.Dao.ModelBuilder;

import cn.edu.njnu.geoproblemsolving.Entity.ConceptualTaskEntity;
import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Component
public class ConceptualTaskDaoImpl implements IConceptualTaskDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ConceptualTaskDaoImpl(MongoTemplate mongoTemplate){this.mongoTemplate=mongoTemplate;}

    @Override
    public String saveConceptualTask(ConceptualTaskEntity conceptualTask){
        try {
            mongoTemplate.save(conceptualTask);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object readConceptualTask(String collaborativeId){
        try {
            Query query=new Query(Criteria.where("collaborativeId").is(collaborativeId));
            if (mongoTemplate.find(query,ConceptualTaskEntity.class).isEmpty()){
                return "None";
            }
            else {
                return mongoTemplate.find(query,ConceptualTaskEntity.class);
            }
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public String deleteConceptualTask(String taskId){
        try {
            Query query=new Query(Criteria.where("taskId").is(taskId));
            mongoTemplate.remove(query,ConceptualTaskEntity.class);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public String updateConceptualTask(HttpServletRequest request){
        try {
            Query query=new Query(Criteria.where("taskId").is(request.getParameter("taskId")));
            CommonMethod method=new CommonMethod();
            Update update=method.setUpdate(request);
            mongoTemplate.updateFirst(query,update,ConceptualTaskEntity.class);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }
}
