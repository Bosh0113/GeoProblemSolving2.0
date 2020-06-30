package cn.edu.njnu.geoproblemsolving.domain.hydrologicalconcept.support.georule.rule_spaceposition;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface Rule_SpacePositionDao extends MongoRepository<Rule_SpacePosition,String> {
    List<Rule_SpacePosition> findByFrom(String key);
}
