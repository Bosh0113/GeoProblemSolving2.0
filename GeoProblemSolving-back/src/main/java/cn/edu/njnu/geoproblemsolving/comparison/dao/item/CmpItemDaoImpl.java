package cn.edu.njnu.geoproblemsolving.comparison.dao.item;

import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;
import cn.edu.njnu.geoproblemsolving.comparison.dao.dataresource.DataResourceDaoImpl;
import cn.edu.njnu.geoproblemsolving.comparison.dao.modelresource.ModelResourceDaoImpl;
import cn.edu.njnu.geoproblemsolving.comparison.entity.BaseCmpInfo;
import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpItem;
import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpProject;
import cn.edu.njnu.geoproblemsolving.comparison.utils.DaoUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 20:04 2019/7/25
 * @Modified By:
 **/
public class CmpItemDaoImpl implements ICmpItemDao {

    private final MongoTemplate mongoTemplate;

    public CmpItemDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public CmpItem addCmpItem(CmpItem item) {
        CmpItem cmpItem = (CmpItem) DaoUtils.createCmpInfo(item,item.getProjectId(), mongoTemplate);
        // itemId
        String itemId = UUID.randomUUID().toString();
        cmpItem.setItemId(itemId);
        mongoTemplate.save(cmpItem);
        return cmpItem;
    }

    @Override
    public String deleteCmpItem(CmpItem item) {
        return null;
    }

    @Override
    public String updateCmpItem(CmpItem item) {
        return null;
    }

    @Override
    public List<CmpItem> getAllItem() {
        return null;
    }

    @Override
    public CmpItem findItemById(String id) {
        return null;
    }

    @Override
    public CmpItem findItemByItemId(String itemId) {
        Query query = Query.query(Criteria.where("itemId").is(itemId));
        CmpItem cmpItem = mongoTemplate.findOne(query, CmpItem.class);
        return cmpItem;
    }

    @Override
    public List<CmpItem> findItemByItemIdList(List<String> idList) {
        Query query = Query.query(Criteria.where("itemId").in(idList));
        List<CmpItem> cmpItems = mongoTemplate.find(query, CmpItem.class);
        return cmpItems;
    }

    @Override
    public List<CmpItem> getItems(String key, String value) {
        Query query = Query.query(Criteria.where(key).is(value));
        if (mongoTemplate.find(query, CmpItem.class).isEmpty()) {
            return null;
        } else {
            List<CmpItem> cmpItems = mongoTemplate.find(query, CmpItem.class);
            return cmpItems;
        }
    }
}
