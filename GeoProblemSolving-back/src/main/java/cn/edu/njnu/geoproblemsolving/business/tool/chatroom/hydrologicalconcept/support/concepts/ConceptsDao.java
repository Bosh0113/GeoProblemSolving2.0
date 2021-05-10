package cn.edu.njnu.geoproblemsolving.business.tool.chatroom.hydrologicalconcept.support.concepts;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConceptsDao extends MongoRepository<Concepts,String> {
    Concepts findByName(String name);
    Concepts findByConceptID(String conceptId);
}
