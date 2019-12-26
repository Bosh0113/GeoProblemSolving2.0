package cn.edu.njnu.geoproblemsolving.Entity.ModelTools.ModelItem.Support;

import lombok.Data;

import java.util.List;

/**
 * @ClassName ParameterTableDataTemplate
 * @Description todo
 * @Author sun_liber
 * @Date 2019/11/15
 * @Version 1.0.0
 */
@Data
public class ParameterTableDataTemplate extends DataTemplate {
    private List<String> fields;
    private String defaultValue;
}
