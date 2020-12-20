package cn.edu.njnu.geoproblemsolving.business.resource.entity;

import cn.edu.njnu.geoproblemsolving.Dto.ToDomainConverter;
import lombok.Data;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/18 18:48
 * @modified By：
 * @version: 1.0.0
 */
@Data
public class AddIResourceDTO implements ToDomainConverter<IResourceEntity> {
    private String name;
    private String description;
    private String type;
    private String fileSize;
    private String pathURL;
    private String uploaderId;
    private String uploaderName;
    private String uploadTime;
    private String privacy;     //public/private
    private String thumbnail; //缩略图地址
    private String editToolInfo;
}
