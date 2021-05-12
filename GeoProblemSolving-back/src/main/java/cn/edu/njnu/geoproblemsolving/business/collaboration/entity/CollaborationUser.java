package cn.edu.njnu.geoproblemsolving.business.collaboration.entity;

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
}
