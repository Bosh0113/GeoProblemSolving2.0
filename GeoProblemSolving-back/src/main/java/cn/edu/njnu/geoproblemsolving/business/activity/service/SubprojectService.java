package cn.edu.njnu.geoproblemsolving.business.activity.service;


import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Subproject;


public interface SubprojectService{
    public JsonResult createSubproject(Subproject subproject);

    public JsonResult inquirySubproject(String aid);

    public JsonResult updateSubproject(Subproject subproject);

    public JsonResult deleteSubproject(String aid);

    public JsonResult findParticipants(String aid);

    public JsonResult joinSubproject(String aid, String userId);

    public JsonResult updateMemberRole(String aid, String userId, String role);

    public JsonResult quitSubproject(String aid, String userId);
}
