package cn.edu.njnu.geoproblemsolving.Entity.ModelItem.Support;

import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Event
 * @Description todo
 * @Author sun_liber
 * @Date 2019/11/6
 * @Version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    private String name;
    private String des;
    private Boolean isOptional;
    private IOFlagEnum ioFlagEnum;
    private DataTemplate dataTemplate;
    private String url;//上传文件返回的url
//    private String eventType;
//    private ResponseParameter responseParameter;
}