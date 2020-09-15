package cn.edu.njnu.geoproblemsolving.business.activity.dto;

import cn.edu.njnu.geoproblemsolving.Dto.ToDomainConverter;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.ActivityType;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.ProjectCategory;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.ProjectPrivacy;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;


@Data
public class UpdateActivityDTO implements ToDomainConverter {
    /**
     * Common activity
     */
    private String name;
    private String description;
    private String purpose;
    private ActivityType type;
    private JSONObject pathway;
    private String permission;


    /**
     * Project
     */
    private ProjectPrivacy privacy;
    private String tag;
    private String picture;
    private ProjectCategory category;
}
