package cn.edu.njnu.geoproblemsolving.business.user.service.Impl;

import cn.edu.njnu.geoproblemsolving.Entity.Resources.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import cn.edu.njnu.geoproblemsolving.business.user.repository.UserRepository;
import cn.edu.njnu.geoproblemsolving.business.user.service.UserService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    public UserServiceImpl(UserRepository userRepository, MongoTemplate mongoTemplate) {
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public User findUser(String userId){
        try {
            Optional user =  userRepository.findById(userId);
            if (user.isPresent())
                return (User) user.get();
            else
                return null;
        }
        catch (Exception ex){
            return null;
        }
    }

    @Override
    public Object updataUserInfo(User user){
        try {
            String userId = user.getUserId();
            if(userRepository.findById(userId).isPresent()) return "The user does not exist.";

            String name = user.getName();
            if(name != null) {
                Query queryResource = Query.query(Criteria.where("uploaderId").is(userId));
                List<ResourceEntity> resourceEntities = mongoTemplate.find(queryResource, ResourceEntity.class);
                for (ResourceEntity resourceEntity : resourceEntities) {
                    String resourceId = resourceEntity.getResourceId();
                    Query query = Query.query(Criteria.where("resourceId").is(resourceId));
                    Update update = new Update();
                    update.set("uploaderName", name);
                    mongoTemplate.updateFirst(query, update, ResourceEntity.class);
                }
            }
            userRepository.save(user);

            return user;
        }
        catch (Exception ex){
            return "Fail: Exception";
        }
    }

    @Override
    public Object register(User user){
        try {
            String email = user.getEmail();
            if(userRepository.findByEmail(email) != null) return "The email address has been registered.";

            String userId = UUID.randomUUID().toString();
            user.setUserId(userId);

            user.setJoinedProjects(new ArrayList<>());
            user.setCreatedProjects(new ArrayList<>());

            userRepository.save(user);
            return user;
        }
        catch (Exception ex){
            return "Fail: Exception";
        }
    }

    @Override
    public Object login(String email, String password){
        try {
            User user = userRepository.findByEmail(email);
            if(user == null) return "The email address has not been registered.";

            if(user.getPassword().equals(password)){
                return user;
            }
            else {
                return "Fail: Worrg password";
            }
        }
        catch (Exception ex){
            return "Fail: Exception";
        }
    }

    @Override
    public String changePassword(String email, String password){
        try {
            User user = userRepository.findByEmail(email);
            if(user == null) return "The email address has not been registered.";

            user.setPassword(password);
            userRepository.save(user);

            return "Success";
        }
        catch (Exception ex){
            return "Fail: Exception";
        }
    }
}
