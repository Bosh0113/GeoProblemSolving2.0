package cn.edu.njnu.geoproblemsolving.business.user.repository;

import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "user")
public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    User findByUserId(String userId);
}
