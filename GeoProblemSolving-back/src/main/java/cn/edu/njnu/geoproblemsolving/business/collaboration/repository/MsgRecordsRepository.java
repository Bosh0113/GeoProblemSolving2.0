package cn.edu.njnu.geoproblemsolving.business.collaboration.repository;

import cn.edu.njnu.geoproblemsolving.business.collaboration.entity.MsgRecords;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "msgRecords")
public interface MsgRecordsRepository extends MongoRepository<MsgRecords, String> {
    public List<MsgRecords> findByAid(String aid);
}
