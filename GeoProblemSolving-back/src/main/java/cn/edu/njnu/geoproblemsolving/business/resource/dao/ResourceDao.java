package cn.edu.njnu.geoproblemsolving.business.resource.dao;

import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public interface ResourceDao {
    ResourceEntity addResource(ResourceEntity res);

    /*
    根目录下资源直接删除document
    非根目录下内容实际就是更新字段，更新根目录下资源的字段
     */
    Object delResource(String uid);

    Object queryByAid(String aid);

    Object updateRes(Query query, Update update);

    ResourceEntity queryByUid(String uid);
}
