package cn.edu.njnu.geoproblemsolving.domain.reproducibility.map.support;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * @Author Zhiyi
 * @Date 2020/7/9  15:58
 * @Version 1.0.0
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type",visible =true)
public class Nodes {
    String type;
    String name;
}
