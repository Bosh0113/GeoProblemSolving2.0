package cn.edu.njnu.geoproblemsolving.business.tool.integration.repository;

import cn.edu.njnu.geoproblemsolving.business.tool.integration.entity.IntegratedTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IntegratedTaskDao extends MongoRepository<IntegratedTask,String> {

    IntegratedTask findByTaskId(String taskId);

    IntegratedTask findByOid(String taskOid);

    List<IntegratedTask> findByUserId(String userName);

    Page<IntegratedTask> findByUserId(String userName, Pageable pageable);

    Page<IntegratedTask> findByAid(String aid, Pageable pageable);
}
