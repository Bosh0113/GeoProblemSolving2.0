package cn.edu.njnu.geoproblemsolving.comparison.entity;

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
    String modelId;
    String ownerId;
    String ownerName;

//    String[] nodeIds;
    String modelName;
    String desc;
    String createdTime;
    //todo udxschema

    public ModelResource(String modelId,String modelName,String ownerId,String ownerName,String createdTime){
        this.modelId=modelId;
        this.modelName=modelName;
        this.ownerId=ownerId;
        this.ownerName=ownerName;
        this.createdTime=createdTime;
    }
}
