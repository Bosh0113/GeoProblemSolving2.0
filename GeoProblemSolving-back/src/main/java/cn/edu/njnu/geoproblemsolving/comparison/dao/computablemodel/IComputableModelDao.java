package cn.edu.njnu.geoproblemsolving.comparison.dao.computablemodel;

import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpItem;
import cn.edu.njnu.geoproblemsolving.comparison.entity.ComputableModel;
import cn.edu.njnu.geoproblemsolving.comparison.entity.ModelState;

import java.util.List;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 19:57 2019/7/31
 * @Modified By:
 **/
public interface IComputableModelDao {
    ComputableModel addComputableModel(ComputableModel computableModel);

    String deleteComputableModel(ComputableModel computableModel);

    String updateComputableModel(ComputableModel computableModel);

    List<ComputableModel> getAllComputableModel();

    ComputableModel findItemById(String id);

    ComputableModel findItemByOid(String oid);

    ComputableModel findItemByMd5(String md5);

    ComputableModel updateStates(String oid, List<ModelState> states);

    List<ComputableModel> getItems(String key, String value);
}
