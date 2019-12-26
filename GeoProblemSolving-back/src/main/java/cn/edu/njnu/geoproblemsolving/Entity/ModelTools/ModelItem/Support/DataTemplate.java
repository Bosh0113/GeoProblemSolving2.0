package cn.edu.njnu.geoproblemsolving.Entity.ModelTools.ModelItem.Support;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;


/**
 * @ClassName DataTemplate
 * @Description todo
 * @Author sun_liber
 * @Date 2019/11/14
 * @Version 1.0.0
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type",visible = true)
@JsonSubTypes({@JsonSubTypes.Type(value = FileDataTemplate.class, name = "file")
        , @JsonSubTypes.Type(value = ParameterInputDataTemplate.class, name = "parameter_input"),
        @JsonSubTypes.Type(value = ParameterSliderDataTemplate.class, name = "parameter_range"),
        @JsonSubTypes.Type(value = ParameterSelectDataTemplate.class, name = "parameter_select"),
        @JsonSubTypes.Type(value = ParameterTableDataTemplate.class, name = "parameter_table")
})
public class DataTemplate {
    private String type;
    private String tooltip ;
    private String value;
}
