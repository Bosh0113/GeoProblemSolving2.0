package cn.edu.njnu.geoproblemsolving.Entity.ModelTools.ModelItem;


import cn.edu.njnu.geoproblemsolving.Entity.ModelTools.CModel.support.BaseEntity;
import cn.edu.njnu.geoproblemsolving.Entity.ModelTools.ModelItem.Support.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @ClassName ModelItem
 * @Description todo
 * @Author sun_liber
 * @Date 2019/7/5
 * @Version 1.0.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "CModel")
public class ModelItemEntity extends BaseEntity {
    @Id
    private String id;
    private String name;
    private String des;
    private String md5;//ComputableModel-md5
    private String oid;//ComputableModel-oid
    private List<State> stateList;
}
