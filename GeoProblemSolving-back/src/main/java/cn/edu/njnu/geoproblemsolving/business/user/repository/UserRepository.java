package cn.edu.njnu.geoproblemsolving.business.user.repository;

import cn.edu.njnu.geoproblemsolving.business.user.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "user")
public interface UserRepository extends MongoRepository<UserEntity, String> {
    UserEntity findByEmail(String email);
    UserEntity findByUserId(String userId);
}
