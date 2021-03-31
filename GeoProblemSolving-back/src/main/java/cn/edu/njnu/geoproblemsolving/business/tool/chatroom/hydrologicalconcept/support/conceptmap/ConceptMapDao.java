package cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.conceptmap;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ConceptMapDao extends MongoRepository<ConceptMap,String> {

    ConceptMap findConceptMapByGeoId(String geoId);
    ConceptMap findConceptMapByConceptId(String conceptId);

    List<ConceptMapDTO> findConceptMapByDescriptionContains(String key);

    Boolean existsByGeoId(String geoId);

    void deleteByGeoId(String geoId);

    List<ConceptMap> findAllByNameContains(String key);
}
