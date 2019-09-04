package cn.edu.njnu.geoproblemsolving.comparison.dao.modelresource;

import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;
import cn.edu.njnu.geoproblemsolving.comparison.entity.ModelResource;

import java.util.List;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 20:02 2019/7/19
 * @Modified By:
 **/
public interface IModelResourceDao {

    ModelResource findModelByOid(String oid);

    ModelResource createModel(ModelResource mr);

    List<ModelResource> findModelByIdList(List<String> idList);

    List<String> createModelResByNameList(List<String> nameList, UserEntity user);
}
