package cn.edu.njnu.geoproblemsolving.domain.reproducibility.map.support;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

/**
 * @Author Zhiyi
 * @Date 2020/7/9  16:34
 * @Version 1.0.0
 */
@Data
@JsonTypeName(value = "judgement")
public class JudgementNode {
    Boolean result;
}
