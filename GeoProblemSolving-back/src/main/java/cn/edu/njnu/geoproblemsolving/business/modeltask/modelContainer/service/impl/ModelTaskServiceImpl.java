package cn.edu.njnu.geoproblemsolving.business.modeltask.modelContainer.service.impl;

import cn.edu.njnu.geoproblemsolving.business.modeltask.modelContainer.InvokeInfo;
import cn.edu.njnu.geoproblemsolving.business.modeltask.modelContainer.service.ModelTaskService;
import cn.edu.njnu.geoproblemsolving.business.tool.ToolEntity;
import cn.edu.njnu.geoproblemsolving.business.tool.ToolService;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.UUID;

public class ModelTaskServiceImpl implements ModelTaskService {
    @Autowired
    ToolService toolService;
    @Autowired
    RestTemplate restTemplate;

    //第一步，返回 mdl 对应内容渲染工具 invoke 页面


    @Override
    public Object getModelMdlInfo(String mid) {

        return null;
    }

    @Override
    public Object uploadGeoData(String userId, String toolId, MultipartFile[] files) {
        //查询 tool 并取出 toolId
        ToolEntity tool = toolService.getToolById(toolId);
        String toolUrl = tool.getToolUrl();
        //从 url 中获取模型服务容器的: 假设 url 为 http://191.11.12.105:8060/toolId
        int index = toolUrl.lastIndexOf("/");
        String ipAndPort = toolUrl.substring(0, index);
        String uploadUrl = ipAndPort + "/geodata?type=file";
        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<String, Object>();
        InvokeInfo invokeInfo = new InvokeInfo();
        invokeInfo.setInvokeUserId(userId);
        invokeInfo.setInvokeId(UUID.randomUUID().toString());
        HashSet<JSONObject> dataInfoSet = Sets.newHashSet();
        for (MultipartFile file: files){
            multiValueMap.add("myfile", file);
            HttpEntity<Object> httpEntity = new HttpEntity<>(multiValueMap, headers);
            JSONObject uploadResult = restTemplate.exchange(uploadUrl, HttpMethod.POST, httpEntity, JSONObject.class).getBody();
            Integer code = uploadResult.getInteger("code");
            //似乎并不能这样写
            if (code != 0){
                return uploadResult.getString("result");
            }
            //上传成功
            String fileName = file.getOriginalFilename();
            String dataId = uploadResult.getString("data");
            JSONObject dataInfo = new JSONObject();
            dataInfo.put("fileName", fileName);
            dataInfo.put("dataId", dataId);
            dataInfoSet.add(dataInfo);
            multiValueMap.clear();
        }
        invokeInfo.setUploadDataInfo(dataInfoSet);
        return null;
    }
    //数据上传完成，进入 invoke 阶段，读取模型 mdl，然后
}
