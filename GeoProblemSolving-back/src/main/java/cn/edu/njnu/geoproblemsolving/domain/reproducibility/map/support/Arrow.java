package cn.edu.njnu.geoproblemsolving.domain.reproducibility.map.support;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

/**
 * @Author Zhiyi
 * @Date 2020/7/9  16:21
 * @Version 1.0.0
 */
@Data
@JsonTypeName(value = "arrow")
public class Arrow {
    String target;//箭头的目标
    String source;//箭头初始
}
