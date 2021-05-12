package cn.edu.njnu.geoproblemsolving.business.user.util;

import cn.edu.njnu.geoproblemsolving.business.user.enums.UserTitle;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Decoder;


import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.UUID;

@Component
public class ICommonUtil {

    public Update setUpdate(HttpServletRequest req) {
        // Update update = new Update();
        // Enumeration<String> parameterNames = req.getParameterNames();
        // while (parameterNames.hasMoreElements()) {
        //     String key = parameterNames.nextElement();
        //     String value = req.getParameter(key);
        //     if (key.equals("userState")) {
        //         continue;
        //     }
        //     if (key.equals("userId")) {
        //         StaticParams.paramsMap.add(key, value);
        //         continue;
        //     }
        //     //local user update
        //     update.set(key, value);
        //     //remote userBase update
        //     StaticParams.paramsMap.add(key, value);
        // }
        // return update;
        return null;
    }

    /*
    将所有工具类全部在这个地方来
     */

    public Object gsm2BaseUser(MultiValueMap<String, Object> updateInfo) {
        RestTemplate restTemplate = new RestTemplate();
        String updateUrl = "http://106.14.78.235/ResServer/user/update";
        // String updateUrl  = "http://localhost:8090/ResServer/user/update";
        HttpHeaders headers = new HttpHeaders();
        // headers.add("Authorization", "Bearer " + StaticParams.access_token);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(updateInfo, headers);
        ResponseEntity<String> entity = restTemplate.postForEntity(updateUrl, httpEntity, String.class);
        String body = entity.getBody();
        return body;
    }

    public void sendAuth() {
        RestTemplate restTemplate = new RestTemplate();
        String authUrl = "http://106.14.78.235/AuthServer/oauth/authorize?grant_type=authorization_code&response_type=code&client_id=GSM&scope=all";
        restTemplate.getForObject(authUrl, String.class);
    }


    public Update setUpdate(Map<String, Object> userInfoMap) {
        Update update = new Update();
        for (Map.Entry<String, Object> entry : userInfoMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key.equals("userId") || key.equals("email") || key.equals("createdTime") || key.equals("password")) {
                continue;
            }
            switch (key) {
                case "title":
                    UserTitle title = UserTitle.valueOf((String) value);
                    update.set(key, title);
                    break;
                case "domain":
                case "organizations":
                case "loginIp":
                case "resource":
                    update.set(key, value);
                    break;
                default:
                    update.set(key, (String) value);
            }
        }

        return update;
    }
}
