package cn.edu.njnu.geoproblemsolving.comparison.dao.base_cmp_info;

import cn.edu.njnu.geoproblemsolving.comparison.entity.BaseCmpInfo;
import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpProject;

import java.util.List;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 20:43 2019/8/5
 * @Modified By:
 **/
public interface IBaseCmpInfoDao {
    CmpProject addBaseCmpInfo(BaseCmpInfo info);

    String deleteBaseCmpInfo(BaseCmpInfo info);

    String updateBaseCmpInfo(BaseCmpInfo info);

    List<BaseCmpInfo> getAllBaseCmpInfo();

    BaseCmpInfo findBaseCmpInfoById(String id);

    List<BaseCmpInfo> getBaseCmpInfos(String key, String value);
}
