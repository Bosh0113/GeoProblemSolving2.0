package cn.edu.njnu.geoproblemsolving.domain.hydrologicalconcept.support.georule;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface GeoRuleDao extends MongoRepository<GeoRule,String> {

}
