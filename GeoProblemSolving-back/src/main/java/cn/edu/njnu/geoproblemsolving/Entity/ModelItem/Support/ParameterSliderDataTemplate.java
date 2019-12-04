package cn.edu.njnu.geoproblemsolving.Entity.ModelItem.Support;

import lombok.Data;

/**
 * @ClassName ParameterSliderDataTemplate
 * @Description todo
 * @Author sun_liber
 * @Date 2019/11/15
 * @Version 1.0.0
 */
@Data
public class ParameterSliderDataTemplate extends DataTemplate {
    private String max;
    private String min;
    private String step;
    private String defaultValue;
}
