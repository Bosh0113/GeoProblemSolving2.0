package cn.edu.njnu.geoproblemsolving.business.activity.processDriven.service.impl;

import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.service.ResourceDispatch;
import cn.edu.njnu.geoproblemsolving.business.resource.dao.ActivityResDaoImpl;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.resource.service.Impl.ActivityResServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @ClassName ResourceDispatch
 * @Description 活动间资源调度中心
 * @Author zhngzhng
 * @Date 2021/7/21
 **/
@Service
public class ResourceDispatchImpl implements ResourceDispatch {
    private final ActivityResDaoImpl activityResDao;

    private final ActivityResServiceImpl activityResService;

    public ResourceDispatchImpl(ActivityResDaoImpl activityResDao, ActivityResServiceImpl activityResService) {
        this.activityResDao = activityResDao;
        this.activityResService = activityResService;
    }

    // TODO: 2021/8/13 现在资源的过滤只需很少的判定
    @Override
    public HashMap<String, HashSet<String>> getResourceTagInActivity(HashSet<String> nodeIds) {
        HashMap<String, HashSet<String>> resourceTag = new HashMap<>();
        HashSet<String> types = new HashSet<>();
        HashSet<String> suffixes = new HashSet<>();
        //遍历项目中的所有资源
        for (String aid : nodeIds){
            ArrayList<ResourceEntity> allFileInProject = activityResService.getAllFileInProject(aid);
            for (ResourceEntity item : allFileInProject){
                types.add(item.getType());
                suffixes.add(item.getSuffix());
            }
        }
        resourceTag.put("types", types);
        resourceTag.put("suffixes",suffixes);
        return resourceTag;
    }
}
