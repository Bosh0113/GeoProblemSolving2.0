package cn.edu.njnu.geoproblemsolving.business.modeltask.modelContainer.service;

import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import org.springframework.web.multipart.MultipartFile;

public interface ModelTaskService {
    public Object uploadGeoData(String userId, String toolId, MultipartFile[] file);

    Object getModelMdlInfo(String mid);
}
