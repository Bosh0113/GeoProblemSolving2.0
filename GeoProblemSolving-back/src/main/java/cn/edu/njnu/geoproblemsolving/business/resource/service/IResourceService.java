package cn.edu.njnu.geoproblemsolving.business.resource.service;

import cn.edu.njnu.geoproblemsolving.business.resource.entity.AddIResourceDTO;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
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
    Object saveResource(AddIResourceDTO add) throws IOException, URISyntaxException;
}
