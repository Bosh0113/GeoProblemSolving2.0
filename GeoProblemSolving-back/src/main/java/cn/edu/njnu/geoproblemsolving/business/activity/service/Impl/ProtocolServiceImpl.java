package cn.edu.njnu.geoproblemsolving.business.activity.service.Impl;

import cn.edu.njnu.geoproblemsolving.business.activity.dto.UpdateProtocolDTO;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.ActivityDoc;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.LinkProtocol;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ProtocolRepository;
import cn.edu.njnu.geoproblemsolving.business.activity.service.ProtocolService;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProtocolServiceImpl implements ProtocolService {

    private final ProtocolRepository protocolRepository;

    public ProtocolServiceImpl(ProtocolRepository protocolRepository) {
        this.protocolRepository = protocolRepository;
    }

    @Override
    public JsonResult setProtocol(LinkProtocol linkProtocol){
        try {
            String pid = UUID.randomUUID().toString();
            linkProtocol.setPid(pid);
            return ResultUtils.success(protocolRepository.save(linkProtocol));
        } catch (Exception ex) {
            return ResultUtils.error(-2, ex.toString());
        }
    }

    @Override
    public JsonResult findProtocol(String pid){
        try {
            // confirm
            Optional result = protocolRepository.findById(pid);
            if (!result.isPresent()) return ResultUtils.error(-1, "Fail: link protocol does not exist.");
            LinkProtocol linkProtocol = (LinkProtocol) result.get();

            return ResultUtils.success(linkProtocol);
        } catch (Exception ex) {
            return ResultUtils.error(-2, ex.toString());
        }
    }

    @Override
    public JsonResult deleteProtocol(String pid){
        try {
            // confirm
            Optional result = protocolRepository.findById(pid);
            if (!result.isPresent()) return ResultUtils.error(-1, "Fail: link protocol does not exist.");
            protocolRepository.deleteById(pid);

            return ResultUtils.success("Success");
        } catch (Exception ex) {
            return ResultUtils.error(-2, ex.toString());
        }
    }

    @Override
    public JsonResult updateProtocol(String pid, UpdateProtocolDTO update){
        try {
            // confirm
            Optional result = protocolRepository.findById(pid);
            if (!result.isPresent()) return ResultUtils.error(-1, "Fail: link protocol does not exist.");
            LinkProtocol linkProtocol = (LinkProtocol) result.get();

            update.updateTo(linkProtocol);

            return ResultUtils.success(protocolRepository.save(linkProtocol));
        } catch (Exception ex) {
            return ResultUtils.error(-2, ex.toString());
        }
    }
}
