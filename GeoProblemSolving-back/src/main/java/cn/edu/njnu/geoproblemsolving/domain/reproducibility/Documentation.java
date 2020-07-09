package cn.edu.njnu.geoproblemsolving.domain.reproducibility;

import cn.edu.njnu.geoproblemsolving.domain.reproducibility.support.DocumentationInfo;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author Zhiyi
 * @Date 2020/7/9  14:15
 * @Version 1.0.0
 */
@Data
@Document(collection = "ProcessDocumentations")
public class Documentation extends DocumentationInfo {
    public String id;
}
