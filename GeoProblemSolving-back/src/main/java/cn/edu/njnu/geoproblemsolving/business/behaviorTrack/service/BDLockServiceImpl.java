package cn.edu.njnu.geoproblemsolving.business.behaviorTrack.service;

import cn.edu.njnu.geoproblemsolving.business.behaviorTrack.entity.BehaviorDoc;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 * @ClassName BDLockServiceImpl
 * @Description 交互行为文档锁的操作
 * @Author zhngzhng
 * @Date 2022/7/6
 **/
@Slf4j
@Service
public class BDLockServiceImpl implements BDLockService {
    private final MongoTemplate mongoTemplate;

    public BDLockServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 获取锁、判断是否能操作该文档，如下两种情况可以获取到锁
     * 1. False 直接获取到锁
     * 2. True time < currentTime, 可获取到锁
     * upsert无此文档是创建
     *
     * @param bid        交互行为文档的 id
     * @param expiration 锁的过期时间
     * @return 是否能操作该文档
     */
    @Override
    public BehaviorDoc acquire(String bid, long expiration) {
        Query query = Query.query(Criteria.where("bid").is(bid));
        long releaseTime = System.currentTimeMillis() + 2000;
        Update update = new Update();
        update.set("lockFlag", true);
        update.set("expireAt", releaseTime);
        BehaviorDoc behaviorDoc = mongoTemplate.findAndModify(query, update, BehaviorDoc.class);
        //无此文档,几乎是不可能的发生的
        if (behaviorDoc == null) return null;
        //未上锁或者锁超时情况,可获取到锁
        if (!behaviorDoc.isLockFlag() || behaviorDoc.getExpireAt() <= System.currentTimeMillis()) {
            behaviorDoc.setExpireAt(releaseTime);
            return behaviorDoc;
        }
        return null;
    }

    /**
     * acquire 方法确保仅有一个client在对文档进行操作
     * 释放锁
     *
     * @param bid
     * @return
     */
    @Override
    public boolean release(String bid) {
        Query query = Query.query(Criteria.where("bid").is(bid));
        Update update = new Update()
                .addToSet("lockFlag", false)
                .addToSet("expireAt", System.currentTimeMillis());
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);
        BehaviorDoc behaviorDoc = mongoTemplate.findAndModify(query, update, options, BehaviorDoc.class);
        if (!behaviorDoc.isLockFlag()) return true;
        log.error("Unexpected result from release for behavior document lock {}", bid);
        return false;
    }

    /**
     * 延长锁的时间
     * @param bid
     * @param expiration
     * @return
     */
    @Override
    public boolean refresh(String bid, long expiration) {
        Query query = Query.query(Criteria.where("bid").is(bid));
        Update update = new Update()
                .addToSet("expireAt", System.currentTimeMillis() + expiration);
        UpdateResult updated = mongoTemplate.updateFirst(query, update, BehaviorDoc.class);
        final boolean refreshed = updated.getModifiedCount() == 1;
        if (refreshed) log.debug("Refresh lock successfully affected 1 record for id {}", bid);
        else log.warn("Refresh didn't affect any records for id {}", bid);
        return refreshed;
    }
}
