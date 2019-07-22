package cn.edu.njnu.geoproblemsolving.comparison.dao.modelresource;

import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;

import java.util.List;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 20:02 2019/7/19
 * @Modified By:
 **/
public interface IModelResourceDao {
    List<String> createModelResByNameList(List<String> nameList, UserEntity user);
}
