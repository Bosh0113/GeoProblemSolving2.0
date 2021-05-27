package cn.edu.njnu.geoproblemsolving.business.tool.IToll;

import cn.edu.njnu.geoproblemsolving.business.collaboration.compute.util.CommonUtil;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName ToolDaoImpl
 * @Description Todo
 * @Author zhngzhng
 * @Date 2021/5/13
 **/
@Repository
public class ToolDaoImpl implements ToolDao{
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Tool findToolById(String toolId) {
        Query query = new Query(Criteria.where("tid").is(toolId));
        return mongoTemplate.findOne(query, Tool.class);
    }

    @Override
    public List<Tool> findAllByToolCreator(String providerId) {
        Query query = new Query(Criteria.where("provider").is(providerId));
        return mongoTemplate.find(query, Tool.class);
    }

    @Override
    public Tool createTool(Tool tool) {
        return mongoTemplate.save(tool);
    }

    @Override
    public long deleteById(String toolId) {
        Query query = new Query(Criteria.where("tid").is(toolId));
        DeleteResult deleteResult = mongoTemplate.remove(query, Tool.class);
        return deleteResult.getDeletedCount();
    }

    @Override
    public Tool updateTool(Tool putTool, String[] nullPropertyNames) {
        String tid = putTool.getTid();
        Tool localTool = findToolById(tid);
        BeanUtils.copyProperties(putTool, localTool, nullPropertyNames);
        return createTool(localTool);
    }

}
