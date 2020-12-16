package cn.edu.njnu.geoproblemsolving.business.resource.service;

import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

public interface IResourceService {
    Object uploadRemote(HttpServletRequest request);
    Object upSomeRemote();
    Object downloadRemote(String uid);
    Object downSomeRemote(ArrayList<String> oids);
    Object deleteRemote(String uid);
    Object delSomeRemote(ArrayList<String> oids);
    Object searchRemote(String fileName);
    JsonResult inquiryLocal(Map<String, String> filedAndValue);
}
