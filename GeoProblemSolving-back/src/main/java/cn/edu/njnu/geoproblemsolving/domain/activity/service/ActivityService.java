package cn.edu.njnu.geoproblemsolving.domain.activity.service;

import cn.edu.njnu.geoproblemsolving.domain.activity.Activity;
import cn.edu.njnu.geoproblemsolving.domain.activity.LinkProtocol;

import java.util.List;

public interface ActivityService {

    public Object createActivity(Activity activity);

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
