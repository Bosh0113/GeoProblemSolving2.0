package cn.edu.njnu.geoproblemsolving.domain.chatroom.hydrologicalconcept.support.georule.rule_shape;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface Rule_ShapeDao extends MongoRepository<Rule_Shape,String> {
    List<Rule_Shape> findByFrom(String key);
}
