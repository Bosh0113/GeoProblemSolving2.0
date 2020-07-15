package cn.edu.njnu.geoproblemsolving.Service.Impl;

import cn.edu.njnu.geoproblemsolving.Entity.Activities.Activity;
import cn.edu.njnu.geoproblemsolving.Entity.Activities.LinkProtocol;
import cn.edu.njnu.geoproblemsolving.Entity.User;
import cn.edu.njnu.geoproblemsolving.Repository.ActivityRepository;
import cn.edu.njnu.geoproblemsolving.Repository.ProtocolRepository;
import cn.edu.njnu.geoproblemsolving.Repository.UserRepository;
import cn.edu.njnu.geoproblemsolving.Service.ActivityService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ProtocolRepository protocolRepository;
    private final UserRepository userRepository;

    public ActivityServiceImpl(ActivityRepository activityRepository, ProtocolRepository protocolRepository, UserRepository userRepository) {
        this.activityRepository = activityRepository;
        this.protocolRepository = protocolRepository;
        this.userRepository = userRepository;
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
