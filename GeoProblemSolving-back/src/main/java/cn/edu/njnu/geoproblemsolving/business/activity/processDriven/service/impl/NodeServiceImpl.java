package cn.edu.njnu.geoproblemsolving.business.activity.processDriven.service.impl;

import cn.edu.njnu.geoproblemsolving.business.activity.entity.Activity;
import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity.ActivityNode;
import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.service.NodeService;
import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.service.TagUtil;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ActivityNodeRepository;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ActivityRepository;
import cn.edu.njnu.geoproblemsolving.business.resource.dao.ActivityResDao;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.resource.service.Impl.ActivityResServiceImpl;
import cn.hutool.http.HttpException;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName NodeServiceImpl
 * @Description 节点层面的操作
 * 使用 AOP 方式来实现，实际是流程驱动中间的内容
 * @Author zhngzhng
 * @Date 2021/8/18
 **/
@Service
public class NodeServiceImpl implements NodeService{
    @Autowired
    ActivityResServiceImpl activityResService;
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    ActivityNodeRepository nodeRepository;

    @Autowired
    ActivityResDao activityResDao;

    @Value("${userServerLocation}")
    String userServer;

    @Override
    public ActivityNode createActivityNode(String aid) {
        ActivityNode node = new ActivityNode();
        node.setId(aid);
        HashMap<String, String> activityUserTag = getActivityUserTag(aid);
        HashMap<String, String> activityResourceTag = getActivityResourceTag(aid);
        node.setMembers(activityUserTag);
        node.setResources(activityResourceTag);
        node.setCreateDate(new Date());
        return nodeRepository.save(node);
    }

    @Override
    public ActivityNode nodeIsPresent(String aid) {
        Optional<ActivityNode> byId = nodeRepository.findById(aid);
        return byId.isPresent() ? byId.get() : null;
    }

    @Override
    public HashMap<String, String> getActivityResourceTag(String aid) {
        ArrayList<ResourceEntity> files = activityResService.getAllFileInProject(aid);
        HashMap<String, String> fileTagMap = new HashMap<>();
        for (ResourceEntity file : files){
            // 仅流动项目中的 public 资源
            if (file.getPrivacy().equals("private")) continue;
            String fileTagStr = TagUtil.setResourceTag(file);
            String uid = file.getUid();
            fileTagMap.put(uid, fileTagStr);
        }
        return fileTagMap;
    }

    public String getUserTag(String userId, String role) {
        HashMap<String, JSONArray> userTagMap = getUserTagFromUserServer(userId);
        if (userTagMap == null) return null;
        JSONArray domain = userTagMap.get("domain");
        JSONArray organization = userTagMap.get("organization");
        return TagUtil.setUserTag(role, domain, organization);
    }

    public HashMap<String, JSONArray> getUserTagFromUserServer(String userId) {
        String getTagUrl = "http://" + userServer + "/user/tag/" + userId;
        try {
            JSONObject response = restTemplate.getForObject(getTagUrl, JSONObject.class);
            if (response.getInteger("code") != 0){
                System.out.println("Fail: getUserTagFromUserServer ->" + response.getString("msg"));
                return null;
            }
            JSONObject userTags = response.getJSONObject("data");
            JSONArray domain = userTags.getJSONArray("domain");
            JSONArray organization = userTags.getJSONArray("organization");
            HashMap<String, JSONArray> userTagMap = new HashMap<>();
            userTagMap.put("domain", domain);
            userTagMap.put("organization", organization);
            return userTagMap;
        }catch (HttpException exception){
            //后面改用 AOP 方式写日志
            System.out.println("Fail: getUserTagFromUserServer ->" + exception.toString());
            return null;
        }
    }


    public HashMap<String, String> getActivityUserTag(String aid){
        Optional<Activity> byId = activityRepository.findById(aid);
        if (!byId.isPresent()) return null;
        Activity activity = byId.get();
        JSONArray members = activity.getMembers();
        HashMap<String, String> idRoleMap = new HashMap<>();
        for (int i = 0; i < members.size(); i++) {
            JSONObject member = members.getJSONObject(i);
            String userId = member.getString("userId");
            String role = member.getString("role");
            idRoleMap.put(userId, role);
        }
        return getUsersTag(idRoleMap);
    }


    public HashMap<String, String> getUsersTag(HashMap<String, String> idRoleMap) {
        if (idRoleMap == null || idRoleMap.size() == 0) return null;
        Set<String> idSet = idRoleMap.keySet();
        String userIdStr = idSet.stream().collect(Collectors.joining(","));
        String getTagUrl = "http://" + userServer + "/user/tags/" + userIdStr;
        try {
            JSONObject response = restTemplate.getForObject(getTagUrl, JSONObject.class);
            if (response.getInteger("code") != 0){
                System.out.println("Fail: getUsersTag ->" + response.getString("msg"));
                return null;
            }
            HashMap<String, String> userTagMap = new HashMap<>();
            HashMap<String, HashMap<String, ArrayList<String>>> usersTags = JSONObject.parseObject(response.getJSONObject("Data").toJSONString(), HashMap.class);
            for (Map.Entry<String, HashMap<String, ArrayList<String>>> item : usersTags.entrySet()){
                String userId = item.getKey();
                String role = idRoleMap.get(userId);
                HashMap<String, ArrayList<String>> value = item.getValue();
                JSONArray domain = JSONObject.parseObject(JSONObject.toJSONString(value.get("domain")), JSONArray.class);
                JSONArray organization = JSONObject.parseObject(JSONObject.toJSONString(value.get("organization")), JSONArray.class);
                String tags = TagUtil.setUserTag(role, domain, organization);
                userTagMap.put(userId, tags);
            }
            return userTagMap;
        }catch (HttpException exception){
            System.out.println("Fail: getUsersTag -> " + exception.toString());
            return null;
        }
    }

    //节点、活动层面同步的内容
    private HashMap<String, String> putNodeUser(String aid, String userId,String role, String operationType){
        Optional<ActivityNode> byId = nodeRepository.findById(aid);
        //无此节点，返回 null
        if (!byId.isPresent()) return null;
        ActivityNode node = byId.get();
        //members 同 node 中 members 字段地址相同
        HashMap<String, String> members = node.getMembers();
        switch (operationType){
            case "add":
                //涉及到人的信息变动，直接从全部更新即可
                String userTag = getUserTag(userId, role);
                members.put(userId, userTag);
                break;
            case "del":
                members.remove(userId);
                break;
            default:
                return null;
        }
        return members;
    }

    //只关注当前节点上的操作
    private HashMap<String, String> putNodeResource(String aid, String operationType, ResourceEntity res){
        Optional<ActivityNode> byId = nodeRepository.findById(aid);
        if (!byId.isPresent()) return null;
        ActivityNode node = byId.get();
        HashMap<String, String> resources = node.getResources();
        String uid = res.getUid();
        switch (operationType){
            case "add":
                String resTag = TagUtil.setResourceTag(res);
                resources.put(uid, resTag);
                break;
            case "del":
                resources.remove(uid);
                break;
            case "put":
                String resTagStr = resources.get(uid);
                HashMap<String, String> tagMap = TagUtil.recoveryResTag(resTagStr);
                String type = res.getType();
                if (type != null && !type.equals("")){
                    tagMap.put("type", type);
                }
                String suffix = res.getSuffix();
                if (suffix != null && !suffix.equals("")){
                    tagMap.put("suffix", suffix);
                }
                String resourceTag = TagUtil.setResourceTag(tagMap);
                resources.put(uid, resourceTag);
                break;
        }
        return resources;
    }

    @Override
    public void addOrPutUserToNode(String aid, String userId, String userRole) {
        HashMap<String, String> users = putNodeUser(aid, userId, userRole, "add");
        ActivityNode node = nodeRepository.findById(aid).get();
        node.setMembers(users);
        nodeRepository.save(node);
    }

    @Override
    public void userFlowToNode(String aid, String userId) {
        Activity activity = activityRepository.findById(aid).get();
        JSONArray members = activity.getMembers();
        JSONObject member = new JSONObject();
        member.put("role", "ordinary-member");
        member.put("userId", userId);
        members.add(member);
        activityRepository.save(activity);
        addOrPutUserToNode(aid, userId, "ordinary-member");
    }

    @Override
    public void userExitActivity(String aid, String userId) {
        HashMap<String, String> members = putNodeUser(aid, userId, null, "del");
        ActivityNode node = nodeRepository.findById(aid).get();
        node.setMembers(members);
        nodeRepository.save(node);
    }
}
