package cn.edu.njnu.geoproblemsolving.business.tool.chatroom.hydrologicalconcept.support.support;


import cn.edu.njnu.geoproblemsolving.business.tool.chatroom.hydrologicalconcept.support.userimage.UserImage;
import lombok.Data;

import java.util.List;

@Data
public class ProcessStructure {
    String name;
    List<String> elements;
    String description;
    String[] tags;
    List<UserImage> relateImages;
}
