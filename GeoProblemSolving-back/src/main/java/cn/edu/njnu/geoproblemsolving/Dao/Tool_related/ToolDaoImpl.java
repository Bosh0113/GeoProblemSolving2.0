package cn.edu.njnu.geoproblemsolving.Dao.Tool_related;

import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import cn.edu.njnu.geoproblemsolving.Entity.ToolEntity;
import cn.edu.njnu.geoproblemsolving.Entity.ToolsetEntity;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Component
public class ToolDaoImpl implements IToolDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ToolDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public String createTool(ToolEntity tool) {

        String toolName = tool.getToolName();
        Query query = Query.query(Criteria.where("toolName").is(toolName));

        if (mongoTemplate.find(query, ToolEntity.class).isEmpty()) {

            String tid = UUID.randomUUID().toString();
            tool.setTid(tid);
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            tool.setCreateTime(dateFormat.format(date));

            // 有待测试-----------------------------------------------------------------------------
            // 创建tool时，如果选择了toolset，更新toolset离的toolList
            JSONArray toolsetList = tool.getToolsetInfo();
            for(int i=0;i<toolsetList.size();i++){

                ToolsetDaoImpl toolsetDao = new ToolsetDaoImpl(mongoTemplate);

                //遍历创建tool时选择的toolsetList的Id，找到对应的Toolset实体
                JSONObject toolObj = toolsetList.getJSONObject(i);
                List<ToolsetEntity> toolsets = (List<ToolsetEntity>)toolsetDao.readToolset("tsid", toolObj.getString("tsid"));
                ToolsetEntity updataToolset = toolsets.get(0);
                // 更新toolset
                JSONArray newToolList = updataToolset.getToolList();
                newToolList.add(toolObj);
                updataToolset.setToolList(newToolList);

                toolsetDao.updateToolsetbyToolset(updataToolset);
            }

            mongoTemplate.save(tool);
            return tid;
        }
        else {
            return "Duplicate naming";
        }

    }

    @Override
    public Object readTool(String key, String value) {
        Query query = Query.query(Criteria.where(key).is(value));
        if (mongoTemplate.find(query, ToolEntity.class).isEmpty()) {
            return "None";
        } else {
            List<ToolEntity> ToolEntities = mongoTemplate.find(query, ToolEntity.class);
            return ToolEntities;
        }
    }

    @Override
    public void deleteTool(String key, String value) {
        Query query = Query.query(Criteria.where(key).is(value));
        mongoTemplate.remove(query, ToolEntity.class);
    }

    @Override
    public String updateTool(HttpServletRequest request) {
        try {
            Query query = new Query(Criteria.where("tid").is(request.getParameter("tid")));
            CommonMethod method = new CommonMethod();
            Update update = method.setUpdate(request);
            mongoTemplate.updateFirst(query, update, ToolEntity.class);
            return "Success";
        } catch (Exception e) {
            return "Fail";
        }
    }
}


