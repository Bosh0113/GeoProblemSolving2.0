package cn.edu.njnu.geoproblemsolving.Dao.GraphEditor;

import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import cn.edu.njnu.geoproblemsolving.Entity.GraphEditorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class GraphEditorDaoImpl implements GraphEditorDao{

    private final MongoTemplate mongoTemplate;

    @Autowired
    public GraphEditorDaoImpl(MongoTemplate mongoTemplate){this.mongoTemplate=mongoTemplate;}

    @Override
    public Object saveTask(GraphEditorEntity editorEntity){
        try {
            Date date=new Date();
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            editorEntity.setTaskId(UUID.randomUUID().toString());
            editorEntity.setDate(dateFormat.format(date));
            mongoTemplate.save(editorEntity);
            return editorEntity;
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object loadTaskList(String scopeId){
        try {
            Query query = Query.query(Criteria.where("scopeId").is(scopeId));
            return mongoTemplate.find(query,GraphEditorEntity.class);
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object updateTask(HttpServletRequest request){
        try{
            Query query = Query.query(Criteria.where("taskId").is(request.getParameter("taskId")));
            CommonMethod method = new CommonMethod();
            Update update = method.setUpdate(request);
            Date date=new Date();
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            update.set("date",dateFormat.format(date));
            mongoTemplate.updateFirst(query, update, GraphEditorEntity.class);
            return mongoTemplate.findOne(query, GraphEditorEntity.class);
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public String deleteTask(String taskId){
        try {
            Query query = Query.query(Criteria.where("taskId").is(taskId));
            mongoTemplate.remove(query, GraphEditorEntity.class);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }
}
