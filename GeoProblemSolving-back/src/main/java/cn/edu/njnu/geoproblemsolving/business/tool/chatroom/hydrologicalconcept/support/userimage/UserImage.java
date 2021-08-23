package cn.edu.njnu.geoproblemsolving.business.tool.chatroom.hydrologicalconcept.support.userimage;

import lombok.Data;

@Data
public class UserImage {
    String geoId;
    String conceptId;
    String name;
    String description;
    String pathUrl;
    String[] tags;
}
