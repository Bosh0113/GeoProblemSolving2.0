package cn.edu.njnu.geoproblemsolving.Entity.ModelItem.Support;

import cn.edu.njnu.geoproblemsolving.Entity.ModelItem.Support.Event;
import lombok.Data;

import java.util.List;

/**
 * @ClassName State
 * @Description todo
 * @Author sun_liber
 * @Date 2019/11/6
 * @Version 1.0.0
 */
@Data
public class State {
    private String name;
    private String des;
    private String id;
    private String type;
    private List<Event> eventList;
}
