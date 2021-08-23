package cn.edu.njnu.geoproblemsolving.business.activity.repository;

import cn.edu.njnu.geoproblemsolving.business.activity.entity.Subproject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "subproject")
public interface SubprojectRepository extends MongoRepository<Subproject, String> {
    public List<Subproject> findByParent(String aid);
}
