package cn.edu.njnu.geoproblemsolving.domain.user.repository;

import cn.edu.njnu.geoproblemsolving.domain.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "user")
public interface UserRepository extends MongoRepository<User, String> {
    public User findByEmail(String email);
}
