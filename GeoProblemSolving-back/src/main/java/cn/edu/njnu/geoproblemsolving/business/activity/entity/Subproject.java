package cn.edu.njnu.geoproblemsolving.business.activity.entity;

import cn.edu.njnu.geoproblemsolving.business.activity.enums.SubprojectPrivacy;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Subprojects")
public class Subproject extends Activity{

    /**
     * Privacy control
     */
    private SubprojectPrivacy privacy;

    public Subproject(){
        this.setLevel(1);
    }
}
