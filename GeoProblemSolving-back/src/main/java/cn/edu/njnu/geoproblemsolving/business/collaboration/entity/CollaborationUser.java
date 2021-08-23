package cn.edu.njnu.geoproblemsolving.business.collaboration.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.websocket.Session;

@Data
public class CollaborationUser {

    @Id
    private String userId;  //UUID
    private String name;
    private String email;   //used for registration
    private String avatar;

    /**
     * for communication
     */
    private Session session;

    public JSONObject getUserInfo() {
        JSONObject user = new JSONObject();
        user.put("userId", userId);
        user.put("name", name);
        user.put("email", email);
        user.put("avatar", avatar);

        return user;
    }
}
