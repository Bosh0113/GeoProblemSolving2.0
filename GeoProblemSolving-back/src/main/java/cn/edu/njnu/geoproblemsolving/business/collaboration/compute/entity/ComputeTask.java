package cn.edu.njnu.geoproblemsolving.business.collaboration.compute.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;

/**
 * @ClassName ComputeTask
 * @Description 计算任务实体，主要用于监控计算任务运行状况
 * @Author zhngzhng
 * @Date 2021/5/13
 **/
@Data
@Document(collection = "computeTask")
public class ComputeTask {
    /*
    计算任务基础字段
     */
    @Id
    String taskId;
    TaskType taskType;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    Date invokeTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    Date finishTime;

    /*
    绑定部分内容
     */
    String activityId;
    //基本用作显示，与绑定无关
    String invokeUserId;

    /*
    计算模型相关内容
    无默认值
    不赋值的话，在表中就相当于无此字段
     */
    String taskServiceIp;
    String taskServicePort;
    //模型md5值，管理服务器中使用此来区分模型
    String computableModelMd5;
    String computableModelId;
    String computableModelName;

    /*
    数据方法相关内容
     */
    //在中转服务器中通过数据容器的 token 值进行区分
    String dataContainerToken;
    String dataMethodId;

    /*
    计算任务I/O以及运行状态
     */
    Integer status;
    ArrayList<TaskIO> inputs;
    ArrayList<TaskIO> outputs;
}
