package cn.edu.njnu.geoproblemsolving.Dao.CModel;

import cn.edu.njnu.geoproblemsolving.Entity.ModelTools.ToolRecords.ToolRecordsEntity;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @ClassName ToolRecordDao
 * @Description
 * @Author Zhiyi
 * @Date 2019/12/25  15:29
 * @Version 1.0.0
 */
public interface ToolRecordDao {
    String saveRecord(JSONObject obj);
    List<ToolRecordsEntity> getAllRecords(String stepId);
}
