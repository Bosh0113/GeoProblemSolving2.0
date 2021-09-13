package cn.edu.njnu.geoproblemsolving.business.tool.generalTool.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

/**
 * @ClassName ToolSetVo
 * @Description Todo
 * @Author zhngzhng
 * @Date 2021/9/13
 **/
@Data
public class ToolSetVo {
    String tid;
    String toolName;
    String description;
    String privacy;
    ArrayList<String> recommendation;
    Boolean toolSet = false;
    ArrayList<String> toolList;
    String toolImg;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    Date createdTime;
}
