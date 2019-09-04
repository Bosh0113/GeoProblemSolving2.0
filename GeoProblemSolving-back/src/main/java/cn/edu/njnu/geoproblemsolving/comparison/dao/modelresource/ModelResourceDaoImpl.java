package cn.edu.njnu.geoproblemsolving.comparison.dao.modelresource;

import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;
import cn.edu.njnu.geoproblemsolving.comparison.dao.project.CmpProjectDaoImpl;
import cn.edu.njnu.geoproblemsolving.comparison.entity.DataResource;
import cn.edu.njnu.geoproblemsolving.comparison.entity.ModelResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 20:03 2019/7/19
 * @Modified By:
 **/
public class ModelResourceDaoImpl implements IModelResourceDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ModelResourceDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public ModelResource findModelByOid(String oid) {
        Query query = Query.query(Criteria.where("oid").is(oid));
        ModelResource model = mongoTemplate.findOne(query, ModelResource.class);
        return model;
    }

    @Override
    public ModelResource createModel(ModelResource mr) {
        // uid
        String oid = UUID.randomUUID().toString();
        mr.setOid(oid);
        // time
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        mr.setCreateTime(dateFormat.format(date));
        mongoTemplate.save(mr);
        // 更新project
        CmpProjectDaoImpl cmpProjectDao = new CmpProjectDaoImpl(mongoTemplate);
        cmpProjectDao.updateModelList(mr.getProjectId(),mr.getOid(),true);

        return mr;
    }

    @Override
    public List<ModelResource> findModelByIdList(List<String> idList) {
        Query query = Query.query(Criteria.where("oid").in(idList));
        List<ModelResource> cmpModels = mongoTemplate.find(query, ModelResource.class);
        return cmpModels;
    }

    @Override
    public List<String> createModelResByNameList(List<String> nameList, UserEntity user) {
        if(nameList.size()>0){
            int len = nameList.size();
            List<String> idList = new ArrayList<>();
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            for(int i=0;i<len;i++){
                String name = nameList.get(i);
                String oid = UUID.randomUUID().toString();
                ModelResource modelResource = new ModelResource(oid, name, user.getUserId(), user.getUserName(),dateFormat.format(date));
                mongoTemplate.save(modelResource);
                idList.add(oid);
            }
            return idList;
        }else{
            return null;
        }
    }
}
