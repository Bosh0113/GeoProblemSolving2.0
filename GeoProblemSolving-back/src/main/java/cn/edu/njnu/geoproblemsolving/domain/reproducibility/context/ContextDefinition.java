package cn.edu.njnu.geoproblemsolving.domain.reproducibility.context;

import lombok.Data;

/**
 * @Author Zhiyi
 * @Date 2020/7/9  13:53
 * @Version 1.0.0
 */
@Data
public class ContextDefinition {
    String theme;
    String purpose;
    String simulationObject;//sumulation object
    String methods;
    String expectedResults;//expected results
    String drawbacks;
}
