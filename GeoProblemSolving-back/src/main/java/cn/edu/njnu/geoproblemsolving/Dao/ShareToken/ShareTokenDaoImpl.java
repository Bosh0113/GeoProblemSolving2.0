package cn.edu.njnu.geoproblemsolving.Dao.ShareToken;

import cn.edu.njnu.geoproblemsolving.Entity.ShareTokenEntity;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ShareTokenDaoImpl {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ShareTokenDaoImpl(MongoTemplate mongoTemplate){this.mongoTemplate = mongoTemplate;}

    public String getShareToken(long ttlMillis){

        //暂时只是记录了时间和有效时间，后面按需应该限制使用范围
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        JwtBuilder builder = Jwts.builder().setIssuedAt(now).setSubject(UUID.randomUUID().toString());

        if (ttlMillis>0){
            long expMillis = nowMillis+ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        String shareTokenStr = builder.compact().split("\\.")[1];

        ShareTokenEntity shareTokenEntity = new ShareTokenEntity();
        shareTokenEntity.setToken(shareTokenStr);
        mongoTemplate.save(shareTokenEntity);
        return shareTokenStr;
    }

    public Boolean checkShareToken(String tokenStr){
        Query query = new Query(Criteria.where("token").is(tokenStr));
        return !mongoTemplate.find(query,ShareTokenEntity.class).isEmpty();
    }
}
