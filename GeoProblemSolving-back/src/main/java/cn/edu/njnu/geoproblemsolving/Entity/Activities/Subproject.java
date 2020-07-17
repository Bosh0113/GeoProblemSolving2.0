package cn.edu.njnu.geoproblemsolving.Entity.Activities;

import cn.edu.njnu.geoproblemsolving.Enums.SubprojectPrivacy;
import com.alibaba.fastjson.JSONArray;
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
