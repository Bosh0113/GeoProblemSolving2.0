package cn.edu.njnu.geoproblemsolving.business.resource.service;

import cn.edu.njnu.geoproblemsolving.business.resource.entity.AddIResourceDTO;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.IResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourcePojo;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.mongodb.client.result.UpdateResult;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

public interface IResourceService {
    Object uploadRemote(HttpServletRequest request);

    Object upSomeRemote();

    Object downloadRemote(String uid);

    Object downSomeRemote(ArrayList<String> oids);

    JsonResult deleteRemote(String uid, String rid);

    Object delSomeRemote(String uid, ArrayList<String> oids);

    Object searchRemote(String fileName);

    JsonResult inquiryLocal(Map<String, String> filedAndValue);

    Object saveResource(AddIResourceDTO add) throws IOException, URISyntaxException;

    JsonResult uploadImage(HttpServletRequest request);


    ArrayList<ResourcePojo> getRes(ArrayList<String> rids);

    UpdateResult updateRes(String rid, IResourceEntity resource);
}
