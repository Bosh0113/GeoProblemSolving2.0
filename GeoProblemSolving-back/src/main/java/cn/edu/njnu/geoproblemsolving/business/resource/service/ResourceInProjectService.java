package cn.edu.njnu.geoproblemsolving.business.resource.service;

import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public interface ResourceInProjectService {
    Object createFolder(String folderName, String aid, ArrayList<String> paths, String userId);

    Object uploadFileInProject(HttpServletRequest req);

    Object delResource(String aid, ArrayList<String> uids, ArrayList<String> paths);

    Object putResourceByPath(String aid, ResourceEntity putRes, ArrayList<String> paths);

    ArrayList<ResourceEntity> getAllRes(String aid, ArrayList<String> paths);

    List<ResourceEntity> resourceToProject(String userId, String aid, String uids, ArrayList<String> paths);


}
