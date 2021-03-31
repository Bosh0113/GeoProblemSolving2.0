package cn.edu.njnu.geoproblemsolving.business.chatroom.hydrologicalconcept.support.georule.rule_property;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface Rule_PropertyDao extends MongoRepository<Rule_Property,String> {
    List<Rule_Property> findByFrom(String key);
}
