package cn.edu.njnu.geoproblemsolving.Dao.ShareToken;

import cn.edu.njnu.geoproblemsolving.Entity.ShareTokenEntity;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ShareTokenDaoImpl {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ShareTokenDaoImpl(MongoTemplate mongoTemplate){this.mongoTemplate = mongoTemplate;}

    public String getShareToken(JSONObject info, long ttlMillis){

        //暂时只是记录了时间和有效时间，后面按需应该限制使用范围
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKey = DatatypeConverter.parseBase64Binary("opengms");
        Key signKey = new SecretKeySpec(apiKey,signatureAlgorithm.getJcaName());
        JwtBuilder builder = Jwts.builder()
                .setIssuer(info.toJSONString())
                .setIssuedAt(now)
                .signWith(signatureAlgorithm, signKey);

        if (ttlMillis>0){
            long expMillis = nowMillis+ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        String fullToken = builder.compact();

        String tokenId = UUID.randomUUID().toString();
        ShareTokenEntity shareTokenEntity = new ShareTokenEntity();
        shareTokenEntity.setTokenId(tokenId);
        shareTokenEntity.setToken(fullToken);
        mongoTemplate.save(shareTokenEntity);
        return tokenId;
    }

    public Object checkShareToken(String tokenId){
        Query query = new Query(Criteria.where("tokenId").is(tokenId));
        List<ShareTokenEntity> result = mongoTemplate.find(query,ShareTokenEntity.class);
        if(!result.isEmpty()){
            try {
                String fullToken = result.get(0).getToken();
                Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary("opengms")).parseClaimsJws(fullToken).getBody();
                return JSONObject.parse(claims.getIssuer());
            }catch (Exception e){
                return false;
            }
        }else {
            return false;
        }
    }
}
