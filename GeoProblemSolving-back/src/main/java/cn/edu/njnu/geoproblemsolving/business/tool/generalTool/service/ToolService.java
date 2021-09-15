package cn.edu.njnu.geoproblemsolving.business.tool.generalTool.service;

import cn.edu.njnu.geoproblemsolving.business.tool.generalTool.entity.Tool;
import cn.edu.njnu.geoproblemsolving.business.tool.generalTool.entity.ToolSetVo;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public interface ToolService {
    List<JSONObject> getComputeModelByUserEmail(String email, int page, int size);

    List<JSONObject> getPublicComputeModel(int page, int size);

    JSONObject getComputableModelList(String email, int page, int size);

    JSONObject queryPublicComputeModelByName(String searchText, int page);


    List<JSONObject> getDataMethodByUserEmail(String email, int page, int size);

    List<JSONObject> getPublicDataMethod(int page, int size);

    JSONObject getDataMethodList(String email, int page, int size);

    JSONObject queryPublicDataMethodByName(String searchText, int page);


    Tool createTool(JSONObject tool) throws UnsupportedEncodingException;

    ToolSetVo addToolInToolSet(String tid, ArrayList<String> tids);

    ToolSetVo delToolInToolSet(String tid, ArrayList<String> tids);

    ToolSetVo createToolSet(Tool toolSet);

    List<Tool> queryTool(ArrayList<String> key, ArrayList<String> value);

    List<Tool> getToolByProviderService(String provider);

    Tool getToolByTid(String tid);

    Tool updateToolService(Tool putTool);

    //将工具标记为删除
    void delToolService(String tid);

    List<Tool> getToolByIds(ArrayList<String> ids);

    Object emptyProviderService(String tid);

    JsonResult uploadToolImg(HttpServletRequest req) throws IOException, ServletException;


    List<Tool> getRelevantPurposeTool(String purpose);
}
