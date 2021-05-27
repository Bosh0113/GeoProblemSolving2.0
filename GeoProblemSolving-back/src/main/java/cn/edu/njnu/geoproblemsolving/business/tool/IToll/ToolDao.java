package cn.edu.njnu.geoproblemsolving.business.tool.IToll;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ToolDao {
    Tool findToolById(String id);

    List<Tool> findAllByToolCreator(String toolCreator);

    Tool createTool(Tool tool);

    long deleteById(String toolId);

    Tool updateTool(Tool putTool, String[] nullPropertyNames);

}
