package cn.edu.njnu.geoproblemsolving.business.activity.processDriven.service.impl;

import cn.edu.njnu.geoproblemsolving.business.activity.entity.Activity;
import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity.ActivityNode;
import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.service.NodeService;
import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.service.TagUtil;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ActivityNodeRepository;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ActivityRepository;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.SubprojectRepository;
import cn.edu.njnu.geoproblemsolving.business.resource.dao.ActivityResDao;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.resource.service.Impl.ActivityResServiceImpl;
import cn.hutool.http.HttpException;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
public class NodeServiceImpl implements NodeService {
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

    @Autowired
    SubprojectRepository subprojectRepository;

    @Value("${userServerLocation}")
    String userServer;


    @Override
    public ActivityNode createActivityNode(String aid, Integer level) {
        ActivityNode node = new ActivityNode();
        node.setId(aid);
        HashMap<String, String> activityUserTag = getActivityUserTag(aid, level);
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
        for (ResourceEntity file : files) {
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
            if (response.getInteger("code") != 0) {
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
        } catch (HttpException exception) {
            //后面改用 AOP 方式写日志
            System.out.println("Fail: getUserTagFromUserServer ->" + exception.toString());
            return null;
        }
    }


    public HashMap<String, String> getActivityUserTag(String aid, Integer level) {
        Activity activity;
        if (level == 0) {
            activity = subprojectRepository.findById(aid).get();
        } else {
            activity = activityRepository.findById(aid).get();
        }
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
            if (response.getInteger("code") != 0) {
                System.out.println("Fail: getUsersTag ->" + response.getString("msg"));
                return null;
            }
            HashMap<String, String> userTagMap = new HashMap<>();
            HashMap<String, HashMap<String, ArrayList<String>>> usersTags = JSONObject.parseObject(response.getJSONObject("data").toJSONString(), HashMap.class);
            for (Map.Entry<String, HashMap<String, ArrayList<String>>> item : usersTags.entrySet()) {
                String userId = item.getKey();
                String role = idRoleMap.get(userId);
                HashMap<String, ArrayList<String>> value = JSONObject.parseObject(JSONObject.toJSONString(item.getValue()), HashMap.class);
                JSONArray domain = JSONObject.parseObject(JSONObject.toJSONString(value.get("domain")), JSONArray.class);
                JSONArray organization = JSONObject.parseObject(JSONObject.toJSONString(value.get("organization")), JSONArray.class);
                String tags = TagUtil.setUserTag(role, domain, organization);
                userTagMap.put(userId, tags);
            }
            return userTagMap;
        } catch (HttpException exception) {
            System.out.println("Fail: getUsersTag -> " + exception.toString());
            return null;
        }
    }


    //只关注当前节点上的操作
    private HashMap<String, String> putNodeResource(String aid, Object res, String operationType) {
        Optional<ActivityNode> byId = nodeRepository.findById(aid);
        //无此节点，未进行过连接；等待连接即可。
        if (!byId.isPresent()) return null;
        ActivityNode node = byId.get();
        HashMap<String, String> resources = node.getResources();
        switch (operationType) {
            case "add":
                ResourceEntity addRes = (ResourceEntity) res;
                String resTag = TagUtil.setResourceTag(addRes);
                resources.put(addRes.getUid(), resTag);
                break;
            case "addBatch":
                HashMap<String, String> resInfo = JSONObject.parseObject((JSONObject.toJSONString(res)), HashMap.class);
                resources.putAll(resInfo);
                break;
            case "del":
                resources.remove(res);
                break;
            case "delBatch":
                HashSet<String> uids = (HashSet<String>) res;
                Iterator<String> iterator = uids.iterator();
                while (iterator.hasNext()) {
                    resources.remove(iterator.next());
                }
                break;
            case "put":
                ResourceEntity putRes = (ResourceEntity) res;
                String uid = putRes.getUid();
                String resTagStr = resources.get(uid);
                HashMap<String, String> tagMap = TagUtil.recoveryResTag(resTagStr);
                String type = putRes.getType();
                if (type != null && !type.equals("")) {
                    tagMap.put("type", type);
                }
                String suffix = putRes.getSuffix();
                if (suffix != null && !suffix.equals("")) {
                    tagMap.put("suffix", suffix);
                }
                String resourceTag = TagUtil.setResourceTag(tagMap);
                resources.put(uid, resourceTag);
                break;
        }
        nodeRepository.save(node);
        return resources;
    }

    @Override
    public void addResToNode(String aid, ResourceEntity res) {
        putNodeResource(aid, res, "add");
    }

    @Override
    public void addResToNodeBatch(String aid, HashMap<String, String> resInfo) {
        putNodeResource(aid, resInfo, "addBatch");
    }

    @Override
    public void delResInNode(String aid, String uid) {
        putNodeResource(aid, uid, "del");
    }

    @Override
    public void delResInNodeBatch(String aid, HashSet<String> uid) {
        putNodeResource(aid, uid, "delBatch");
    }

    @Override
    public void putResInNode(String aid, ResourceEntity res) {
        // todo 等待接入metadata，现在的无法修改
    }

    @Override
    public void addOrPutUserToNode(String aid, String userId, String userRole) {
        putNodeUser(aid, userId, userRole, "add");
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
        putNodeUser(aid, userId, null, "del");
    }


    //节点、活动层面同步的内容
    private ActivityNode putNodeUser(String aid, String userId, String role, String operationType) {
        Optional<ActivityNode> byId = nodeRepository.findById(aid);
        //无此节点，返回 null
        if (!byId.isPresent()) return null;
        ActivityNode node = byId.get();
        //members 同 node 中 members 字段地址相同
        HashMap<String, String> members = node.getMembers();
        switch (operationType) {
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
        return nodeRepository.save(node);
    }

    @Override
    public void addUserToNode(String aid, String userId, String role) {
        putNodeUser(aid, userId, role, "add");
    }

    @Override
    public void addUserToNodeBatch(String aid, HashSet<String> userIds) {
        Optional<ActivityNode> byId = nodeRepository.findById(aid);
        if (!byId.isPresent()) return;
        ActivityNode node = byId.get();
        HashMap<String, String> members = node.getMembers();
        for (String uid: userIds){
            String userTag = getUserTag(uid, "ordinary-member");
            members.put(uid, userTag);
        }
        nodeRepository.save(node);
    }

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void putUserInfoToNode(String userId, ArrayList<String> organizations, ArrayList<String> domains) {
        String key = "members." + userId;
        Criteria criteria = Criteria.where(key).exists(true);
        Query query = new Query(criteria);
        try {
            List<ActivityNode> activityNodes = mongoTemplate.find(query, ActivityNode.class);
            for (ActivityNode item : activityNodes) {
                HashMap<String, String> members = item.getMembers();
                String userTag = members.get(userId);
                int fIndex = userTag.indexOf("O517");
                int lIndex = userTag.lastIndexOf("O517");
                String oOrgs = userTag.substring(lIndex + 4);
                String oDomain = userTag.substring(fIndex + 4, lIndex);
                String oRole = userTag.substring(0, fIndex);
                if (organizations != null) {
                    JSONArray orgs = JSONArray.parseArray(JSONObject.toJSONString(organizations));
                    oOrgs = TagUtil.array2String(orgs);
                }
                if (domains != null) {
                    JSONArray domain = JSONArray.parseArray(JSONObject.toJSONString(domains));
                    oDomain = TagUtil.array2String(domain);
                }
                String newUserTag = TagUtil.addFlagInTags(oRole, oDomain, oOrgs);
                members.put(userId, newUserTag);
                mongoTemplate.save(item);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    @Override
    public void putUserInfoToNode(String userId,
                                  ArrayList<String> localOrg,
                                  ArrayList<String> serverOrg,
                                  ArrayList<String> localDomain,
                                  ArrayList<String> serverDomain) {

        if (localOrg == null) localOrg = new ArrayList<>();
        if (serverOrg == null) serverOrg = new ArrayList<>();
        if (localDomain == null) localDomain = new ArrayList<>();
        if (serverDomain == null) serverDomain = new ArrayList<>();
        boolean orgEq = localOrg.containsAll(serverOrg) && serverOrg.containsAll(localOrg);
        boolean domainEq = localDomain.containsAll(serverDomain) && serverDomain.containsAll(localDomain);
        if (!orgEq && !domainEq) {
            putUserInfoToNode(userId, serverOrg, serverDomain);
        } else if (!orgEq && domainEq) {
            putUserInfoToNode(userId, serverOrg, null);
        } else if (orgEq && !domainEq) {
            putUserInfoToNode(userId, null, serverDomain);
        }
    }
}
