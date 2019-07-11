package cn.edu.njnu.geoproblemsolving.Dao.ModelBuilder;

import cn.edu.njnu.geoproblemsolving.Entity.LogicalTaskEntity;
import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Component
public class LogicalTaskDaoImpl implements ILogicalTaskDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public LogicalTaskDaoImpl(MongoTemplate mongoTemplate){this.mongoTemplate=mongoTemplate;}

    @Override
    public String saveLogicalTask(LogicalTaskEntity logicalTask){
        try {
            mongoTemplate.save(logicalTask);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object readLogicalTask(String collaborativeId){
        try {
            Query query=new Query(Criteria.where("collaborativeId").is(collaborativeId));
            if (mongoTemplate.find(query,LogicalTaskEntity.class).isEmpty()){
                return "None";
            }
            else {
                return mongoTemplate.find(query,LogicalTaskEntity.class);
            }
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public String deleteLogicalTask(String taskId){
        try {
            Query query=new Query(Criteria.where("taskId").is(taskId));
            mongoTemplate.remove(query,LogicalTaskEntity.class);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public String updateLogicalTask(HttpServletRequest request){
        try {
            Query query=new Query(Criteria.where("taskId").is(request.getParameter("taskId")));
            CommonMethod method=new CommonMethod();
            Update update=method.setUpdate(request);
            mongoTemplate.updateFirst(query,update,LogicalTaskEntity.class);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }
}
