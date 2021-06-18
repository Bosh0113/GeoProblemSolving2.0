package cn.edu.njnu.geoproblemsolving.business.collaboration.compute.dao;

import cn.edu.njnu.geoproblemsolving.business.collaboration.compute.entity.ComputeTask;
import cn.edu.njnu.geoproblemsolving.business.collaboration.compute.util.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName computeTaskDaoImpl
 * @Description Todo
 * @Author zhngzhng
 * @Date 2021/5/13
 **/
@Repository
public class ComputeTaskDaoImpl implements ComputeTaskDao{
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public ComputeTask createTask(ComputeTask computeTask) {
        return mongoTemplate.save(computeTask);
    }

    @Override
    public ComputeTask updateTask(ComputeTask putTask) {
        String taskId = putTask.getTaskId();
        Query query = new Query(Criteria.where("taskId").is(taskId));
        ComputeTask localTask = mongoTemplate.findOne(query, ComputeTask.class);
        CommonUtil commonUtil = new CommonUtil();
        String[] nullPropertyNames = commonUtil.getNullPropertyNames(putTask);
        BeanUtils.copyProperties(putTask, localTask, nullPropertyNames);
        return createTask(localTask);
    }

}
