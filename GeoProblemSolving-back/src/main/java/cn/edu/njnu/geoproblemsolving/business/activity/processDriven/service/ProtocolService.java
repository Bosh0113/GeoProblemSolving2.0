package cn.edu.njnu.geoproblemsolving.business.activity.processDriven.service;

import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity.ActivityLinkProtocol;


public interface ProtocolService {
    //更新协议，同时需要更新 graph
    ActivityLinkProtocol putProtocol(String rootId, ActivityLinkProtocol protocol);

    //删除协议
    void delProtocol(String rootId, String protocolId);
}
