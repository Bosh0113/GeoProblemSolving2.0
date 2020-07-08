package cn.edu.njnu.geoproblemsolving.Repository;

import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "user")
public interface UserRepository extends MongoRepository<UserEntity, String> {
}
