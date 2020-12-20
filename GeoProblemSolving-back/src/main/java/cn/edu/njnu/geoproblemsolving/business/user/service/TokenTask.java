package cn.edu.njnu.geoproblemsolving.business.user.service;

import cn.edu.njnu.geoproblemsolving.business.user.StaticParams;
import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TokenTask {
    @Autowired
    RestTemplate restTemplate;
    @Value("${client_id}")
    String client_id;
    @Value("${client_secret}")
    String client_secret;
    @Value("${authServerIp}")
    String authIp;
    @Value("${resServerIp}")
    String resIp;

    /**
     * oauth2 密码模式获取token
     * @param email
     * @param pwd
     * @return
     */
    public JSONObject getTokenUsePwd(String email, String pwd){
        LinkedMultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("client_id", client_id);
        paramMap.add("client_secret", client_secret);
        paramMap.add("username", email);
        paramMap.add("password", pwd);
        paramMap.add("scope", "all");
        paramMap.add("grant_type", "password");
        String authUri = "http://"+ authIp + "/AuthServer/oauth/token";
        JSONObject tokenJson = restTemplate.postForObject(authUri, paramMap, JSONObject.class);
        return tokenJson;
    }

    /**
     * 获得用户信息
     * @param access_token
     * @return
     */
    public JSONObject getUserFromResServer(String access_token){
        HttpHeaders headers = new HttpHeaders();
        StaticParams.access_token = access_token;
        headers.add("Authorization", "Bearer "+ access_token);
        //httpEntity = httpHeader + httpBody,当然也可以只有其中一部分
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        String resUserUri = "http://" + resIp + "/ResServer/user/getInfo";
        //Url, RequestType, RequestContent, ResponseDataType
        ResponseEntity<JSONObject> userJson = restTemplate.exchange(resUserUri, HttpMethod.GET, httpEntity, JSONObject.class);
        return userJson.getBody();
    }


    /**
     * 授权码模式(废弃)---code换token
     * @param code
     * @return
     */
    // public Object getData(String code){
    //     //第一次授权
    //     if ("".equals(StaticParams.access_token) && code != null){
    //         LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
    //         map.add("code", code);
    //         map.add("client_id", "zhengzhong");
    //         map.add("client_secret", "zz");
    //         map.add("redirect_uri", redirect_uri);
    //         map.add("grant_type", "authorization_code");
    //         String authUrl = "http://" + authIp +"/AuthServer/oauth/token";
    //         Map<String, String> resp = restTemplate.postForObject(authUrl, map, Map.class);
    //         StaticParams.access_token = resp.get("access_token");
    //         StaticParams.refresh_token = resp.get("refresh_token");
    //     }
    //     // return loadDataFromResServer();
    //     return null;
    // }
    //
    // public JSONObject loadDataFromResServer(String access_token){
    //     try {
    //         RestTemplate restTemplate = new RestTemplate();
    //         HttpHeaders headers = new HttpHeaders();
    //         headers.add("Authorization","Bearer " + access_token);
    //         HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
    //         String resUrl = "http://106.14.78.235/ResServer/user/getInfo";
    //         ResponseEntity<JSONObject> entity = restTemplate.exchange(resUrl, HttpMethod.GET, httpEntity, JSONObject.class);
    //         return entity.getBody();
    //     }catch (RestClientException e){
    //         // return "load failed";
    //         throw e;
    //     }
    // }

    // @Scheduled(cron = "0 55 0/1 * * ?")
    // public void tokenTask(){
    //     if (!StaticParams.refresh_token.equals("")){
    //         LinkedMultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
    //         map.add("client_id", "zhengzhong");
    //         map.add("client_secret", "zz");
    //         map.add("refresh_token", StaticParams.refresh_token);
    //         map.add("grant_type", "refresh_token");
    //         String authUrl = "http://"+ authIp + "/oauth/token";
    //         Map<String, String> resp =  restTemplate.postForObject(authUrl, map, Map.class);
    //         StaticParams.access_token = resp.get("access_token");
    //         StaticParams.refresh_token = resp.get("refresh_token");
    //     }
    // }


}
