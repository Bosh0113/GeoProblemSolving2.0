package cn.edu.njnu.geoproblemsolving.Dao.ModelBuilder;

import cn.edu.njnu.geoproblemsolving.Entity.ComputationalTaskEntity;
import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class ComputationalTaskDaoImpl implements IComputationalTaskDao{

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ComputationalTaskDaoImpl(MongoTemplate mongoTemplate){this.mongoTemplate=mongoTemplate;}

    @Override
    public String saveComputationalTask(ComputationalTaskEntity computationalTask){
        try {
            mongoTemplate.save(computationalTask);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object readComputationalTask(String collaborativeId){
        try {
            Query query=new Query(Criteria.where("collaborativeId").is(collaborativeId));
            if (mongoTemplate.find(query,ComputationalTaskEntity.class).isEmpty()){
                return "None";
            }
            else {
                return mongoTemplate.find(query,ComputationalTaskEntity.class);
            }
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public String deleteComputationTask(String taskId){
        try {
            Query query=new Query(Criteria.where("taskId").is(taskId));
            mongoTemplate.remove(query,ComputationalTaskEntity.class);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public String updateComputationalTask(HttpServletRequest request){
        try {
            Query query=new Query(Criteria.where("taskId").is(request.getParameter("taskId")));
            CommonMethod method=new CommonMethod();
            Update update=method.setUpdate(request);
            mongoTemplate.updateFirst(query,update,ComputationalTaskEntity.class);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }
}
