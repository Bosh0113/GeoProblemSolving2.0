package cn.edu.njnu.geoproblemsolving.domain.hydrologicalconcept.support.support;

import cn.edu.njnu.geoproblemsolving.domain.hydrologicalconcept.support.userimage.UserImage;
import lombok.Data;

import java.util.List;

@Data
public class PropertyStructure {
    String type;
    String description;
    String[] tags;
    List<UserImage> relateImages;
}
