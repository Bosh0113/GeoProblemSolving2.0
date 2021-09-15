package cn.edu.njnu.geoproblemsolving.business.activity.processDriven.service.impl;

import cn.edu.njnu.geoproblemsolving.business.CommonUtil;
import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity.ActivityLinkProtocol;
import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.service.GeoAnalysisProcess;
import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.service.ProtocolService;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ActivityLinkProtocolRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @ClassName ProtocolServiceImpl
 * @Description
 * @Author zhngzhng
 * @Date 2021/9/1
 **/
@Service
public class LinkProtocolServiceImpl implements ProtocolService {
    @Autowired
    ActivityLinkProtocolRepository protocolRepository;

    @Autowired
    GeoAnalysisProcess geoAnalysisProcess;

    @Override
    public ActivityLinkProtocol putProtocol(String rootId, ActivityLinkProtocol protocol) {
        Optional<ActivityLinkProtocol> byId = protocolRepository.findById(protocol.getId());
        if (byId.get() == null){
            return null;
        }
        ActivityLinkProtocol preProtocol = byId.get();
        String[] nullPropertyNames = CommonUtil.getNullPropertyNames(protocol);
        //完成协议的更新
        BeanUtils.copyProperties(protocol, preProtocol, nullPropertyNames);

        //开始更新图
        geoAnalysisProcess.updateGraphByProtocol(rootId, protocol);
        return protocolRepository.save(preProtocol);
    }

    @Override
    public void delProtocol(String rootId, String protocolId) {
        //更新图
        geoAnalysisProcess.delGraphByProtocol(rootId, protocolId);
        protocolRepository.deleteById(protocolId);
    }
}
