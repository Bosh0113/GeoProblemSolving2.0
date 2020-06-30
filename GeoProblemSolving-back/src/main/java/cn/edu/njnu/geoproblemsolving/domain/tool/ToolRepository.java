package cn.edu.njnu.geoproblemsolving.domain.tool;

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
public interface ToolRepository extends MongoRepository<Tool,String> {
    Optional<Tool> findFirstByTid(String tid);
    List<Tool> findAllByProvider(String provider);
    Optional<Tool> findFirstByToolName(String toolName);
}
