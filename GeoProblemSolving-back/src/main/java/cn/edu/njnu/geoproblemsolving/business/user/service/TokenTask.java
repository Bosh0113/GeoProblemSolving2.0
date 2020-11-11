package cn.edu.njnu.geoproblemsolving.business.user.service;

import cn.edu.njnu.geoproblemsolving.business.StaticParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${authServerIpAndPort}")
    String authIp;
    @Value("${resServerIpAndPort}")
    String resIp;
    @Value("${redirect_uri}")
    String redirect_uri;

    public Object getData(String code){
        //第一次授权
        if ("".equals(StaticParams.access_token) && code != null){
            LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("code", code);
            map.add("client_id", "GSM");
            map.add("client_secret", "mzy");
            map.add("redirect_uri", redirect_uri);
            map.add("grant_type", "authorization_code");
            String authUrl = "http://" + authIp +"/AuthServer/oauth/token";
            Map<String, String> resp = restTemplate.postForObject(authUrl, map, Map.class);
            StaticParams.access_token = resp.get("access_token");
            StaticParams.refresh_token = resp.get("refresh_token");
        }
        return loadDataFromResServer();
    }

    public Object loadDataFromResServer(){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization","Bearer " + StaticParams.access_token);
            HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
            String resUrl = "http://" + resIp + "/ResServer/user/getInfo";
            ResponseEntity<String> entity = restTemplate.exchange(resUrl, HttpMethod.GET, httpEntity, String.class);
            return entity.getBody();
        }catch (RestClientException e){
            return "load failed";
        }
    }

    @Scheduled(cron = "0 55 0/1 * * ?")
    public void tokenTask(){
        if (!StaticParams.refresh_token.equals("")){
            LinkedMultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
            map.add("client_id", "GSM");
            map.add("client_secret", "mzy");
            map.add("refresh_token", StaticParams.refresh_token);
            map.add("grant_type", "refresh_token");
            String authUrl = "http://"+ authIp + "/oauth/token";
            Map<String, String> resp =  restTemplate.postForObject(authUrl, map, Map.class);
            StaticParams.access_token = resp.get("access_token");
            StaticParams.refresh_token = resp.get("refresh_token");
        }
    }

}
