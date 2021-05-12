package cn.edu.njnu.geoproblemsolving.Dao.CModel;

import com.alibaba.fastjson.JSONObject;


public interface ModelItemDao  {
//    JSONObject getAllModelPid();
    Object readComputableModel(String pid);
    String updateComputableModel(JSONObject obj);
    String saveOutputLists(JSONObject lists);
}
