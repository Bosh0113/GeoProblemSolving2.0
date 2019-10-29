package cn.edu.njnu.geoproblemsolving.Dao.Tool_related;

import cn.edu.njnu.geoproblemsolving.Entity.ToolEntity;

import javax.servlet.http.HttpServletRequest;

public interface IToolDao {

    String createTool(ToolEntity step);
    Object readTool(String key,String value);
    Object readAccessibleTools(String userId);
    void deleteTool(String key,String value);
    String updateTool(HttpServletRequest request);
    String uploadPicture(HttpServletRequest request);
}