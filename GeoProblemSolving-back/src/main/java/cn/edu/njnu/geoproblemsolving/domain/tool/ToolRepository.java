package cn.edu.njnu.geoproblemsolving.domain.activity.repository.tool;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName
 * @Description
 * @Author Zhiyi
 * @Date 2020/5/26  20:06
 * @Version 1.0.0
 */
public interface ToolRepository extends MongoRepository<ToolEntity,String> {
    Optional<ToolEntity> findFirstByTid(String tid);
    List<ToolEntity> findAllByProvider(String provider);
    Optional<ToolEntity> findFirstByToolName(String toolName);
    void deleteByTid(String id);
    List<ToolEntity> findAllByTid(String tid);
}
