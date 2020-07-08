package cn.edu.njnu.geoproblemsolving.Repository;

import cn.edu.njnu.geoproblemsolving.Entity.Activities.Activity;
import cn.edu.njnu.geoproblemsolving.Entity.Activities.Enums.ActivityPrivacy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "activity")
public interface ActivityRepository extends MongoRepository<Activity, String> {

    public List<Activity> findByLevel(Integer level);

    public List<Activity> findByPrivacy(ActivityPrivacy privacy);
}
