package cn.edu.njnu.geoproblemsolving.domain.reproducibility.map;

import cn.edu.njnu.geoproblemsolving.domain.reproducibility.map.support.Nodes;
import lombok.Data;

import java.util.List;

/**
 * @Author Zhiyi
 * @Date 2020/7/9  14:22
 * @Version 1.0.0
 */
@Data
public class ConceptMap {
    List<Nodes> nodes;
    String conceptXml;
}
