package cn.edu.njnu.geoproblemsolving.business.tool.integration.entity;

import lombok.Data;

import java.util.List;

@Data
public class Input {
    String eventName;
    String stateName;
    String eventID;
    String eventDesc;

    List<Param> params;
}
