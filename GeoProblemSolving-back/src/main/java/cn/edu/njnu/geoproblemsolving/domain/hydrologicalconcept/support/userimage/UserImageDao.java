package cn.edu.njnu.geoproblemsolving.domain.hydrologicalconcept.support.userimage;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserImageDao extends MongoRepository<UserImage,String> {
    UserImage findByGeoId(String geoId);
    List<UserImage> findByConceptId(String conceptId);
}
