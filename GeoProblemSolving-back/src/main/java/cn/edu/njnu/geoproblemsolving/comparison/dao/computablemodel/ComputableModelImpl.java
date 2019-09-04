package cn.edu.njnu.geoproblemsolving.comparison.dao.computablemodel;

import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpProject;
import cn.edu.njnu.geoproblemsolving.comparison.entity.ComputableModel;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 19:59 2019/7/31
 * @Modified By:
 **/
public class ComputableModelImpl implements IComputableModelDao {

    private final MongoTemplate mongoTemplate;

    public ComputableModelImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ComputableModel addComputableModel(ComputableModel computableModel) {
        mongoTemplate.save(computableModel);
        return computableModel;
    }

    @Override
    public String deleteComputableModel(ComputableModel computableModel) {
        return null;
    }

    @Override
    public String updateComputableModel(ComputableModel computableModel) {
        return null;
    }

    @Override
    public List<ComputableModel> getAllComputableModel() {
        return null;
    }

    @Override
    public ComputableModel findItemById(String id) {
        return null;
    }

    @Override
    public ComputableModel findItemByOid(String oid) {
        return null;
    }

    @Override
    public ComputableModel findItemByMd5(String md5) {
        Query query = Query.query(Criteria.where("md5").is(md5));
        ComputableModel computableModel = mongoTemplate.findOne(query, ComputableModel.class);
        return computableModel;
    }

    @Override
    public List<ComputableModel> getItems(String key, String value) {
        return null;
    }
}
