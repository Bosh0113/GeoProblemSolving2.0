package cn.edu.njnu.geoproblemsolving.Entity.Activities;

import cn.edu.njnu.geoproblemsolving.Enums.ProjectCategory;
import cn.edu.njnu.geoproblemsolving.Enums.ProjectPrivacy;
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
