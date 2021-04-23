package cn.edu.njnu.geoproblemsolving.business.modeltask.modelContainer.service.impl;

import cn.edu.njnu.geoproblemsolving.business.modeltask.modelContainer.service.ModelTaskService;
import cn.edu.njnu.geoproblemsolving.business.tool.ToolEntity;
import cn.edu.njnu.geoproblemsolving.business.tool.ToolService;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

public class ModelTaskServiceImpl implements ModelTaskService {
    @Autowired
    ToolService toolService;
    @Autowired
    RestTemplate restTemplate;

    @Override
    public JsonResult uploadGeoData(String toolId, MultipartFile[] file) {
        //查询 tool 并取出 toolId
        ToolEntity tool = toolService.getToolById(toolId);
        String toolUrl = tool.getToolUrl();
        //从 url 中获取模型服务容器的: 假设 url 为 http://191.11.12.105:8060/toolId
        int index = toolUrl.lastIndexOf("/");
        String ipAndPort = toolUrl.substring(0, index);
        String uploadUrl = ipAndPort + "/geodata?type=file";
        LinkedMultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<String, Object>();
        multiValueMap.add("myfile", file);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> httpEntity = new HttpEntity<>(multiValueMap, headers);
        ResponseEntity<JSONObject> uploadResult = restTemplate.exchange(uploadUrl, HttpMethod.POST, httpEntity, JSONObject.class);

        return null;
    }
}
