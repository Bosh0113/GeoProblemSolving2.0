package cn.edu.njnu.geoproblemsolving.Dao.CModel;

import com.alibaba.fastjson.JSONObject;


public interface ModelItemDao  {
    Object readComputableModel(String oid);
    String updateComputableModel(JSONObject obj);
    String saveOutputLists(JSONObject lists);
}
