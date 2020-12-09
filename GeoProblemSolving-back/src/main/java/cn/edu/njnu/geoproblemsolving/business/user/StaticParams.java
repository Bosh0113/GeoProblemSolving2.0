package cn.edu.njnu.geoproblemsolving.business.user;

import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import cn.edu.njnu.geoproblemsolving.business.user.entity.UserDto;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class StaticParams {
    public static String access_token = "";
    public static String refresh_token = "";
    public static User loginUser;
    public static String referPageUrl = "";
    public static MultiValueMap<String, Object> paramsMap = new LinkedMultiValueMap<>();
}
