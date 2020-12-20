package cn.edu.njnu.geoproblemsolving.business.datacontainer;


import cn.edu.njnu.geoproblemsolving.common.enums.ResultEnum;
import cn.edu.njnu.geoproblemsolving.common.exception.MyException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

/**
 * @ClassName dataItemService
 * @Description
 * @Author Zhiyi
 * @Date 2019/12/16  22:06
 * @Version 1.0.0
 */
@Service
public class DataIContainerService {
    @Value("${dataContainer}")
    private String dataContainer;

    public JSONObject addDataItem(File file){
        FileSystemResource resource = new FileSystemResource(file);      //临时文件
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("file", resource);
        RestTemplate restTemplate = new RestTemplate();
        String urlStr = "http://" + dataContainer  + ":8081" + "/dataitem/addByStorage" ; ////Step0:根据MD5获取可用的任务服务器

        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity(urlStr,form, JSONObject.class);//虚拟http请求
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.ERROR);
        }
        JSONObject result = jsonObjectResponseEntity.getBody().getJSONObject("data");
        return result;
    }

    public ResponseEntity<byte[]> download(String id) {
        RestTemplate restTemplate = new RestTemplate();
//        String urlStr = "http://" + dataContainer + ":8082/data?uid=" + id;
        String urlStr = "http://" + dataContainer + ":8082/data/" + id;
        ResponseEntity<byte []> response = restTemplate.exchange(urlStr, HttpMethod.GET,
                null, byte[].class);
        return  response;
    }

    public String upload(File file, String userId, String userName) {
        FileSystemResource resource = new FileSystemResource(file);      //临时文件
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("serverNode", "china");
        form.add("userId", userId);
        form.add("datafile", resource);
        form.add("name", userName);
        form.add("origination", "GeoProblemSolving_3r");

        String urlStr = "http://" + dataContainer + ":8082/data";
//        String urlStr = "http://" + dataContainer + ":8082/dataNoneConfig";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity(urlStr, form, JSONObject.class);
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.ERROR);
        }
        JSONObject result = jsonObjectResponseEntity.getBody();//获得上传数据的URL
//        String urlResult = result.getJSONObject("data").getString("source_store_id");
        String urlResult = result.getJSONObject("data").getString("id");
        return urlResult;
    }
}
