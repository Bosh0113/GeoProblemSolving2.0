package cn.edu.njnu.geoproblemsolving.Dao.Tool_related;

import cn.edu.njnu.geoproblemsolving.Entity.ToolsetEntity;

import javax.servlet.http.HttpServletRequest;

public interface IToolsetDao {
    String createToolset(ToolsetEntity step);
    Object readToolset(String key,String value);
    void deleteToolset(String key,String value);
    String updateToolset(HttpServletRequest request);
    String updateToolsetbyToolset(ToolsetEntity toolset);
}
