package cn.edu.njnu.geoproblemsolving.domain.activity.service.Impl;

import cn.edu.njnu.geoproblemsolving.Dao.Folder.FolderDaoImpl;
import cn.edu.njnu.geoproblemsolving.domain.activity.Activity;
import cn.edu.njnu.geoproblemsolving.domain.activity.LinkProtocol;
import cn.edu.njnu.geoproblemsolving.domain.activity.Subproject;
import cn.edu.njnu.geoproblemsolving.domain.activity.repository.SubprojectRepository;
import cn.edu.njnu.geoproblemsolving.domain.activity.service.ActivityService;
import cn.edu.njnu.geoproblemsolving.domain.tool.ToolsetEntity;
import cn.edu.njnu.geoproblemsolving.domain.user.User;
import cn.edu.njnu.geoproblemsolving.domain.activity.repository.ActivityRepository;
import cn.edu.njnu.geoproblemsolving.domain.activity.repository.ProtocolRepository;
import cn.edu.njnu.geoproblemsolving.domain.user.repository.UserRepository;
import cn.edu.njnu.geoproblemsolving.domain.tool.ToolEntity;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ProtocolRepository protocolRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;
    private final FolderDaoImpl folderDao;
    private final SubprojectRepository subprojectRepository;

    @Autowired
    public ActivityServiceImpl(ActivityRepository activityRepository,
                               ProtocolRepository protocolRepository,
                               UserRepository userRepository,
                               SubprojectRepository subprojectRepository,
                               MongoTemplate mongoTemplate,
                               FolderDaoImpl folderDao) {
        this.activityRepository = activityRepository;
        this.protocolRepository = protocolRepository;
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
        this.folderDao = folderDao;
        this.subprojectRepository = subprojectRepository;
    }

    private User findByUserId(String userId){
        Optional optional = userRepository.findById(userId);
        if(optional.isPresent()){
            Object user = optional.get();
            return (User)user;
        }
        else {
            return null;
        }
    }

    public Activity findActivityById(String aid){
        Optional optional = activityRepository.findById(aid);
        if(optional.isPresent()){
            Object activity = optional.get();
            return (Activity)activity;
        }
        else {
            return null;
        }
    }

    @Override
    public Object createActivity(Activity activity){
        try{
            // aid
            String aid = UUID.randomUUID().toString();
            activity.setAid(aid);

            // created time
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            activity.setCreatedTime(dateFormat.format(date));

            // update project info
            String subprojectId = activity.getParent();
            Subproject subproject = subprojectRepository.findById(subprojectId).get();
            if(subproject == null) return "Fail: subproject does not exist";
            ArrayList<String> children = subproject.getChildren();
            children.add(subprojectId);
            subproject.setChildren(children);
            subprojectRepository.save(subproject);

            // members
            String creatorId = activity.getCreator();
            JSONObject creator = new JSONObject();
            JSONObject member = new JSONObject();
            JSONArray members = new JSONArray();

            creator.put("userId", creatorId);
            creator.put("role", "creator");
            members.add(creator);
            creator.put("userId", subproject.getCreator());
            creator.put("role", "administrator");
            members.add(member);
            activity.setMembers(members);

            // tools and toolsets
            Query queryPublic = new Query(Criteria.where("privacy").is("Public"));
            List<ToolEntity> toolEntities = mongoTemplate.find(queryPublic, ToolEntity.class);
            ArrayList<String> tools = new ArrayList<>();
            for (ToolEntity toolEntity:toolEntities){
                tools.add(toolEntity.getTid());
            }
            activity.setToolList(tools);

            List<ToolsetEntity> toolsetEntities = mongoTemplate.find(queryPublic,ToolsetEntity.class);
            ArrayList<String> toolsets = new ArrayList<>();
            for (ToolsetEntity toolsetEntity: toolsetEntities){
                toolsets.add(toolsetEntity.getTsid());
            }
            activity.setToolsetList(toolsets);

            //folder
            folderDao.createFolder(activity.getName(), "", aid);

            activityRepository.save(activity);

            return activity;
        }
        catch (Exception ex){
            return "Fail: Exception";
        }
    }

    @Override
    public List<Activity> findChildren(String aid) {
        List<Activity> activities = new ArrayList();
        Activity current = findActivityById(aid);
        if(current == null) return null;

        ArrayList<String> children = current.getChildren();
        for(String childId : children){

            Activity child = findActivityById(childId);
            if(child == null) continue;
            activities.add(child);
        }

        return activities;
    }

    @Override
    public List<Activity> findLast(String aid){
        List<Activity> activities = new ArrayList();
        Activity current = findActivityById(aid);
        if(current == null) return null;

        JSONArray lastActivities = current.getLast();
        for(Object last : lastActivities) {
            if (last instanceof JSONObject) {

                Activity lastActivity = findActivityById(((JSONObject) last).getString("aid"));
                if(lastActivity == null) continue;
                activities.add(lastActivity);
            }
        }
        return activities;
    }

    @Override
    public List<Activity> findNext(String aid){
        List<Activity> activities = new ArrayList();
        Activity current = findActivityById(aid);
        if(current == null) return null;

        JSONArray nextActivities = current.getNext();
        for(Object next : nextActivities){
            if ( next instanceof JSONObject ) {

                Activity nextActivity = findActivityById(((JSONObject)next).getString("aid"));
                if(nextActivity == null) continue;
                activities.add(nextActivity);
            }
        }
        return activities;
    }

    @Override
    public Activity createChild(String aid, Activity activity){
        // Save the child activity
        Activity child = activityRepository.save(activity);
        String childId = child.getAid();

        // Update the current activity
        Activity current = findActivityById(aid);
        if(current == null) return null;

        ArrayList<String> children = current.getChildren();
        children.add(childId);
        current.setChildren(children);
        activityRepository.save(current);

        return child;
    }

    @Override
    public Activity createNext(String aid, Activity activity, LinkProtocol protocol){
        try {
            // Save the protocol
            String pid = protocolRepository.save(protocol).getPid();

            // Save the next activity
            Activity next = activityRepository.save(saveNextActivityInfo(aid, pid, activity));
            String nextId = next.getAid();

            // Update the current activity
            Activity current = findActivityById(aid);
            if(current == null) return null;
            activityRepository.save(saveLastActivityInfo(nextId, pid, current));

            return next;
        }
        catch (Exception ex){
            System.out.println("Exception in createNext of ActivityServiceImpl");
            return null;
        }
    }

    @Override
    public Activity createLast(String aid, Activity activity, LinkProtocol protocol){
        try {
            // Save the protocol
            String pid = protocolRepository.save(protocol).getPid();

            // Save the last activity
            Activity last = activityRepository.save(saveLastActivityInfo(aid, pid, activity));
            String lastId = last.getAid();

            // Update the current activity
            Activity current = findActivityById(aid);
            if(current == null) return null;
            activityRepository.save(saveNextActivityInfo(lastId, pid, current));

            return last;
        }
        catch (Exception ex){
            System.out.println("Exception in createLast of ActivityServiceImpl");
            return null;
        }
    }

    @Override
    public String linkActivities(String aid1, String aid2, LinkProtocol protocol){
        try {
            // Save the protocol
            String pid = protocolRepository.save(protocol).getPid();

            // Save the last activity
            Activity activity1 = findActivityById(aid1);
            if(activity1 == null) return "Fail: activity does not exist";
            activityRepository.save(saveLastActivityInfo(aid2, pid, activity1));

            // Update the next activity
            Activity activity2 = findActivityById(aid2);
            if(activity2 == null) return "Fail: activity does not exist";
            activityRepository.save(saveNextActivityInfo(aid1, pid, activity2));

            return "Success";
        }
        catch (Exception ex){
            System.out.println("Exception in linkActivities of ActivityServiceImpl");
            return "Fail";
        }
    }

    @Override
    public String separateActivities(String lastAid, String nextAid){
        try {
            // Save the last activity
            Activity activity1 = findActivityById(lastAid);
            if(activity1 == null) return "Fail: activity does not exist";
            JSONArray nextActivities = activity1.getNext();

            IntStream.range(0, nextActivities.size()).forEach(i -> {
                String aid = nextActivities.getJSONObject(i).getString("aid");
                if (aid.equals(nextAid)) {
                    nextActivities.remove(i);
                }
            });
            activity1.setNext(nextActivities);
            activityRepository.save(activity1);

            // Save the next activity
            Activity activity2 = findActivityById(nextAid);
            if(activity2 == null) return "Fail: activity does not exist";
            JSONArray lastActivities = activity2.getLast();

            IntStream.range(0, lastActivities.size()).forEach(i -> {
                String aid = lastActivities.getJSONObject(i).getString("aid");
                if (aid.equals(lastAid)) {
                    lastActivities.remove(i);
                }
            });
            activity2.setLast(lastActivities);
            activityRepository.save(activity2);

            return "Success";
        }
        catch (Exception ex){
            System.out.println("Exception in separateActivities of ActivityServiceImpl");
            return "Fail";
        }
    }

    @Override
    public String joinActivity(String aid, String userId){
        try {
            User user = findByUserId(userId);
            if(user == null) return "Fail: user does not exist";
            JSONObject userInfo = new JSONObject();
            userInfo.put("userId", user.getUserId());
            userInfo.put("name", user.getName());
            userInfo.put("avatar", user.getAvatar());
            userInfo.put("role", "");

            Activity activity = findActivityById(aid);
            if(activity == null) return "Fail: activity does not exist";
            JSONArray members = activity.getMembers();
            members.add(userInfo);
            activity.setMembers(members);
            activityRepository.save(activity);

            return "Success";
        }
        catch (Exception ex){
            return "Fail: exception";
        }
    }

    @Override
    public String quitActivity(String aid, String userId){
        try {
            Activity activity = findActivityById(aid);
            if(activity == null) return "Fail: activity does not exist";

            JSONArray members = activity.getMembers();
            for(Object member: members){
                if(member instanceof JSONObject){
                    if(((JSONObject) member).get("userId").equals(userId)){
                        members.remove(member);
                        break;
                    }
                }
            }
            activity.setMembers(members);
            activityRepository.save(activity);

            return "Success";
        }
        catch (Exception ex){
            return "Fail: exception";
        }
    }

    private Activity saveLastActivityInfo(String aid, String pid, Activity activity){
        JSONObject nextInfo = new JSONObject();
        nextInfo.put("aid", aid);
        nextInfo.put("protocolId", pid);

        JSONArray nextActivities = activity.getNext();
        nextActivities.add(nextInfo);
        activity.setNext(nextActivities);

        return activity;
    }

    private Activity saveNextActivityInfo(String aid, String pid, Activity activity){
        JSONObject lastInfo = new JSONObject();
        lastInfo.put("aid", aid);
        lastInfo.put("protocolId", pid);

        JSONArray lastActivities = activity.getLast();
        lastActivities.add(lastInfo);
        activity.setLast(lastActivities);

        return activity;
    }
}
