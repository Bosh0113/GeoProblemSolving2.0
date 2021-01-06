package cn.edu.njnu.geoproblemsolving.business.tool.integration.entity;

import lombok.Data;

import java.util.List;

@Data
public class ModelParam {
    String mId;
    List<Input> inputs;
}
