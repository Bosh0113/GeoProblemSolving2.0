package cn.edu.njnu.geoproblemsolving.comparison.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 10:34 2019/9/6
 * @Modified By:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class ModelEvent {
    public String name;
    public String type;
    public String description;
    public boolean optional;
}
