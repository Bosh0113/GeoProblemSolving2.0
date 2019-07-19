package cn.edu.njnu.geoproblemsolving.incomparison.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 21:44 2019/7/8
 * @Modified By:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class ModelResource {
    @Id
    String id;
    String ownerId;
    String ownerName;

//    String[] nodeIds;
    String modelName;
    String desc;
    Date createdTime;
    //todo udxschema
}
