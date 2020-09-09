package cn.edu.njnu.geoproblemsolving.business.activity.service;

import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Activity;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.LinkProtocol;

public interface ActivityService {

    public JsonResult findActivity(String aid);

    public JsonResult createActivity(Activity activity);

    public JsonResult updateActivity(Activity activity);

    public JsonResult deleteActivity(String aid);

    public JsonResult findChildren(String aid);

    public JsonResult findLast(String aid);

    public JsonResult findNext(String aid);

    public JsonResult createNext(String aid, String nextId, LinkProtocol protocol);

    public JsonResult createLast(String aid, String lastId, LinkProtocol protocol);

    public JsonResult linkActivities(String aid1, String aid2, LinkProtocol protocol);

    public JsonResult separateActivities(String aid1, String aid2);

    public JsonResult joinActivity(String aid, String userId);

    public JsonResult quitActivity(String aid, String userId);
}
