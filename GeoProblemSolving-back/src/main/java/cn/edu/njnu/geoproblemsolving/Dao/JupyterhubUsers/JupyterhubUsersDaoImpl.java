package cn.edu.njnu.geoproblemsolving.Dao.JupyterhubUsers;

import cn.edu.njnu.geoproblemsolving.Entity.JupyterhubUsersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class JupyterhubUsersDaoImpl {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public JupyterhubUsersDaoImpl(MongoTemplate mongoTemplate){this.mongoTemplate = mongoTemplate;}

    public String createUser(JupyterhubUsersEntity jupyterUser){
        try {
            mongoTemplate.save(jupyterUser);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    public Object inquiryUser(String projectId){
        try {

            Query query=new Query(Criteria.where("projectId").is(projectId));
            if (mongoTemplate.find(query,JupyterhubUsersEntity.class).isEmpty()){
                return "None";
            }
            else {
                return mongoTemplate.find(query,JupyterhubUsersEntity.class);
            }

        }catch (Exception e){
            return "Fail";
        }
    }
}
