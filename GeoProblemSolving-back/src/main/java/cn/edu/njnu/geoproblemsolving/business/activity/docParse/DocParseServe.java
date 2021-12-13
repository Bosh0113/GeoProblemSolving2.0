package cn.edu.njnu.geoproblemsolving.business.activity.docParse;

import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Activity;
import cn.edu.njnu.geoproblemsolving.business.collaboration.entity.MsgRecords;
import cn.edu.njnu.geoproblemsolving.business.tool.generalTool.entity.Tool;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public interface DocParseServe {
    void initActivityDoc(Activity activity);

    Object changeActivityType(String aid, Activity activity);

    Object userJoin(String aid, String userId);

    Object userJoin(String aid, HashSet<String> userIds);


    Object uploadResource(String aid, HashMap<String, String> resInfo);

    ArrayList<HashMap<String, String>> uploadResource(String aid, ArrayList<ResourceEntity> resourceEntities);

    Object resFlow(String fromAid, String endAid, HashSet<String> uids);

    HashMap<String, String> resFlow(String formAid, String endAid, String uid);

    ArrayList<HashMap<String, String>> getResInfo(String aid, HashSet<String> uids);

    HashMap<String, String> getResInfo(String aid, String uid);

    ArrayList<HashMap<String, String>> getResInfo(String aid);

    void appendChildActivity(String aid, String childId, String name, String creator);

    Object addTool(String aid, List<Tool> tools);

    Object removeTool(String aid, String tid);

    Object removeTool(String aid, HashSet<String> tids);

    String messageRecord(String tid, MsgRecords msgRecords);

    String geoAnalysis(String aid, String toolId,
                       HashSet<String> inResId,
                       ArrayList<ResourceEntity> outRes,
                       HashSet<String> participants);


    String geoAnalysisRLS90(String aid, String toolId,
                            HashSet<String> onlineMembers,
                            String purpose,
                            ResourceEntity input,
                            ResourceEntity output);


    //Mapping
    Object toWorkflowTemplate(String aid);

}
