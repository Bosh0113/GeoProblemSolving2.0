package cn.edu.njnu.geoproblemsolving.business.tool.support.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelService {

    String relateModelItem;
    String md5;
    String name;
    String description;
    String detail;
    String userName;
    String password;
    List<AuthorInfo> authorInfo;
    String mdl;
    String runtime;
}
