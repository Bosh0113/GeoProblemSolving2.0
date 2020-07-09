package cn.edu.njnu.geoproblemsolving.domain.chatroom.hydrologicalconcept.support.geoicon;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GeoIconDao extends MongoRepository<GeoIcon,String> {
    GeoIcon findGeoIconByGeoId(String geoId);

    List<GeoIconDTO> findGeoIconByNameContains(String key);
}
