package cn.edu.njnu.geoproblemsolving.Service.Impl;

import cn.edu.njnu.geoproblemsolving.Entity.Activities.Activity;
import cn.edu.njnu.geoproblemsolving.Entity.Activities.Enums.ActivityPrivacy;
import cn.edu.njnu.geoproblemsolving.Entity.Activities.LinkProtocol;
import cn.edu.njnu.geoproblemsolving.Repository.ActivityRepository;
import cn.edu.njnu.geoproblemsolving.Repository.ProtocolRepository;
import cn.edu.njnu.geoproblemsolving.Service.ActivityService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

@Service
public class ActivityServiceImpl implements ActivityService {


    private final ActivityRepository activityRepository;
    private final ProtocolRepository protocolRepository;

    public ActivityServiceImpl(ActivityRepository activityRepository, ProtocolRepository protocolRepository) {
        this.activityRepository = activityRepository;
        this.protocolRepository = protocolRepository;
    }


    @Override
    public List<Activity> findByLevel(Integer level){
        return activityRepository.findByLevel(level);
    }

    @Override
    public List<Activity> findByPrivacy(ActivityPrivacy privacy){
        return activityRepository.findByPrivacy(privacy);
    }

    private Activity findByAid(String aid){
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
        Activity current = findByAid(aid);
        if(current == null) return null;

        ArrayList<String> children = current.getChildren();
        for(String childId : children){

            Activity child = findByAid(childId);
            if(child == null) continue;
            activities.add(child);
        }

        return activities;
    }

    @Override
    public List<Activity> findLast(String aid){
        List<Activity> activities = new ArrayList();
        Activity current = findByAid(aid);
        if(current == null) return null;

        JSONArray lastActivities = current.getLast();
        for(Object last : lastActivities) {
            if (last instanceof JSONObject) {

                Activity lastActivity = findByAid(((JSONObject) last).getString("aid"));
                if(lastActivity == null) continue;
                activities.add(lastActivity);
            }
        }
        return activities;
    }

    @Override
    public List<Activity> findNext(String aid){
        List<Activity> activities = new ArrayList();
        Activity current = findByAid(aid);
        if(current == null) return null;

        JSONArray nextActivities = current.getNext();
        for(Object next : nextActivities){
            if ( next instanceof JSONObject ) {

                Activity nextActivity = findByAid(((JSONObject)next).getString("aid"));
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
        Activity current = findByAid(aid);
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
            Activity current = findByAid(aid);
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
            Activity current = findByAid(aid);
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
            Activity activity1 = findByAid(aid1);
            if(activity1 == null) return null;
            activityRepository.save(saveLastActivityInfo(aid2, pid, activity1));

            // Update the next activity
            Activity activity2 = findByAid(aid2);
            if(activity2 == null) return null;
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
            Activity activity1 = findByAid(lastAid);
            if(activity1 == null) return null;
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
            Activity activity2 = findByAid(nextAid);
            if(activity2 == null) return null;
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
