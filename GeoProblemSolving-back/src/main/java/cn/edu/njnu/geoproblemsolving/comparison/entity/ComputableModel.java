package cn.edu.njnu.geoproblemsolving.comparison.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 14:55 2019/7/30
 * @Modified By:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class ComputableModel {
    String oid;
    String name;
    String relateModel;
    String ownerName;
    String ownerId;
    String md5;
    String createTime;
    List<String>  recordList;
    // todo MDL 信息

    public ComputableModel(String oid,String name,String ownerName,String ownerId,String md5,String createTime){
        this.oid = oid;
        this.name = name;
        this.ownerName = ownerName;
        this.ownerId = ownerId;
        this.md5 = md5;
        this.createTime = createTime;
    }
}
