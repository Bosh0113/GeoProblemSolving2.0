package cn.edu.njnu.geoproblemsolving.business.tool.chatroom.hydrologicalconcept.support.georule.rule_process;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface Rule_ProcessDao extends MongoRepository<Rule_Process,String> {
    List<Rule_Process> findByFrom(String key);
}
