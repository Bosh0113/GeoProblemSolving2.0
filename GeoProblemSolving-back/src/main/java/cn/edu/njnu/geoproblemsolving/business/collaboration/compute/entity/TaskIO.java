package cn.edu.njnu.geoproblemsolving.business.collaboration.compute.entity;

import lombok.Data;

import java.util.ArrayList;

/**
 * @ClassName TaskIO
 * @Description task的输入输出
 * @Author zhngzhng
 * @Date 2021/5/13
 **/
@Data
public class TaskIO {
    String taskDataId;
    /*
    计算模型数据
     */
    String stateName;
    String event;
    String url;
    String visualUrl;
    ArrayList<String> urls;
    String tag;
    String suffix;
    String templateId;
    Boolean multipleFile = false;
    Boolean visual;

    /*
    数据方法数据
     */
    String token;
    String dataMethodId;
    ArrayList<String> dataUrls;
    String params;
}
