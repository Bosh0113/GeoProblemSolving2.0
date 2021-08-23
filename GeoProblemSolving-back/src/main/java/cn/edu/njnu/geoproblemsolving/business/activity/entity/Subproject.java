package cn.edu.njnu.geoproblemsolving.business.activity.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Subprojects")
public class Subproject extends Activity{

    /**
     * Privacy control
     */
    public Subproject(){
        this.setLevel(1);
    }
}
