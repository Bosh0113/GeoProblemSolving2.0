package cn.edu.njnu.geoproblemsolving.Dao.Tool_related;


import cn.edu.njnu.geoproblemsolving.domain.tool.Tool;

import javax.servlet.http.HttpServletRequest;

public interface IToolDao {

    Object createTool(Tool step);
    Object readTool(String key,String value);
    Object readAccessibleTools(String userId);
    void deleteTool(String key,String value);
    String updateTool(HttpServletRequest request);
    String uploadPicture(HttpServletRequest request);
}
