package cn.edu.njnu.geoproblemsolving.business.activity.repository;

import cn.edu.njnu.geoproblemsolving.business.activity.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(path = "project")
public interface ProjectRepository extends MongoRepository<Project, String> {
    public Page<Project> findProjectsByPrivacyIsNot(String privacy, Pageable pageable);

    public Page<Project> findProjectsByPrivacyIsNotAndCategoryEquals(String privacy, String categtory, Pageable pageable);

    public Page<Project> findProjectsByPrivacyIsNotAndCategoryEqualsAndTagContaining(String privacy, String categtory, String tag, Pageable pageable);


    public Page<Project> findProjectsByPrivacyIsAndCreatorIsOrPrivacyIsNot(String privacy, String creator, String privacy2, Pageable pageable);

    public Page<Project> findProjectsByPrivacyIsAndCreatorIsOrPrivacyIsNotAndCategoryEquals(String privacy, String creator, String privacy2, String categtory, Pageable pageable);

    public Page<Project> findProjectsByPrivacyIsAndCreatorIsOrPrivacyIsNotAndCategoryEqualsAndTagContaining(String privacy, String creator, String privacy2, String categtory, String tag, Pageable pageable);


    public Page<Project> findProjectsByNameLikeOrDescriptionLikeAndPrivacyIsNot(String name, String description, String privacy, Pageable pageable);

    public Page<Project> findProjectsByNameLikeOrDescriptionLikeAndPrivacyIsAndCreatorIsOrPrivacyIsNot(String name, String description, String privacy, String creator, String privacy2, Pageable pageable);
}
