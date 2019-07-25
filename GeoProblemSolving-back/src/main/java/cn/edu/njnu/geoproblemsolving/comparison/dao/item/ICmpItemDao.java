package cn.edu.njnu.geoproblemsolving.comparison.dao.item;

import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpItem;

import java.util.List;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 19:58 2019/7/25
 * @Modified By:
 **/
public interface ICmpItemDao {
    CmpItem addCmpItem(CmpItem item);

    String deleteCmpItem(CmpItem item);

    String updateCmpItem(CmpItem item);

    List<CmpItem> getAllItem();

    CmpItem findItemById(String id);

    CmpItem findItemByItemId(String itemId);

    List<CmpItem> getItems(String key, String value);
}
