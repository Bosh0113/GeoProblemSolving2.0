package cn.edu.njnu.geoproblemsolving.business.activity.dto;

import cn.edu.njnu.geoproblemsolving.business.activity.enums.ActivityType;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.ResProtocol;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.RoleProtocol;
import cn.edu.njnu.geoproblemsolving.common.utils.ToDomainConverter;
import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;


@Data
public class UpdateProtocolDTO implements ToDomainConverter {


    private String pid;

    private ArrayList<String> types;
    private ArrayList<String> formats;
    private ArrayList<String> concepts;
    private ArrayList<String> scales;
    private ArrayList<String> references;
    private ArrayList<String> units;

    private ArrayList<String> roles;
    private ArrayList<String> domains;

}
