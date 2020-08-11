package cn.edu.njnu.geoproblemsolving.domain.activity.repository;

import cn.edu.njnu.geoproblemsolving.domain.activity.LinkProtocol;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "protocol")
public interface ProtocolRepository extends MongoRepository<LinkProtocol, String> {
}
