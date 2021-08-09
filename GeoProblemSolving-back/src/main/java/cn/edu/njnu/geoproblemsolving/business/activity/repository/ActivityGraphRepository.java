package cn.edu.njnu.geoproblemsolving.business.activity.repository;

import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity.ActivityGraph;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "activityGraph")
public interface ActivityGraphRepository extends MongoRepository<ActivityGraph, String> {
}
