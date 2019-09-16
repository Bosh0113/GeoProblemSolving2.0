package cn.edu.njnu.geoproblemsolving.comparison.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 10:29 2019/9/6
 * @Modified By:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class ModelState {
    public String stateId;
    public String stateName;
    public String type;
    public String description;
    public List<ModelEvent> events;
}
