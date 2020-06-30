package cn.edu.njnu.geoproblemsolving.Entity.ToolReq;


import cn.edu.njnu.geoproblemsolving.domain.tool.Tool;

public class AddToolReq {
    private Tool newTool;
    private String[] tsIds;

    public String[] getTsIds() {
        return tsIds;
    }

    public Tool getNewTool() {
        return newTool;
    }

    public void setTsIds(String[] tsIds) {
        this.tsIds = tsIds;
    }

    public void setNewTool(Tool newTool) {
        this.newTool = newTool;
    }
}
