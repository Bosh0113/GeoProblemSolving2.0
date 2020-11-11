package cn.edu.njnu.geoproblemsolving.business.resource.service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public interface IResourceService {
    Object uploadRemote(HttpServletRequest request);
    Object upSomeRemote();
    Object downloadRemote(String uid);
    Object downSomeRemote(ArrayList<String> oids);
    Object deleteRemote(String uid);
    Object delSomeRemote(ArrayList<String> oids);
    Object searchRemote(String fileName);
}
