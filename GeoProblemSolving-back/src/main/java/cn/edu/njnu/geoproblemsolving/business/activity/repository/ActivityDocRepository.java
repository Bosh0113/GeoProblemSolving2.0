package cn.edu.njnu.geoproblemsolving.business.activity.repository;

import cn.edu.njnu.geoproblemsolving.business.activity.entity.ActivityDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "aDocument")
public interface ActivityDocRepository extends MongoRepository<ActivityDoc, String> {
}
