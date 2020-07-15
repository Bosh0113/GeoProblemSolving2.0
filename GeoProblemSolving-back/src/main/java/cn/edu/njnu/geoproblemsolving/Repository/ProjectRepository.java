package cn.edu.njnu.geoproblemsolving.Repository;

import cn.edu.njnu.geoproblemsolving.Entity.Activities.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(path = "project")
public interface ProjectRepository extends MongoRepository<Project, String> {
    public Page<Project> findProjectsByPrivacyIsNotOrTagContainingOrNameLikeOrDescriptionLike(String privacy, String tag, String name, String description, Pageable pageable);

    public Page<Project> findProjectsByPrivacyIsNotAndCategoryEqualsOrTagContainingOrNameLikeOrDescriptionLike(String privacy, String categtory, String tag, String name, String description, Pageable pageable);
}
