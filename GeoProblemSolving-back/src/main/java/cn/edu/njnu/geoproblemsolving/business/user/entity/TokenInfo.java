package cn.edu.njnu.geoproblemsolving.business.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName TokenInfo
 * @Description 将获取到的 token 入库
 * @Author zhngzhng
 * @Date 2021/4/1
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfo {
    String access_token;
    String refreshToken;
    // 过期时间
    int expire;
    Date expiryTime;
}
