package cn.edu.njnu.geoproblemsolving.domain.chatroom.hydrologicalconcept.support.georule.rule_elementrelation;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface Rule_ElementRelationDao extends MongoRepository<Rule_ElementRelation,String> {
    List<Rule_ElementRelation> findByFrom(String key);
}
