package cn.edu.njnu.geoproblemsolving.comparison.entity;

import cn.edu.njnu.geoproblemsolving.comparison.enums.ExtentsType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

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
    String oid;
    String ownerId;
    String ownerName;
    String privacy;
    String modelName;
    String description;
    String createTime;
    String projectId;

    List<String> computableModels;
    List<String> conceptualModels;
    List<String> logicalModels;

    // 模型元数据：
    // mechanism
    // methods
    int spatialDimension;
    String scale;
    String spatialReference;//wkt
    ResolutionConstraint  resolutionConstraint;
    SpatialExtents spatialExtents;
    GeoVariable timeStep;
    String startTime;
    String endTime;
    List<String> programmingLang;




    public ModelResource(String oid,String modelName,String ownerId,String ownerName,String createTime){
        this.oid=oid;
        this.modelName=modelName;
        this.ownerId=ownerId;
        this.ownerName=ownerName;
        this.createTime=createTime;
    }


}

class SpatialExtents{
    ExtentsType type;
    String value;
    double[] envelopeValue; // xmin,xmax,ymin,ymax
    List<GeoPoint> polygon;

}


class ResolutionConstraint{
    GeoVariable x;
    GeoVariable y;
    GeoVariable z;
}

class GeoVariable {
    String value;
    String unit;
}

class GeoPoint{
    double x;
    double y;
}
