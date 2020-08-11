package cn.edu.njnu.geoproblemsolving.domain.activity;

import cn.edu.njnu.geoproblemsolving.domain.activity.enums.ProjectCategory;
import cn.edu.njnu.geoproblemsolving.domain.activity.enums.ProjectPrivacy;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Projects")
public class Project extends Activity{

    /**
     * Privacy control
     */
    private ProjectPrivacy privacy;

    /**
     * Permission Manager
     */
    private String permission;

    /**
     * tag
     * picture: descriptive picture
     */
    private String tag;
    private String picture;

    /**
     * Project category
     */
    private ProjectCategory category;

    public Project(){
        this.setLevel(0);
    }
}
