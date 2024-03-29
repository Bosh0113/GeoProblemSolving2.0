package cn.edu.njnu.geoproblemsolving.Dao.Tool_related;


import cn.edu.njnu.geoproblemsolving.Entity.ToolReq.UpdateToolListReq;
import cn.edu.njnu.geoproblemsolving.Entity.ToolsetEntity;
import cn.edu.njnu.geoproblemsolving.domain.tool.ToolEntity;

import javax.servlet.http.HttpServletRequest;

public interface IToolsetDao {
    Object createToolset(ToolsetEntity step);
    Object readToolset(String key,String value);
    Object readAccessibleToolsets(String userId);
    void deleteToolset(String key,String value);
    String updateToolset(HttpServletRequest request);
    String updateToolList(UpdateToolListReq updateToolListReq);
    String uploadPicture(HttpServletRequest request);
    String updateToolsetbyToolset(ToolsetEntity toolset);
    String addToolToToolset(ToolEntity newTool, String[] tsIds);
}
