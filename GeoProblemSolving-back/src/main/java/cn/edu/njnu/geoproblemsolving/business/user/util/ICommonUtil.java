package cn.edu.njnu.geoproblemsolving.business.user.util;

import cn.edu.njnu.geoproblemsolving.business.StaticParams;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Component
public class ICommonUtil {

    public Update setUpdate(HttpServletRequest req){
        Update update = new Update();
        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()){
            String key = parameterNames.nextElement();
            String value = req.getParameter(key);
            if (key.equals("userState")){
                continue;
            }
            //local user update
            update.set(key, value);
            //remote userBase update
            StaticParams.paramsMap.add(key, value);
        }
        return update;
    }
    public Object gsm2BaseUser(MultiValueMap<String, Object> updateInfo){
        RestTemplate restTemplate = new RestTemplate();
        String updateUrl  = "http://106.14.78.235/ResServer/user/update";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer " + StaticParams.access_token);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(updateInfo, headers);
        ResponseEntity<String> entity = restTemplate.postForEntity(updateUrl, httpEntity, String.class);
        String body = entity.getBody();
        return body;
    }
    public void sendAuth(){
        RestTemplate restTemplate = new RestTemplate();
        String authUrl = "http://106.14.78.235/AuthServer/oauth/authorize?grant_type=authorization_code&response_type=code&client_id=GSM&scope=all";
        restTemplate.getForObject(authUrl, String.class);
    }
}
