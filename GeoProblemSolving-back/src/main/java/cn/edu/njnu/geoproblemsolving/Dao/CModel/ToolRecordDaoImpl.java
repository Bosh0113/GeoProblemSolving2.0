package cn.edu.njnu.geoproblemsolving.Dao.CModel;

import cn.edu.njnu.geoproblemsolving.Entity.ModelTools.ToolRecords.ToolRecordsEntity;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName ToolRecordDaoImpl
 * @Description
 * @Author Zhiyi
 * @Date 2019/12/25  15:32
 * @Version 1.0.0
 */
@Component
public class ToolRecordDaoImpl  implements ToolRecordDao {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public ToolRecordDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public String saveRecord(JSONObject obj) {
        mongoTemplate.save(obj,"ToolRecords");
        return "Suc";
    }

    @Override
    public List<ToolRecordsEntity> getAllRecords(String stepId){
        Criteria criteriaUserId = Criteria.where("stepId").is(stepId);
        Query queryRecords = Query.query(new Criteria().orOperator(criteriaUserId));
        return mongoTemplate.find(queryRecords,ToolRecordsEntity.class);
    }
}
