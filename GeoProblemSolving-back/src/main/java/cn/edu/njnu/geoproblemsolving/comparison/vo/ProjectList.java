package cn.edu.njnu.geoproblemsolving.comparison.vo;

import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpProject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 16:35 2019/7/10
 * @Modified By:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class ProjectList {
    private List<CmpProject> projects;
}
