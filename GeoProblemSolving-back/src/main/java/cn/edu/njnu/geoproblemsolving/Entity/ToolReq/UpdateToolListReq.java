package cn.edu.njnu.geoproblemsolving.Entity.ToolReq;


import cn.edu.njnu.geoproblemsolving.domain.tool.Tool;

import java.util.ArrayList;

public class UpdateToolListReq {
    private ArrayList<Tool> newToolList;
    private String tsId;

    public void setTsId(String tsId) {
        this.tsId = tsId;
    }

    public ArrayList<Tool> getNewToolList() {
        return newToolList;
    }

    public String getTsId() {
        return tsId;
    }

    public void setNewToolList(ArrayList<Tool> newToolList) {
        this.newToolList = newToolList;
    }
}
