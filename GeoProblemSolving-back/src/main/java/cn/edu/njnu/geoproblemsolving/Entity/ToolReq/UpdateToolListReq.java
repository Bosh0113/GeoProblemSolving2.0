package cn.edu.njnu.geoproblemsolving.Entity.ToolReq;


import cn.edu.njnu.geoproblemsolving.domain.tool.ToolEntity;

import java.util.ArrayList;

public class UpdateToolListReq {
    private ArrayList<ToolEntity> newToolList;
    private String tsId;

    public void setTsId(String tsId) {
        this.tsId = tsId;
    }

    public ArrayList<ToolEntity> getNewToolList() {
        return newToolList;
    }

    public String getTsId() {
        return tsId;
    }

    public void setNewToolList(ArrayList<ToolEntity> newToolList) {
        this.newToolList = newToolList;
    }
}
