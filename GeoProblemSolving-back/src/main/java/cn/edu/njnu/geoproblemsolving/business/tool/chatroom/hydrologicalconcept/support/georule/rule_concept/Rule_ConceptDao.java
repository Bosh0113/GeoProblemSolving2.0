package cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.georule.rule_concept;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface Rule_ConceptDao extends MongoRepository<Rule_Concept,String> {
    List<Rule_Concept> findAllByFrom(String key);
}
