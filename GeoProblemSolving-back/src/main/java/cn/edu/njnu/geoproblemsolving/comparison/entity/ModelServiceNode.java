package cn.edu.njnu.geoproblemsolving.comparison.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 14:49 2019/9/6
 * @Modified By:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class ModelServiceNode {
    public String host;
    public String port;
    public String msid;
}
