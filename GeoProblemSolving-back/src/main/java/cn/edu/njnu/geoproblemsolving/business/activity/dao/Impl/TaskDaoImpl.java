package cn.edu.njnu.geoproblemsolving.business.activity.dao.Impl;

import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import cn.edu.njnu.geoproblemsolving.business.activity.dao.ITaskDao;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.TaskEntity;
import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class TaskDaoImpl implements ITaskDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public TaskDaoImpl(MongoTemplate mongoTemplate){this.mongoTemplate=mongoTemplate;}

    @Override
    public Object saveTask(TaskEntity task){
        try {
            Date date=new Date();
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            task.setTaskId(UUID.randomUUID().toString());
            task.setCreateTime(dateFormat.format(date));
            mongoTemplate.save(task);
            return task;
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object inquiryTask(String key,String value){
        try {
            Query query=new Query(Criteria.where(key).is(value));
            if (!mongoTemplate.find(query,TaskEntity.class).isEmpty()){
                return mongoTemplate.find(query,TaskEntity.class);
            }
            else {
                return "None";
            }
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object inquiryTodo(String aid){
        try {
            Query query=new Query();
            Criteria criteria=new Criteria();
            criteria.andOperator(Criteria.where("aid").is(aid),Criteria.where("state").is("todo"));
            query.addCriteria(criteria);
            query.with(new Sort(Sort.Direction.ASC,"order"));
            return mongoTemplate.find(query,TaskEntity.class);
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object inquiryDoing(String aid){
        try {
            Query query=new Query();
            Criteria criteria=new Criteria();
            criteria.andOperator(Criteria.where("aid").is(aid),Criteria.where("state").is("doing"));
            query.addCriteria(criteria);
            query.with(new Sort(Sort.Direction.ASC,"order"));
            return mongoTemplate.find(query,TaskEntity.class);
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object inquiryDone(String aid){
        try {
            Query query=new Query();
            Criteria criteria=new Criteria();
            criteria.andOperator(Criteria.where("aid").is(aid),Criteria.where("state").is("done"));
            query.addCriteria(criteria);
            query.with(new Sort(Sort.Direction.ASC,"order"));
            return mongoTemplate.find(query,TaskEntity.class);
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public String deleteTask(String taskId){
        try {
            Query query=new Query(Criteria.where("taskId").is(taskId));
            mongoTemplate.remove(query,TaskEntity.class);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object updateTask(HttpServletRequest request){
        try {
            Query query=new Query(Criteria.where("taskId").is(request.getParameter("taskId")));
            CommonMethod method=new CommonMethod();
            Update update=method.setUpdate(request);
            try{
                int order = Integer.valueOf(request.getParameter("order"));
                 update.set("order",order);
            }catch (Exception ignored){}
            try{
                int importance = Integer.valueOf(request.getParameter("importance"));
                update.set("importance",importance);
            }catch (Exception ignored){}
            mongoTemplate.updateFirst(query,update,TaskEntity.class);
            return mongoTemplate.findOne(query,TaskEntity.class);
        }catch (Exception e){
            return "Fail";
        }
    }

    public String addCreatorNameAndManagerName(){
        try {
            List<TaskEntity> taskEntityList = mongoTemplate.findAll(TaskEntity.class);
            for (TaskEntity taskEntity:taskEntityList){
                Query queryCreator = Query.query(Criteria.where("userId").is(taskEntity.getCreatorId()));
                User creator = mongoTemplate.findOne(queryCreator, User.class);
                Update updateTask = new Update();
                updateTask.set("creatorName",creator.getName());
                updateTask.set("managerName",creator.getName());
                Query queryTask = Query.query(Criteria.where("taskId").is(taskEntity.getTaskId()));
                mongoTemplate.updateFirst(queryTask,updateTask,TaskEntity.class);
            }
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }
}
