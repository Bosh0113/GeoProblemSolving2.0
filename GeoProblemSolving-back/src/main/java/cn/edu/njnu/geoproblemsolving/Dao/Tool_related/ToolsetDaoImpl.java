package cn.edu.njnu.geoproblemsolving.Dao.Tool_related;

import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import cn.edu.njnu.geoproblemsolving.Entity.ToolsetEntity;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ToolsetDaoImpl implements IToolsetDao {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public ToolsetDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public String createToolset(ToolsetEntity toolset) {

        String toolsetName = toolset.getToolsetName();
        Query query = Query.query(Criteria.where("toolsetName").is(toolsetName));

        if (mongoTemplate.find(query, ToolsetEntity.class).isEmpty()) {

            String tsid = UUID.randomUUID().toString();
            toolset.setTsid(tsid);
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            toolset.setCreateTime(dateFormat.format(date));

            mongoTemplate.save(toolset);
            return tsid;
        }
        else {
            return "Duplicate naming";
        }
    }

    @Override
    public Object readToolset(String key, String value) {
        Query query = Query.query(Criteria.where(key).is(value));
        if (mongoTemplate.find(query, ToolsetEntity.class).isEmpty()) {
            return "None";
        } else {
            List<ToolsetEntity> ToolsetEntities = mongoTemplate.find(query, ToolsetEntity.class);
            return ToolsetEntities;
        }
    }

    @Override
    public void deleteToolset(String key, String value) {
        Query query = Query.query(Criteria.where(key).is(value));
        mongoTemplate.remove(query, ToolsetEntity.class);
    }

    @Override
    public String updateToolset(HttpServletRequest request) {
        try {
            Query query = new Query(Criteria.where("tsid").is(request.getParameter("tsid")));
            CommonMethod method = new CommonMethod();
            Update update = method.setUpdate(request);
            mongoTemplate.updateFirst(query, update, ToolsetEntity.class);
            return "Success";
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    // 有待测试---------------------------------------------------------
    public String updateToolsetbyToolset(ToolsetEntity toolset) {
        try {
            Query query = new Query(Criteria.where("tsid").is(toolset.getTsid()));
            CommonMethod method = new CommonMethod();
            Update update = new Update();
            update.set("toolList",toolset.getToolList());
            mongoTemplate.updateFirst(query, update, ToolsetEntity.class);
            return "Success";
        } catch (Exception e) {
            return "Fail";
        }
    }
}
