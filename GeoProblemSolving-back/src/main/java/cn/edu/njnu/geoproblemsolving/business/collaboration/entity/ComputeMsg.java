package cn.edu.njnu.geoproblemsolving.business.collaboration.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

/**
 * @Author mzy
 * @Date 2021/4
 * @Description 计算记录
 */
@Data
public class ComputeMsg {

    private String toolId;

    private String aid;

    private ArrayList<CollaborationUser> receivers;

    private String time;
}
