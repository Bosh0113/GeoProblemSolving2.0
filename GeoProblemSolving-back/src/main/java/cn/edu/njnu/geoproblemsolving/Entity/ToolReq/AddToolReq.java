package cn.edu.njnu.geoproblemsolving.Entity.ToolReq;


import cn.edu.njnu.geoproblemsolving.business.tool.ToolEntity;

public class AddToolReq {
    private ToolEntity newTool;
    private String[] tsIds;

    public String[] getTsIds() {
        return tsIds;
    }

    public ToolEntity getNewTool() {
        return newTool;
    }

    public void setTsIds(String[] tsIds) {
        this.tsIds = tsIds;
    }

    public void setNewTool(ToolEntity newTool) {
        this.newTool = newTool;
    }
}
