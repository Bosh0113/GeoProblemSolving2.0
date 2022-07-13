package cn.edu.njnu.geoproblemsolving.business.behaviorTrack.service;

import cn.edu.njnu.geoproblemsolving.business.activity.entity.Activity;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.ActivityType;
import cn.edu.njnu.geoproblemsolving.business.behaviorTrack.entity.BehaviorDoc;
import cn.edu.njnu.geoproblemsolving.business.behaviorTrack.enums.InteractiveBehavior;
import cn.edu.njnu.geoproblemsolving.business.behaviorTrack.enums.Scene;
import cn.edu.njnu.geoproblemsolving.business.behaviorTrack.enums.SupportElement;
import cn.edu.njnu.geoproblemsolving.business.behaviorTrack.repository.BehaviorDocRepository;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * @ClassName BehaviorTrackServiceImpl
 * @Description 交互行为组织及追溯的具体实现
 * @Author zhngzhng
 * @Date 2022/7/6
 **/
@Service
@Slf4j
public class BehaviorTrackServiceImpl implements BehaviorTrackService {
    private final BDLockServiceImpl bdLockService;
    private final BehaviorDocRepository docRepository;

    public BehaviorTrackServiceImpl(BDLockServiceImpl bdLockService, BehaviorDocRepository docRepository) {
        this.bdLockService = bdLockService;
        this.docRepository = docRepository;
    }

    private final long LOCK_EXPIRATION = 2000;

    /**
     * 尝试获取该文档的锁
     * @param bid 交互行为文档的id
     * @return 行为文档/null
     */
    @Override
    public BehaviorDoc tryAcquireLock(String bid) {
        while (true) {
            BehaviorDoc behaviorDoc = bdLockService.acquire(bid, LOCK_EXPIRATION);
            //拿到锁，可对文档进行操作
            if (behaviorDoc != null) return behaviorDoc;
            try {
                Thread.sleep(LOCK_EXPIRATION);
            }catch (InterruptedException e) {
                log.error("Acquire lock failed: {}", e.toString());
            }
        }
    }

    @Override
    public void tryReleaseLock(String bid) {
        bdLockService.release(bid);
    }

    @Override
    public void tryRefreshLock(String bid, long expirationTime) {
        bdLockService.refresh(bid, expirationTime);
    }

    /**
     *
     * @param pid
     */
    @Override
    public void initBehaviorDoc(String pid) {
        Document behaviorDoc = BDHelper.createBehaviorDoc(pid);
        BehaviorDoc docEntity = new BehaviorDoc();
        docEntity.setBid(pid);
        docEntity.setDocument(behaviorDoc.asXML());
        docRepository.save(docEntity);
    }

    @Override
    public void initBehaviorDoc(String pid, Activity activity, String userId) {

    }

    @Override
    public void appendAcv(String pid, Activity activity, String userId) {
        BehaviorDoc behaviorDoc = tryAcquireLock(pid);
        String docStr = behaviorDoc.getDocument();
        Element behaviorEle = BDHelper.createBehaviorEle(activity.getAid(), Scene.activityStructure, InteractiveBehavior.createActivity);
        String name = activity.getName();
        String type = activity.getType().toString();
        String description = activity.getDescription();
        LinkedHashSet<String> attrs = new LinkedHashSet<>();
        attrs.add("Name");
        attrs.add("Type");
        attrs.add("Description");
        BDHelper.appendSubject(behaviorEle, SupportElement.person, userId);


        LinkedMultiValueMap<String, String> infoMap = new LinkedMultiValueMap<>();
        infoMap.add("Text", name);
        infoMap.add("Text", type);
        infoMap.add("Text", description);

        Element ele = BDHelper.createOperationEle("input", infoMap);
        behaviorEle.add(ele);

        BDHelper.appendObjectAndAttrs(behaviorEle, SupportElement.activity, activity.getAid(), attrs);
        try {
            Document doxXML = DocumentHelper.parseText(docStr);
            Element rootElement = doxXML.getRootElement();
            rootElement.add(behaviorEle);
            behaviorDoc.setDocument(doxXML.asXML());
            docRepository.save(behaviorDoc);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
