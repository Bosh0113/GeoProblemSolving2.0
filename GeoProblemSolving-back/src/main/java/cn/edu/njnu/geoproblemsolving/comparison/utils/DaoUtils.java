package cn.edu.njnu.geoproblemsolving.comparison.utils;

import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;
import cn.edu.njnu.geoproblemsolving.comparison.dao.dataresource.DataResourceDaoImpl;
import cn.edu.njnu.geoproblemsolving.comparison.dao.modelresource.ModelResourceDaoImpl;
import cn.edu.njnu.geoproblemsolving.comparison.entity.BaseCmpInfo;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 20:46 2019/7/25
 * @Modified By:
 **/
public class DaoUtils {

    public static BaseCmpInfo createCmpInfo(BaseCmpInfo info,String projectId, MongoTemplate mongoTemplate){
        // 1.createdTime
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        info.setCreateTime(dateFormat.format(date));
        // 2.projectId
        info.setProjectId(projectId);
        // 3.设置 memberName:
        Query query = Query.query(Criteria.where("userId").is(info.getManagerId()));
        UserEntity userInfo = mongoTemplate.findOne(query, UserEntity.class);
        info.setManagerName(userInfo.getUserName());
        // 4.设置默认 public
        info.setPrivacy("Public");
        // 5.设置outputList
        if(info.getOutputDataList()!=null && info.getOutputDataList().size()>0){
            DataResourceDaoImpl dataResourceDao = new DataResourceDaoImpl(mongoTemplate);
            List<String> dataIdList = dataResourceDao.createDataResByNameList(info.getOutputDataList(), userInfo);
            info.setOutputDataList(dataIdList);
        }
        // 6.设置modelList
        if(info.getModelList()!=null&&info.getModelList().size()>0){
            ModelResourceDaoImpl modelResourceDao = new ModelResourceDaoImpl(mongoTemplate);
            List<String> modelIdList = modelResourceDao.createModelResByNameList(info.getModelList(), userInfo);
            info.setModelList(modelIdList);
        }
        return info;
    }
}
