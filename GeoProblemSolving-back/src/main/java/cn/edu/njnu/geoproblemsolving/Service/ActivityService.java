package cn.edu.njnu.geoproblemsolving.Service;

import cn.edu.njnu.geoproblemsolving.Entity.Activities.Activity;
import cn.edu.njnu.geoproblemsolving.Entity.Activities.Enums.ActivityPrivacy;
import cn.edu.njnu.geoproblemsolving.Entity.Activities.LinkProtocol;

import java.util.List;

public interface ActivityService {

    public List<Activity> findByLevel(Integer level);

    public List<Activity> findByPrivacy(ActivityPrivacy privacy);

    public List<Activity> findChildren(String aid);

    public List<Activity> findLast(String aid);

    public List<Activity> findNext(String aid);

    public Activity createChild(String aid, Activity activity);

    public Activity createNext(String aid, Activity activity, LinkProtocol protocol);

    public Activity createLast(String aid, Activity activity, LinkProtocol protocol);

    public String linkActivities(String aid1, String aid2, LinkProtocol protocol);

    public String separateActivities(String aid1, String aid2);

    public String joinActivity(String aid, String userId);

    public String quitActivity(String aid, String userId);
}
