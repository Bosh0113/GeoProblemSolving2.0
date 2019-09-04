package cn.edu.njnu.geoproblemsolving.comparison.dao.user;

import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;
import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpProject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 17:03 2019/8/7
 * @Modified By:
 **/
public class CmpUserImpl implements ICmpUserDao {

    private final MongoTemplate mongoTemplate;

    public CmpUserImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void updateSolutions(String userId, String solutionId, boolean isAdd) {
        Query query = Query.query(Criteria.where("userId").is(userId));
        Update update = new Update();
        if(isAdd == true){
            update.addToSet("cmpSolutions",solutionId);
        }else{
            update.pull("cmpSolutions",solutionId);
        }
        mongoTemplate.updateFirst(query,update,UserEntity.class);
    }

    @Override
    public void updateProjects(String userId, String projectId, boolean isAdd) {
        Query query = Query.query(Criteria.where("userId").is(userId));
        Update update = new Update();
        if(isAdd == true){
            update.addToSet("cmpProjects",projectId);
        }else{
            update.pull("cmpProjects",projectId);
        }
        mongoTemplate.updateFirst(query,update,UserEntity.class);
    }
}
