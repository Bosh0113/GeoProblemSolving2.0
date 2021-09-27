package cn.edu.njnu.geoproblemsolving.business.tool.generalTool.dao;

import cn.edu.njnu.geoproblemsolving.business.tool.generalTool.entity.Tool;
import org.springframework.data.mongodb.core.query.Query;

import java.util.HashSet;
import java.util.List;

public interface ToolDao {
    Tool findToolById(String id);

    List<Tool> findAllByToolCreator(String toolCreator);

    Tool createTool(Tool tool);

    long deleteById(String toolId);

    Object emptyProvider(String tid);

    Tool updateTool(Tool putTool, String[] nullPropertyNames);

    Tool saveTool(Tool tool);

    List<Tool> findByFields(Query query);

    List<Tool> findToolByIds(HashSet<String> ids);

}
