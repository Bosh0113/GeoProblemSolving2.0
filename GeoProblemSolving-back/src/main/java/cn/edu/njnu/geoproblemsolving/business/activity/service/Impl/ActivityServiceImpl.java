package cn.edu.njnu.geoproblemsolving.business.activity.service.Impl;

import cn.edu.njnu.geoproblemsolving.Dao.Folder.FolderDaoImpl;
import cn.edu.njnu.geoproblemsolving.business.activity.dto.UpdateActivityDTO;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Project;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.ActivityType;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Activity;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.LinkProtocol;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Subproject;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.SubprojectRepository;
import cn.edu.njnu.geoproblemsolving.business.activity.service.ActivityService;
import cn.edu.njnu.geoproblemsolving.business.tool.ToolsetEntity;
import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ActivityRepository;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ProtocolRepository;
import cn.edu.njnu.geoproblemsolving.business.user.repository.UserRepository;
import cn.edu.njnu.geoproblemsolving.business.tool.ToolEntity;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
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

    private User findByUserId(String userId) {
        Optional optional = userRepository.findById(userId);
        if (optional.isPresent()) {
            Object user = optional.get();
            return (User) user;
        } else {
            return null;
        }
    }

    public Activity findActivityById(String aid) {
        Optional optional = activityRepository.findById(aid);
        if (optional.isPresent()) {
            Object activity = optional.get();
            return (Activity) activity;
        } else {
            return null;
        }
    }

    private Activity saveLastActivityInfo(String aid, String pid, Activity last) {
        JSONObject nextInfo = new JSONObject();
        nextInfo.put("aid", aid);
        nextInfo.put("protocolId", pid);

        JSONArray nextActivities = last.getNext();
        nextActivities.add(nextInfo);
        last.setNext(nextActivities);

        return last;
    }

    private Activity saveNextActivityInfo(String aid, String pid, Activity next) {
        JSONObject lastInfo = new JSONObject();
        lastInfo.put("aid", aid);
        lastInfo.put("protocolId", pid);

        JSONArray lastActivities = next.getLast();
        lastActivities.add(lastInfo);
        next.setLast(lastActivities);

        return next;
    }

    @Override
    public JsonResult findActivity(String aid) {
        try {
            Activity activity = findActivityById(aid);
            if (activity == null) return ResultUtils.error(-1, "Fail: activity does not exist.");

            return ResultUtils.success(activity);
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult createActivity(Activity activity) {
        try {
            // aid
            String aid = UUID.randomUUID().toString();
            activity.setAid(aid);

            // created time
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            activity.setCreatedTime(dateFormat.format(date));
            activity.setActiveTime(dateFormat.format(date));

            // members
            String creatorId = activity.getCreator();
            JSONObject creator = new JSONObject();
            JSONArray members = new JSONArray();

            creator.put("userId", creatorId);
            creator.put("role", "manager");
            members.add(creator);

            activity.setMembers(members);

            // set type
            activity.setType(activity.getType());

            // tools and toolsets
            Query queryPublic = new Query(Criteria.where("privacy").is("Public"));
            List<ToolEntity> toolEntities = mongoTemplate.find(queryPublic, ToolEntity.class);
            ArrayList<String> tools = new ArrayList<>();
            for (ToolEntity toolEntity : toolEntities) {
                tools.add(toolEntity.getTid());
            }
            activity.setToolList(tools);

            List<ToolsetEntity> toolsetEntities = mongoTemplate.find(queryPublic, ToolsetEntity.class);
            ArrayList<String> toolsets = new ArrayList<>();
            for (ToolsetEntity toolsetEntity : toolsetEntities) {
                toolsets.add(toolsetEntity.getTsid());
            }
            activity.setToolsetList(toolsets);

            //folder
            folderDao.createFolder(activity.getName(), "", aid);

            // update parent activity info
            if(activity.getLevel() == 2) {
                String subprojectId = activity.getParent();
                Optional optional = subprojectRepository.findById(subprojectId);
                if (!optional.isPresent()) return ResultUtils.error(-1, "Fail: parent activity does not exist.");
                Subproject parent = (Subproject) optional.get();

                ArrayList<String> children = parent.getChildren();
                children.add(subprojectId);
                parent.setChildren(children);
                subprojectRepository.save(parent);
            } else {
                String activityId = activity.getParent();
                Optional optional = activityRepository.findById(activityId);
                if (!optional.isPresent()) return ResultUtils.error(-1, "Fail: parent activity does not exist.");
                Activity parent = (Activity) optional.get();

                ArrayList<String> children = parent.getChildren();
                children.add(activityId);
                parent.setChildren(children);
                activityRepository.save(parent);
            }

            //save
            activityRepository.save(activity);

            return ResultUtils.success(activity);
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult updateActivity(String aid, UpdateActivityDTO update) {
        try {
            // confirm
            Optional result = activityRepository.findById(aid);
            if (!result.isPresent()) return ResultUtils.error(-1, "Fail: activity does not exist.");
            Activity activity = (Activity) result.get();

            update.updateTo(activity);
            return ResultUtils.success(activityRepository.save(activity));
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult deleteActivity(String aid) {
        try {
            // confirm
            Optional optional = activityRepository.findById(aid);
            if (! optional.isPresent())  return ResultUtils.error(-1, "Fail: activity does not exist.");
            Activity activity = (Activity) optional.get();

            if(activity.getLevel() == 2) {
                optional = subprojectRepository.findById(activity.getParent());
                if (!optional.isPresent()) return ResultUtils.error(-1, "Fail: parent activity does not exist.");
                Subproject parent = (Subproject) optional.get();

                // delete from parent
                if(parent.getChildren().contains(aid))
                    parent.getChildren().remove(aid);
                subprojectRepository.save(parent);
            } else {
                optional = activityRepository.findById(activity.getParent());
                if (!optional.isPresent()) return ResultUtils.error(-1, "Fail: parent activity does not exist.");
                Activity parent = (Activity) optional.get();

                // delete from parent
                if(parent.getChildren().contains(aid))
                    parent.getChildren().remove(aid);
                activityRepository.save(parent);
            }

            activityRepository.deleteById(aid);
            return ResultUtils.success("Success");
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult findChildren(String aid) {
        try {
            // confirm
            Optional optional = activityRepository.findById(aid);
            if (!optional.isPresent()) return ResultUtils.error(-1, "Fail: activity does not exist.");
            Activity activity = (Activity) optional.get();

            JSONArray activities = new JSONArray();
            for (String childId : activity.getChildren()) {
                optional = activityRepository.findById(childId);
                if (optional.isPresent()) {
                    Activity childActivity = (Activity) optional.get();
                    activities.add(childActivity);
                }
            }

            return ResultUtils.success(activities);
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult findParticipants(String aid){
        try {
            Optional optional = activityRepository.findById(aid);
            if (!optional.isPresent()) return ResultUtils.error(-1, "Fail: subproject does not exist.");
            Activity activity = (Activity) optional.get();

            // creator
            JSONObject participants = new JSONObject();
            User creator = findByUserId(activity.getCreator());
            participants.put("creator", creator);

            // members
            JSONArray members = activity.getMembers();
            JSONArray memberinfos = new JSONArray();
            for (Object member : members) {
                if (member instanceof JSONObject) {
                    User user = findByUserId(((JSONObject) member).getString("userId"));
                    JSONObject userInfo = (JSONObject) member;
                    userInfo.put("name", user.getName());
                    userInfo.put("avatar", user.getAvatar());
                    userInfo.put("email", user.getEmail());
                    userInfo.put("title", user.getTitle());
                    memberinfos.add(userInfo);
                }
            }
            participants.put("members", memberinfos);

            return ResultUtils.success(participants);
        }
        catch (Exception ex){
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult findLast(String aid) {
        try {
            List<Activity> activities = new ArrayList();
            Activity current = findActivityById(aid);
            if (current == null) return ResultUtils.error(-1, "Fail: activity does not exist.");

            JSONArray lastActivities = current.getLast();
            for (Object last : lastActivities) {
                if (last instanceof JSONObject) {
                    Activity lastActivity = findActivityById(((JSONObject) last).getString("aid"));
                    if (lastActivity == null) continue;
                    activities.add(lastActivity);
                }
            }
            return ResultUtils.success(activities);
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult findNext(String aid) {
        try {
            List<Activity> activities = new ArrayList();
            Activity current = findActivityById(aid);
            if (current == null) return ResultUtils.error(-1, "Fail: activity does not exist.");

            JSONArray nextActivities = current.getNext();
            for (Object next : nextActivities) {
                if (next instanceof JSONObject) {

                    Activity nextActivity = findActivityById(((JSONObject) next).getString("aid"));
                    if (nextActivity == null) continue;
                    activities.add(nextActivity);
                }
            }
            return ResultUtils.success(activities);
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }

    }

    @Override
    public JsonResult createNext(String aid, String nextId, LinkProtocol protocol) {
        try {
            // Confirm aid
            Activity current = findActivityById(aid);
            if (current == null) return ResultUtils.error(-1, "Fail: current activity does not exist.");
            Activity next = findActivityById(nextId);
            if (next == null) return ResultUtils.error(-1, "Fail: next activity does not exist.");

            // Save the protocol
            String pid = UUID.randomUUID().toString();
            protocol.setPid(pid);

            next = saveNextActivityInfo(aid, pid, next);
            current = saveLastActivityInfo(nextId, pid, current);

            protocolRepository.save(protocol);
            activityRepository.save(next);
            activityRepository.save(current);

            return ResultUtils.success(current);
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult createLast(String aid, String lastId, LinkProtocol protocol) {
        try {
            // Confirm aid
            Activity current = findActivityById(aid);
            if (current == null) return ResultUtils.error(-1, "Fail: activity does not exist.");
            Activity last = findActivityById(lastId);
            if (last == null) return ResultUtils.error(-1, "Fail: next activity does not exist.");

            // Save the protocol
            String pid = UUID.randomUUID().toString();
            protocol.setPid(pid);

            last = saveLastActivityInfo(aid, pid, last);
            current = saveNextActivityInfo(lastId, pid, current);

            protocolRepository.save(protocol);
            activityRepository.save(last);
            activityRepository.save(current);

            return ResultUtils.success(current);
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult linkActivities(String aid1, String aid2, LinkProtocol protocol) {
        try {
            // Confirm aid
            Activity activity1 = findActivityById(aid1);
            if (activity1 == null) return ResultUtils.error(-1, "Fail: activity does not exist.");
            Activity activity2 = findActivityById(aid2);
            if (activity2 == null) return ResultUtils.error(-1, "Fail: activity does not exist.");

            // Save the protocol
            String pid = protocolRepository.save(protocol).getPid();

            // Save the last activity
            activityRepository.save(saveLastActivityInfo(aid2, pid, activity1));

            // Update the next activity
            activityRepository.save(saveNextActivityInfo(aid1, pid, activity2));

            return ResultUtils.success("Success");
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult separateActivities(String lastAid, String nextAid) {
        try {
            // Confirm aid
            Activity activity1 = findActivityById(lastAid);
            if (activity1 == null) return ResultUtils.error(-1, "Fail: activity does not exist.");
            Activity activity2 = findActivityById(nextAid);
            if (activity2 == null) return ResultUtils.error(-1, "Fail: activity does not exist.");

            // Save the last activity
            JSONArray nextActivities = activity1.getNext();

            IntStream.range(0, nextActivities.size()).forEach(i -> {
                String aid = nextActivities.getJSONObject(i).getString("aid");
                if (aid.equals(nextAid)) {
                    nextActivities.remove(i);
                }
            });
            activity1.setNext(nextActivities);

            // Save the next activity
            JSONArray lastActivities = activity2.getLast();

            IntStream.range(0, lastActivities.size()).forEach(i -> {
                String aid = lastActivities.getJSONObject(i).getString("aid");
                if (aid.equals(lastAid)) {
                    lastActivities.remove(i);
                }
            });
            activity2.setLast(lastActivities);

            activityRepository.save(activity1);
            activityRepository.save(activity2);

            return ResultUtils.success("Success");
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult joinActivity(String aid, String userId) {
        try {
            // confirm
            Activity activity = findActivityById(aid);
            if (activity == null) return ResultUtils.error(-1, "Fail: activity does not exist.");
            User user = findByUserId(userId);
            if (user == null) return ResultUtils.error(-1, "Fail: user does not exist.");

            // add user info to subproject
            // if user exist in subproject?
            JSONArray members = activity.getMembers();
            for (Object member : members) {
                if (member instanceof JSONObject) {
                    if (((JSONObject) member).get("userId").equals(userId)) {
                        return ResultUtils.error(-3, "Fail: member already exists in the subproject");
                    }
                }
            }

            JSONObject userInfo = new JSONObject();
            userInfo.put("userId", user.getUserId());
            userInfo.put("role", "");
            members.add(userInfo);
            activity.setMembers(members);
            activityRepository.save(activity);

            return ResultUtils.success("Success");
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }

    @Override
    public JsonResult quitActivity(String aid, String userId) {
        try {
            Activity activity = findActivityById(aid);
            if (activity == null) return ResultUtils.error(-1, "Fail: activity does not exist.");

            JSONArray members = activity.getMembers();
            for (Object member : members) {
                if (member instanceof JSONObject) {
                    if (((JSONObject) member).get("userId").equals(userId)) {
                        members.remove(member);
                        break;
                    }
                }
            }
            activity.setMembers(members);
            activityRepository.save(activity);

            return ResultUtils.success("Success");
        } catch (Exception ex) {
            return ResultUtils.error(-2, "Fail: Exception");
        }
    }
}
