package cn.edu.njnu.geoproblemsolving.business.activity.entity.Template.workflow;

import lombok.Data;

import java.util.HashSet;

/**
 * @ClassName Operation
 * @Description Todo
 * @Author zhngzhng
 * @Date 2021/12/9
 **/
@Data
public class Operation {
    String oid;
    String toolId;
    String description;
    HashSet<OperationRes> input;
    HashSet<OperationRes> output;
}
