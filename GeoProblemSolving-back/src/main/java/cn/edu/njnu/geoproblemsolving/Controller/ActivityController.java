package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Entity.Activities.Activity;
import cn.edu.njnu.geoproblemsolving.Enums.ProjectPrivacy;
import cn.edu.njnu.geoproblemsolving.Entity.Activities.LinkProtocol;
import cn.edu.njnu.geoproblemsolving.Service.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping("/activity")
public class ActivityController {

    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

//    @RequestMapping(method = RequestMethod.GET, value = "/level/{activityLevel}")
//    public List<Activity> getActivitiesByLevel(@PathVariable("activityLevel") Integer level){
//        logger.info("getActivitiesByLevel");
//        return activityService.findByLevel(level);
//    }
//
//    @RequestMapping(method = RequestMethod.GET, value = "/privacy/{activityPrivacy}")
//    public List<Activity> getActivitiesByPrivacy(@PathVariable("activityPrivacy") ProjectPrivacy privacy){
//        logger.info("getActivitiesByPrivacy");
//        return activityService.findByPrivacy(privacy);
//    }

    @RequestMapping(method = RequestMethod.GET, value = "/{aid}/children")
    public List<Activity> getChildren(@PathVariable("aid") String aid){
        logger.info("getChildren");
        return activityService.findChildren(aid);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{aid}/children")
    public Activity createChildActivity(@PathVariable("aid") String aid, @RequestBody Activity activityInfo){
        logger.info("createChildActivity");
        return activityService.createChild(aid, activityInfo);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{aid}/next")
    public List<Activity> getNextActivities(@PathVariable("aid") String aid){
        logger.info("getNextActivities");
        return activityService.findNext(aid);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{aid}/next")
    public Activity createNextActivity(@PathVariable("aid") String aid, @RequestBody Map<String, Object> activityInfo){
        logger.info("createNextActivity");
        try {
            return activityService.createNext(aid, (Activity) activityInfo.get("activity"), (LinkProtocol) activityInfo.get("protocol"));
        }
        catch (Exception ex){
            return null;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{aid}/last")
    public List<Activity> getLastActivities(@PathVariable("aid") String aid){
        logger.info("getLastActivities");
        return activityService.findLast(aid);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{aid}/last")
    public Activity createLastActivity(@PathVariable("aid") String aid, @RequestBody Map<String, Object> activityInfo){
        logger.info("createLastActivity");
        try {
            return activityService.createLast(aid, (Activity) activityInfo.get("activity"), (LinkProtocol) activityInfo.get("protocol"));
        }
        catch (Exception ex){
            return null;
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/link/{aid1}/{aid2}")
    public String linkActivities(@PathVariable("aid1") String aid1, @PathVariable("aid2") String aid2, @RequestBody LinkProtocol protocol){
        logger.info("linkActivities");
        return activityService.linkActivities(aid1, aid2, protocol);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/separate/{aid1}/{aid2}")
    public String  separateActivities(@PathVariable("aid1") String aid1, @PathVariable("aid2") String aid2){
        logger.info("separateActivities");
        return activityService.separateActivities(aid1, aid2);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/join")
    public String joinActivity(@RequestParam("aid") String aid, @RequestParam("userId") String userId){
        logger.info("joinActivity");
        return activityService.joinActivity(aid, userId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/quit")
    public String quitActivity(@RequestParam("aid") String aid, @RequestParam("userId") String userId){
        logger.info("quitActivity");
        return activityService.quitActivity(aid, userId);
    }
}
