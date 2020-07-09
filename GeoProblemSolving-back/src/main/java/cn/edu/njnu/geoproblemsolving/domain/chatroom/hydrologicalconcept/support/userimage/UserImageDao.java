package cn.edu.njnu.geoproblemsolving.domain.chatroom.hydrologicalconcept.support.userimage;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserImageDao extends MongoRepository<UserImage,String> {
    UserImage findByGeoId(String geoId);
    List<UserImage> findByConceptId(String conceptId);
}
