package cn.edu.njnu.geoproblemsolving.business.activity.processDriven.setvice;

import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity.ActivityGraph;
import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity.ActivityNode;
import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity.LinkRestriction;

import java.util.ArrayList;
import java.util.HashMap;

public interface GeoAnalysisProcess {
    ActivityGraph initActivityGraph(String rootAid);
    /**
     * 新建协议
     * @param graphId 根活动的 aid
     * @param type 协议关系类型
     * @param nodeList
     * @return
     */
    HashMap<String, HashMap<String, LinkRestriction>> setLinkProtocol(String graphId, String type, ArrayList<String> nodeList, LinkRestriction linkRestriction);

    ActivityNode createActivityNode(String aid);
    //协议已经变成了一系列的字符串


    // void addRelationHelper(String start, String end);
    //
    //
    // //采用广度遍历或深度遍历完成连通的查询
    //
    //
    // Object insertSubgraph(String rootActivityId, ArrayList<String> nodes, ArrayList<String> linkType);
    //
    // Object delSubgraph(String protocolId);
    //
    // Object addScatter(String aid);
    //
    // Object checkConnectivity();
    //
    // Object hasEdge(String start, String end);
    //
    // /*
    // 在实现中写一个究极多参数的查找相关联的节点的函数，然后疯狂重载😄 也没想的那么简单哈。。。。
    // 可能需要重载，主要还是限定条件的注入，两大类（user,resource）以及下面的小类：
    // 1. user
    //     domain
    //     organization
    // 2. resource
    //     ......
    //  */
    // Object relationNode(String nodeId, String type, String tag);
    //
    // Object relationNodeUser(String nodeId, String tag);
    //
    // Object relationNode(String node);
    //
    // //遍历图中
    // ArrayList<String> whereUser(String userId);

}
