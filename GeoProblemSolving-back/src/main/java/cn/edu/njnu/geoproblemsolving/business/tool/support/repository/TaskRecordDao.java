package cn.edu.njnu.geoproblemsolving.business.tool.support.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import cn.edu.njnu.geoproblemsolving.business.tool.support.entity.TaskRecord;

import java.util.Date;
import java.util.List;

public interface TaskRecordDao extends MongoRepository<TaskRecord,String> {

    List<TaskRecord> findAllByComputableId(String oid);

    List<TaskRecord> findAllByComputableIdAndRunTimeGreaterThanEqual(String oid, Date date);

    List<TaskRecord> findAllByComputableIdInAndRunTimeGreaterThanEqual(List<String> oids, Date date);

    TaskRecord findFirstByTaskId(String taskId);

//    Task findFirstByTaskId(String taskId);

    List<TaskRecord> findByUserId(String userId);

    List<TaskRecord> findByUserId(String userId, Sort sort);

    Page<TaskRecord> findByUserId(String userId,Pageable pageable);

    Page<TaskRecord> findByUserIdAndStatusBetween(String userId,int statusFrom,int statusTo,Pageable pageable);

    Page<TaskRecord> findByUserIdAndIntegrate(String userId,Boolean integrate,Pageable pageable);

    List<TaskRecord> findByUserIdAndIntegrate(String userId,Boolean integrate,Sort sort);

    List<TaskRecord> findAllByUserIdAndStatus(String userId, int status);

    Page<TaskRecord> findByComputableNameContainsIgnoreCaseAndUserId(String name,String author,Pageable pageable);

    Page<TaskRecord> findByComputableNameAndUserId(String name,String author,Pageable pageable);

    Page<TaskRecord> findByComputableId(String modelId,Pageable pageable);

    Page<TaskRecord> findByComputableIdAndPermission(String modelId,String permission,Pageable pageable);

    Page<TaskRecord> findByComputableIdAndPermissionAndStatus(String modelId,String permission,int status,Pageable pageable);

    Page<TaskRecord> findByComputableIdAndStatus(String modelId,int status,Pageable pageable);

    Page<TaskRecord> findByComputableIdAndUserId(String modelId,String userId,Pageable pageable);

    Page<TaskRecord> findByComputableIdAndUserIdAndStatus(String modelId,String userId,int status,Pageable pageable);
//
    Page<TaskRecord> findByComputableIdAndPermissionAndUserIdNot(String modelId,String permission,String userId,Pageable pageable);

    Page<TaskRecord> findByComputableIdAndPermissionAndStatusAndUserIdNot(String modelId,String permission,int status,String userId,Pageable pageable);

    TaskRecord findFirstByOid(String oid);

    List<TaskRecord> findAllByComputableIdAndFlag(String oid, boolean flag);

    List<TaskRecord> findAllByComputableIdAndFlagAndRunTimeAfter(String oid, boolean flag, Date date);


}
