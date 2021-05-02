package cn.edu.njnu.geoproblemsolving.business.resource.util;

import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class RestTemplateUtil {
    // @Autowired
    // RestTemplate restTemplate;
    RestTemplate restTemplate = new RestTemplate();

    /**
     * 文件下载请求
     *
     * @param downRemoteUrl
     * @return
     */
    public ResponseEntity<byte[]> getDownRemoteResult(String downRemoteUrl) {
        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(downRemoteUrl, HttpMethod.GET, entity, byte[].class);
        return response;
    }

    public ResponseEntity getDelRemoteResult(String delRemoteUrl) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(delRemoteUrl, HttpMethod.DELETE, httpEntity, String.class);
        return response;
    }

    /**
     * 将数据上传到数据容器
     *
     * @param uploadRemoteUrl
     * @param paramMap
     * @return
     */
    public JSONObject uploadRemote(String uploadRemoteUrl, LinkedMultiValueMap<String, Object> paramMap) {
        JSONObject uploadResult = restTemplate.postForObject(uploadRemoteUrl, paramMap, JSONObject.class);
        return uploadResult;
    }

    //需要测试
    // public Object setUserBase(String userServerUrl,LinkedMultiValueMap<String, Object> paramMap){
    //     ResponseEntity response =  restTemplate.postForObject(userServerUrl, paramMap, ResponseEntity.class);
    //     return response;
    // }
    /*
    ==========================================用户服务器资源相关操作========================================================
     */

    // /**
    //  * 上传资源
    //  * @param userServerUrl
    //  * @param userBaseJson
    //  * @return
    //  */
    public JSONObject setUserBase(String userServerUrl, JSONObject userBaseJson) {
        HttpHeaders headers = new HttpHeaders();
        // headers.add("Authorization", "Bearer " + StaticParams.access_token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(userBaseJson, headers);
        ResponseEntity<String> response = restTemplate.exchange(userServerUrl, HttpMethod.POST, httpEntity, String.class);
        String resultStr = response.getBody();
        JSONObject uploadToUserServer = JSONObject.parseObject(resultStr);
        return uploadToUserServer;
    }
    //
    public JSONObject delUserBaseResource(String delUserBaseResUrl){
        HttpHeaders headers = new HttpHeaders();
        // headers.add("Authorization", "Bearer " + StaticParams.access_token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(delUserBaseResUrl, HttpMethod.DELETE, httpEntity, String.class);
        String resultStr = response.getBody();
        return JSONObject.parseObject(resultStr);
    }

    public Object sendReqToAuthServer(String url,HttpMethod httpMethod){
        HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, String.class);
            String resultStr = responseEntity.getBody();
            return resultStr;
        }catch (Exception e){
            return "Fail";
        }

    }



    /*
    ====================================================================================================================
     */

    public ResponseEntity<JSONObject> getRequestToServer(String url, String access_token){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, JSONObject.class);
    }

    //post
    public ResponseEntity<JSONObject> postRequestToServer(String url, String access_token, JSONObject payload){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);
        headers.add("Content-Type", "application/json;charset=utf-8");
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(payload, headers);
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, JSONObject.class);
    }

    //post form-data 形式数据
    public ResponseEntity<JSONObject> postRequestMap(String url, LinkedMultiValueMap<String, Object> payload){
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<LinkedMultiValueMap<String, Object>> reqInfo = new HttpEntity<>(payload, headers);
        return restTemplate.exchange(url, HttpMethod.POST, reqInfo, JSONObject.class);
    }

    public ResponseEntity<JSONObject> putRequestToServer(String url, String access_token, JSONObject payload){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(payload, headers);
        return restTemplate.exchange(url, HttpMethod.PUT, httpEntity, JSONObject.class);
    }

    public ResponseEntity<JSONObject> deleteRequestToServer(String url, String access_token){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);
        HttpEntity<LinkedMultiValueMap<String, Object>> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, JSONObject.class);
    }

    public JSONObject delRequest(String url){
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, JSONObject.class).getBody();
    }


    /*
    Model Container http request
     */
}
