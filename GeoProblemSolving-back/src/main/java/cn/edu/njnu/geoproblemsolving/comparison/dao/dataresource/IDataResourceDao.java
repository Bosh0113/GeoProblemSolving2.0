package cn.edu.njnu.geoproblemsolving.comparison.dao.dataresource;

import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;

import java.util.List;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 16:41 2019/7/19
 * @Modified By:
 **/
public interface IDataResourceDao {
    List<String> createDataResByNameList(List<String> nameList, UserEntity user);
}
