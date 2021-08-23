package cn.edu.njnu.geoproblemsolving.business.activity.service;

import cn.edu.njnu.geoproblemsolving.business.activity.dto.UpdateProtocolDTO;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.LinkProtocol;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;

public interface ProtocolService {

    JsonResult setProtocol(LinkProtocol linkProtocol);

    JsonResult findProtocol(String pid);

    JsonResult deleteProtocol(String pid);

    JsonResult updateProtocol(String pid, UpdateProtocolDTO update);
}
