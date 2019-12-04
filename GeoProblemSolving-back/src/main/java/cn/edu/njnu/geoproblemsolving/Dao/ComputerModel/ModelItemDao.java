package cn.edu.njnu.geoproblemsolving.Dao.ComputerModel;

import cn.edu.njnu.geoproblemsolving.Entity.ModelItem.ModelItemEntity;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;


public interface ModelItemDao  {
    Object readComputableModel(String oid);
    String updateComputableModel(JSONObject obj);
}
