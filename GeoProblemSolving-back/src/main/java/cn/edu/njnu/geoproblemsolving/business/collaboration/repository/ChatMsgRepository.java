package cn.edu.njnu.geoproblemsolving.business.collaboration.repository;

import cn.edu.njnu.geoproblemsolving.business.collaboration.entity.ChatMsg;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;
import java.util.List;

@RepositoryRestResource(path = "ChatMessage")
public interface ChatMsgRepository extends MongoRepository<ChatMsg, String> {

    List<ChatMsg> findAllByAid(String aid, PageRequest pageRequest);

    List<ChatMsg> findByContentContainingAndAid(String key, String aid);

    List<ChatMsg> findByTimeBetweenAndAid(Date from, Date to, String aid);
}
