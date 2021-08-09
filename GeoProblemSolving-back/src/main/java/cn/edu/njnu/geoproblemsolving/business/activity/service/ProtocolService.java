package cn.edu.njnu.geoproblemsolving.business.activity.service;

import cn.edu.njnu.geoproblemsolving.business.activity.dto.UpdateProtocolDTO;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.LinkProtocol;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import java.util.HashMap;

public interface ProtocolService {

    LinkProtocol setProtocolService(LinkProtocol linkProtocol);

    LinkProtocol findProtocolService(String pid);

    String deleteProtocolService(String pid);

    LinkProtocol updateProtocolService(String pid, HashMap<String, Object> putInfo);

    /**
     * zhngzhng 2021/7/20
     */



}
