package cn.edu.njnu.geoproblemsolving.business.activity.processDriven.setvice.impl;

import cn.edu.njnu.geoproblemsolving.business.activity.dao.Impl.ActivityLinkProtocolDaoImpl;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Activity;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.ResProtocol;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.RoleProtocol;
import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity.ActivityGraph;
import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity.ActivityNode;
import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.setvice.GeoAnalysisProcess;
import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity.LinkRestriction;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ActivityGraphRepository;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ActivityNodeRepository;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ActivityRepository;
import cn.edu.njnu.geoproblemsolving.business.resource.dao.ActivityResDaoImpl;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.resource.service.Impl.ActivityResServiceImpl;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @ClassName GeoAnalysisProcess
 * @Description 进行大图维护以及对外提供一系列接口
 * 后续设计的东西为了能向上兼容的话，最好是以类似于 AOP 的方式进行
 * 这样就不必对以前的代码进行大量的修改
 * @Author zhngzhng
 * @Date 2021/7/21
 **/
@Service
public class GeoAnalysisProcessImpl implements GeoAnalysisProcess {
    private final ActivityLinkProtocolDaoImpl protocolDao;

    private final RestTemplate restTemplate;

    private final ActivityRepository activityRepository;

    private final ActivityGraphRepository graphRepository;

    private final ActivityResServiceImpl activityResService;

    private final ActivityNodeRepository nodeRepository;

    private final ActivityResDaoImpl activityResDao;

    public GeoAnalysisProcessImpl(ActivityRepository activityRepository, ActivityLinkProtocolDaoImpl protocolDao, ActivityGraphRepository graphRepository, RestTemplate restTemplate, ActivityResServiceImpl activityResService, ActivityNodeRepository nodeRepository, ActivityResDaoImpl activityResDao) {
        this.activityRepository = activityRepository;
        this.protocolDao = protocolDao;
        this.graphRepository = graphRepository;
        this.restTemplate = restTemplate;
        this.activityResService = activityResService;
        this.nodeRepository = nodeRepository;
        this.activityResDao = activityResDao;
    }

    @Value("${userServerLocation}")
    String userServer;

    /**
     * 初始化 graph
     * 应用场景：
     * 将项目设定为 MultiActivity 时调用
     *
     * @param rootAid
     * @return
     */
    @Override
    public ActivityGraph initActivityGraph(String rootAid) {
        ActivityGraph activityGraph = new ActivityGraph();
        activityGraph.setId(rootAid);
        activityGraph.setAdjacencyMap(new HashMap<>());
        activityGraph.setCreateDate(new Date());
        return graphRepository.save(activityGraph);
    }

    /**
     * 新建协议
     * 1. 新建协议
     * 2. 维护图
     * 新建协议需要做的
     * 判断节点是否存在，不存在则创建
     * 将协议添加到图中去[实质就是在图中添加边（由于关系的存在所以是简化了用户添加边的困难，还有就是是否可以通过边来简化遍历的内容），判断重复边]
     * 根据协议进行资源流动，先进行关系内资源流动，在节点上进行操作，然后再更新到 activity 上去
     * 遍历图找到关联的节点
     * （由于关系的存在，可以简化操作，如1个分散为3个，可以直接先遍历分散点，完成上层的关联点的判断然后根据关系完成流动，但是这只是简化了）
     * (可能还并不使用)还可以使用关系进行简化存储，现在的邻接表存储的是0->(1,constraint) 可以简化为 => 0 ->
     *
     * @param rootAid         协议所依附于的活动
     * @param type            协议的关系
     * @param nodeIdList      协议中节点的id,即相关联的活动 id
     * @param linkRestriction 协议设置的资源与用户的限制条件
     * @return 参数很多 restFul 风格是 hold 不住的
     */
    @Override
    public HashMap<String, HashMap<String, LinkRestriction>> setLinkProtocol(String rootAid, String type, ArrayList<String> nodeIdList, LinkRestriction linkRestriction) {
        Optional<ActivityGraph> graphOptional = graphRepository.findById(rootAid);
        ActivityGraph activityGraph;
        if (!graphOptional.isPresent()) {
            // 无此图则初始化即可，不用报错
            activityGraph = initActivityGraph(rootAid);
        } else {
            activityGraph = graphOptional.get();
        }
        //Acquiring the graph's adjacency list.
        HashMap<String, HashMap<String, LinkRestriction>> adjacencyMap = activityGraph.getAdjacencyMap();
        // 生成协议
        HashMap<String, HashMap<String, LinkRestriction>> protocolByRelation = setProtocolByRelation(adjacencyMap, nodeIdList, type, linkRestriction);
        //更新邻接表
        adjacencyMap.putAll(protocolByRelation);
        activityGraph.setAdjacencyMap(adjacencyMap);
        graphRepository.save(activityGraph);

        /*
        获得协议的上层节点，用于协议内部的资源流动
        需要完成将协议添加到图中的操作,才能实现此步骤
         */
        HashMap<String, HashMap<String, LinkRestriction>> relevantUpperNode = getRelevantUpperNode(adjacencyMap, nodeIdList);
        //完成关系内资源流动
        resFlowInProtocol(relevantUpperNode, type, nodeIdList, linkRestriction);
        /*
        关系下层资源流动
        1. 关系下层邻接表
        2. 资源流动
         */
        //在此不用多此一举增加构建关系内的邻接表
        // HashMap<String, HashMap<String, LinkRestriction>> subgraphLinkList = setSubgraphLinkList(nodeIdList, type, linkRestriction);
        // resFlowInProtocol(relevantUpperNode, type);
        return protocolByRelation;
    }

    /**
     * 生成协议，包含与协议内节点相关联的其他节点（邻接表结构）
     *
     * @param adjacencyMap
     * @param nodeIdList
     * @param type
     * @param linkRestriction
     * @return
     */
    private HashMap<String, HashMap<String, LinkRestriction>> setProtocolByRelation(HashMap<String, HashMap<String, LinkRestriction>> adjacencyMap, ArrayList<String> nodeIdList, String type, LinkRestriction linkRestriction) {
        //取出第一个节点（关键节点，在排序规则中第一个会汇聚点，分散点，起点）
        String keyNodeId = nodeIdList.get(0);
        //判断协议中节点是否存在，若不存在则新建
        for (String nodeId : nodeIdList) {
            if (nodeIsPresent(nodeId) == null) {
                createActivityNode(nodeId);
            }
        }
        //初始化终点数据结构
        HashMap<String, LinkRestriction> endNodeAndRestriction = new HashMap<>();
        //初始化协议数据结构
        HashMap<String, HashMap<String, LinkRestriction>> linkProtocol = new HashMap<>();
        //将节点转换为邻接表---由于有关系类型的存在，简化了部分关系内部的操作
        /*
        1. 先新建节点
        2. 再将关系添加到图中
        新建关系内节点的邻接表
        有个注意点，需要将终点加在起点已有的邻接表中
        如果此起点无其他终点，则可以为其新建邻接表并将其放在图中
        上述步骤被封装在 generateEdgeEndNode 中
        */
        switch (type) {
            case "Convergence":
                for (int i = 1; i < nodeIdList.size(); i++) {
                    String startNodeId = nodeIdList.get(i);
                    //将终点放入起点的邻接表中
                    endNodeAndRestriction.clear();
                    endNodeAndRestriction = generateEdgeEndNode(adjacencyMap, startNodeId, keyNodeId, linkRestriction);
                    linkProtocol.put(startNodeId, endNodeAndRestriction);
                }
                break;
            case "Dispersed":
                for (int i = 1; i < nodeIdList.size(); i++) {
                    String endNodeId = nodeIdList.get(i);
                    /*
                    同一起点，只需要判断一次起点是否有多边连接
                    如果有的话，其他所有终点都已经被存入
                    后续几个终点只需要存入即可
                     */
                    if (i == 1) {
                        endNodeAndRestriction = generateEdgeEndNode(adjacencyMap, keyNodeId, endNodeId, linkRestriction);
                        continue;
                    }
                    LinkRestriction oldRestriction = edgeIsPresent(adjacencyMap, keyNodeId, endNodeId);
                    LinkRestriction newRestriction = linkRestriction;
                    if (oldRestriction != null) {
                        newRestriction = mergeLinkProperties(linkRestriction, oldRestriction);
                    }
                    endNodeAndRestriction.put(endNodeId, newRestriction);
                }
                linkProtocol.put(keyNodeId, endNodeAndRestriction);
                break;
            case "loop":
                //单向循环模式
                for (int i = 0; i < nodeIdList.size(); i++) {
                    String startNodeId = nodeIdList.get(i);
                    String endNodeId;
                    // 起点为最后列表中最后一个则起点为 keyNode
                    endNodeId = (i == nodeIdList.size() - 1) ? keyNodeId : nodeIdList.get(i + 1);
                    endNodeAndRestriction.clear();
                    endNodeAndRestriction = generateEdgeEndNode(adjacencyMap, startNodeId, endNodeId, linkRestriction);
                    linkProtocol.put(startNodeId, endNodeAndRestriction);
                }
                break;
            case "order":
                //串联模式
                for (int i = 0; i < nodeIdList.size() - 1; i++) {
                    String startNodeId = nodeIdList.get(i);
                    String endNodeId = nodeIdList.get(i + 1);
                    endNodeAndRestriction.clear();
                    endNodeAndRestriction = generateEdgeEndNode(adjacencyMap, startNodeId, endNodeId, linkRestriction);
                    linkProtocol.put(startNodeId, endNodeAndRestriction);
                }
                break;
        }
        return linkProtocol;
    }

    /**
     * 关系上层及关系内资源的流动
     * 1. 关系上层到关系连接点的连接
     * 2. 关系内部的流动
     * ps: 可能需要注意资源的去重，去除 id 相同的即可
     *
     * @param upperNodeLinkList 关系上层节点关系
     * @param nodeIds           关系
     * @return
     */
    private void resFlowInProtocol(HashMap<String, HashMap<String, LinkRestriction>> upperNodeLinkList, String type, ArrayList<String> nodeIds, LinkRestriction linkRestriction) {
        //关系上层到关系连接点的流动
        for (Map.Entry<String, HashMap<String, LinkRestriction>> linkedEdge : upperNodeLinkList.entrySet()) {
            String flowNodeId = linkedEdge.getKey();
            HashMap<String, LinkRestriction> linkNodes = linkedEdge.getValue();
            //获得起点中的资源
            ActivityNode flowNode = nodeRepository.findById(flowNodeId).get();
            HashMap<String, String> flowNodeResources = flowNode.getResources();
            // 遍历节点
            for (Map.Entry<String, LinkRestriction> endNodeItem : linkNodes.entrySet()) {
                String endNodeId = endNodeItem.getKey();
                LinkRestriction edgeRestriction = endNodeItem.getValue();
                HashSet<String> approvedRes = resFlowApprove(flowNodeResources, edgeRestriction);
                // 无可流动的资源
                if (approvedRes.size() == 0) continue;
                HashMap<String, String> nodeResourceTagMap = addResourceToNode(flowNodeId, endNodeId, approvedRes);
                ActivityNode endNode = nodeRepository.findById(endNodeId).get();
                endNode.setResources(nodeResourceTagMap);
                nodeRepository.save(endNode);
            }
        }

        //内部的流动，有个可以省略的地方，协议内部的限制性条件是相同的
        String keyNodeId = nodeIds.get(0);
        ActivityNode keyNode = nodeRepository.findById(keyNodeId).get();
        HashMap<String, String> keyNodeResources = keyNode.getResources();
        switch (type) {
            case "Merger":
                for (int i = 1; i < nodeIds.size(); i++) {
                    String startId = nodeIds.get(i);
                    ActivityNode startNode = nodeRepository.findById(startId).get();
                    HashMap<String, String> startNodeResourceMap = startNode.getResources();
                    HashSet<String> approvedRes = resFlowApprove(startNodeResourceMap, linkRestriction);
                    HashMap<String, String> endNodeResourceTagMap = addResourceToNode(startId, keyNodeId, approvedRes);
                    keyNode.setResources(endNodeResourceTagMap);
                    nodeRepository.save(keyNode);
                }
                break;
            case "Branch":
                HashSet<String> approvedRes = resFlowApprove(keyNodeResources, linkRestriction);
                if (approvedRes.size() == 0) {
                    break;
                }
                for (int i = 1; i < nodeIds.size(); i++) {
                    String endNodeId = nodeIds.get(i);
                    HashMap<String, String> endNodeResourceTagMap = addResourceToNode(keyNodeId, endNodeId, approvedRes);
                    ActivityNode endNode = nodeRepository.findById(endNodeId).get();
                    endNode.setResources(endNodeResourceTagMap);
                    nodeRepository.save(endNode);
                }
                break;
            case "Loop":
                for (int i = 0; i < nodeIds.size(); i++) {
                    String startId = nodeIds.get(i);
                    String endNodeId = (i == nodeIds.size() - 1) ? keyNodeId : nodeIds.get(i + 1);
                    ActivityNode endNode = nodeRepository.findById(endNodeId).get();
                    HashSet<String> approvedResId = resFlowApprove(startId, linkRestriction);
                    HashMap<String, String> endNodeResourceTag = addResourceToNode(startId, endNodeId, approvedResId);
                    endNode.setResources(endNodeResourceTag);
                    nodeRepository.save(endNode);
                }
                break;
            case "Sequence":
                for (int i = 0; i < nodeIds.size() -1; i++) {
                    String startId = nodeIds.get(i);
                    String endId = nodeIds.get(i + 1);
                    HashSet<String> flowApprove = resFlowApprove(startId, linkRestriction);
                    HashMap<String, String> endNodeResourceTag = addResourceToNode(startId, endId, flowApprove);
                    ActivityNode endNode = nodeRepository.findById(endId).get();
                    endNode.setResources(endNodeResourceTag);
                    nodeRepository.save(endNode);
                }
                break;
        }
    }


    /**
     * 两个节点间的资源流动
     *
     * @param startNodeId     流动节点 id
     * @param endNodeId       接收流动资源节点的 id
     * @param linkRestriction 节点间的资源限定条件
     * @return
     */
    private HashMap<String, String> resFlowInNode(String startNodeId, String endNodeId, LinkRestriction linkRestriction) {
        ActivityNode startNode = nodeRepository.findById(startNodeId).get();
        HashMap<String, String> startNodeResourceTagMap = startNode.getResources();

        HashSet<String> approveRes = resFlowApprove(startNodeResourceTagMap, linkRestriction);
        // 没有可流动资源，则直接返回原来的即可
        if (approveRes.size() == 0) {
            return null;
        }
        //判断在当前协议下哪些资源能够得到授权
        return addResourceToNode(startNodeId, endNodeId, approveRes);
    }


    /**
     * 获取当前关系的上级节点及对应的边
     * 这个起终点是的反向的，终点->[起点，限制性条件]
     * 起终点倒置 end->[{start, link} .... {start, link}]
     *
     * @param adjacencyMap 邻接表，图
     * @param nodeList     关系中的节点 id 列表
     * @return key: 关系中节点
     * value: 与该节点先关联的上层节点
     * 约定大于配置的话
     * 存储顺序还是的采用同关系中节点相同的存储顺序
     * 每个节点的都可以有多个上层节点（父级）
     * 这里相当于把起终点顺序给倒置了
     * 还有一个问题：是否一同返回边的限制性条件
     * 是，方便直接验证，不用再根据起终点来拿限制性条件
     * 同key的map，直接value覆盖
     */
    private HashMap<String, HashMap<String, LinkRestriction>> getRelevantUpperNode(HashMap<String, HashMap<String, LinkRestriction>> adjacencyMap, ArrayList<String> nodeList) {
        //初始化用于存储返回值,key:终点, value.key 起点
        HashMap<String, HashMap<String, LinkRestriction>> relevantNode = new HashMap<>();
        //取出邻接表中的终点，通过终点来获取起点
        for (Map.Entry<String, HashMap<String, LinkRestriction>> item : adjacencyMap.entrySet()) {
            HashMap<String, LinkRestriction> endNodeList = item.getValue();
            //遍历协议节点列表，如果是边的终点，则将存入
            for (String nodeId : nodeList) {
                /*
                判断此终点 List 中是否含有当前节点;
                若有则该起点连接着该节点
                 */
                if (endNodeList.containsKey(nodeId)) {
                    LinkRestriction linkRestriction = endNodeList.get(nodeId);
                    // 获取起始节点的 NodeId, 并构造<start, Restriction>结构存入返回值中 <End, <Start, Restriction>>
                    String startNodeId = item.getKey();
                    /*
                    若返回值中已经有以该节点为终点的边了，则将其取出，并将新边放进去
                     */
                    HashMap<String, LinkRestriction> startAndLinkRestriction =
                            (relevantNode.containsKey(nodeId)) ? relevantNode.get(nodeId) : new HashMap<>();
                    startAndLinkRestriction.put(startNodeId, linkRestriction);
                    relevantNode.put(nodeId, startAndLinkRestriction);
                }
            }
        }
        return relevantNode;
    }

    /**
     * 关系下层资源的流动
     * 麻烦一些，因为可能路径比较复杂，话又说回来，一个活动下子活动应该不会超过10个，10个的话无论我怎么连接，路径都很少，不存在非常慢的情况
     * ，不过麻烦不在这个方法中，而是在遍历的时候
     *
     * @param adjacencyMap
     * @return
     */
    private void resFlowLower(HashMap<String, HashMap<String, LinkRestriction>> adjacencyMap) {


    }

    /**
     * 构建子图
     * 单个关系内部，不考虑外部的连接
     * 主要用于内部资源流动
     *
     * @param nodeIdList
     * @param type
     * @return
     */
    private HashMap<String, HashMap<String, LinkRestriction>> setSubgraphLinkList(ArrayList<String> nodeIdList, String type, LinkRestriction linkRestriction) {
        HashMap<String, HashMap<String, LinkRestriction>> subgraphLinkList = new HashMap<>();
        //判断节点是否存在，若无则新建
        for (String nodeId : nodeIdList) {
            if (nodeIsPresent(nodeId) == null) createActivityNode(nodeId);
        }
        String keyNodeId = nodeIdList.get(0);
        HashMap<String, LinkRestriction> endNodeAndRestriction = new HashMap<>();
        switch (type) {
            case "Convergence":
                //将终点放入，然后再成边
                endNodeAndRestriction.put(keyNodeId, linkRestriction);
                for (int i = 1; i < nodeIdList.size(); i++) {
                    String startNodeId = nodeIdList.get(i);
                    subgraphLinkList.put(startNodeId, endNodeAndRestriction);
                }
                break;
            case "Dispersed":
                // 构造终点，然后成边
                for (int i = 1; i < nodeIdList.size(); i++) {
                    String endNodeId = nodeIdList.get(i);
                    endNodeAndRestriction.put(endNodeId, linkRestriction);
                }
                subgraphLinkList.put(keyNodeId, endNodeAndRestriction);
                break;
            case "loop":
                //循环构建，最后一个节点的终点是第一个点
                for (int i = 0; i < nodeIdList.size(); i++) {
                    String startNodeId = nodeIdList.get(i);
                    String endNodeId = (i == nodeIdList.size() - 1) ? nodeIdList.get(0) : nodeIdList.get(i + 1);
                    //清除上一个节点邻接表
                    endNodeAndRestriction.clear();
                    //将终点存入map
                    endNodeAndRestriction.put(endNodeId, linkRestriction);
                    //将边添加到子图中
                    subgraphLinkList.put(startNodeId, endNodeAndRestriction);
                }
                break;
            case "order":
                // i 为起点， i+1 为终点
                for (int i = 0; i < nodeIdList.size() - 1; i++) {
                    String startNodeId = nodeIdList.get(i);
                    String endNodeId = nodeIdList.get(i + 1);
                    endNodeAndRestriction.clear();
                    endNodeAndRestriction.put(endNodeId, linkRestriction);
                    subgraphLinkList.put(startNodeId, endNodeAndRestriction);
                }
                break;

        }
        return subgraphLinkList;
    }

    /**
     * 合并两个子图
     * 上层 中层（关系） 下层关系连接
     * 只是部分的连接而已
     * 主要用于资源流动
     *
     * @param sourceSubgraph
     * @param targetSubgraph
     * @return
     */
    private HashMap<String, HashMap<String, LinkRestriction>> mergeSubgraphToGraph(HashMap<String, HashMap<String, LinkRestriction>> sourceSubgraph, HashMap<String, HashMap<String, LinkRestriction>> targetSubgraph) {
        if (sourceSubgraph == null) {
            return targetSubgraph;
        }
        //
        for (Map.Entry<String, HashMap<String, LinkRestriction>> edge : sourceSubgraph.entrySet()) {
            String startNodeId = edge.getKey();
            HashMap<String, LinkRestriction> endNodeLinkList = targetSubgraph.get(startNodeId);
            HashMap<String, LinkRestriction> endNodeValue = edge.getValue();
            if (endNodeLinkList != null) {
                //不存在重复的边的情况,关系内与关系外的连接
                endNodeLinkList.putAll(endNodeValue);
                targetSubgraph.put(startNodeId, endNodeLinkList);
                continue;
            }
            targetSubgraph.put(startNodeId, endNodeValue);
        }
        return targetSubgraph;
    }

    /**
     * 判断边是否存在
     * 判断图中是否有此边
     * 若有则返回此边的限制性条件
     * 判断重复边
     *
     * @param adjacencyMap
     * @param start
     * @param end
     * @return
     */
    private LinkRestriction edgeIsPresent(HashMap<String, HashMap<String, LinkRestriction>> adjacencyMap, String start, String end) {
        if (adjacencyMap.keySet().contains(start)) {
            HashMap<String, LinkRestriction> endNodeAndLinkRestriction = adjacencyMap.get(start);
            if (endNodeAndLinkRestriction.keySet().contains(end)) {
                return endNodeAndLinkRestriction.get(end);
            }
        }
        return null;
    }

    /**
     * 新建边，并将其存入图的链接表中
     * <p>
     * 方法中加入了重复边检测
     * 由于该功能的存在在新建的时候
     * 就非常灵活了，随便建
     *
     * @param start
     * @param end
     * @param linkRestriction
     * @param adjacencyMap
     * @return
     */
    private HashMap<String, HashMap<String, LinkRestriction>> addEdge(HashMap<String, HashMap<String, LinkRestriction>> adjacencyMap, String start, String end, LinkRestriction linkRestriction) {
        //将终点加到该起点的邻接表中
        HashMap<String, LinkRestriction> endNodeAndLinkRestriction = generateEdgeEndNode(adjacencyMap, start, end, linkRestriction);
        adjacencyMap.put(start, endNodeAndLinkRestriction);
        return adjacencyMap;
    }

    /**
     * 生成边
     * 单独写一个通用方法主要是为了判断重复边
     * 若有重复边则将限制性条件进行合并即可
     * <p>
     * 单个边的生成因为采用邻接表，还有个注意点
     * 同起点的处理
     *
     * @param adjacencyMap
     * @param start
     * @param end
     * @param linkRestriction
     * @return 返回起点的邻接表，如果有相同起点的情况，需要合并结束点(在generateEdgeEndNode方法中执行)
     */
    private HashMap<String, HashMap<String, LinkRestriction>> generateEdge(HashMap<String, HashMap<String, LinkRestriction>> adjacencyMap, String start, String end, LinkRestriction linkRestriction) {
        //生成该地点的邻接表的尾部节点
        HashMap<String, LinkRestriction> edgeEndNodeAndRestriction = generateEdgeEndNode(adjacencyMap, start, end, linkRestriction);
        HashMap<String, HashMap<String, LinkRestriction>> linkList = new HashMap<>();
        linkList.put(start, edgeEndNodeAndRestriction);
        return linkList;
    }

    /**
     * 生成起点邻接表
     * 0->[{1,weight},{3, weight}......]
     *
     * @param adjacencyMap    图的邻接表
     * @param start
     * @param end
     * @param linkRestriction
     * @return 该起点的尾节点们
     */
    private HashMap<String, LinkRestriction> generateEdgeEndNode(HashMap<String, HashMap<String, LinkRestriction>> adjacencyMap, String start, String end, LinkRestriction linkRestriction) {
        //判断边是否存在，如果边重复，则合并 linkRestriction 即可
        LinkRestriction oldRestriction = edgeIsPresent(adjacencyMap, start, end);
        LinkRestriction newLinkRestriction = linkRestriction;
        if (oldRestriction != null) {
            //边已经存在，合并限制性条件
            newLinkRestriction = mergeLinkProperties(linkRestriction, oldRestriction);
        }
        //判断是否起点是否连接多个点
        HashMap<String, LinkRestriction> endNodeAndRestriction;
        endNodeAndRestriction = adjacencyMap.get(start);
        if (endNodeAndRestriction == null) {
            //该节点未连接有边
            endNodeAndRestriction = new HashMap<>();
        }
        endNodeAndRestriction.put(end, newLinkRestriction);
        return endNodeAndRestriction;
    }


    /**
     * 资源的流动函数
     * <p>
     * 当前关系称为中间层
     * 还需要好几个函数
     * 不必关心上层的流动，因为上层的流动已经在新建协议的时候完成了
     * 只需要当前关系与可能当前关系作为连接点实现的上层与下层图的连接
     * 需要两个函数：
     * 1.获取与当前关系有联系的上层节点（关系中节点作为 endNode）
     * 2.关系内流动处理
     * 3.获取当前关系有联系的下层节点（关系中节点作为 startNode,应该是不多的）
     * 4.下层节点的流动
     * 只需要关注连接点
     *
     * @param adjacencyMap
     * @param nodeId
     * @return 返回是上层节点与下层节点 限制性条件在流动的时候再去拿吧
     */
    private ArrayList<String> checkThe(HashMap<String, HashMap<String, LinkRestriction>> adjacencyMap, String nodeId) {
        // 先判断连通到哪一些节点
        /*
        获取上层节点
         */
        return null;
    }



    /**
     * 节点间资源流动判断
     * 判断哪些资源可以流动
     * 这个方法可能还有些麻烦，就感觉
     * 限制性条件中的内容是放行条件，只需要满足其中一个条件即可放行
     * 资源因为字段不明，用限制性条件去 indexOf 可能会更好一一些
     *
     * @param activityResource
     * @param linkRestriction
     * @return 能流动的资源的id list,采用set, set 中元素不能重复
     */
    private HashSet<String> resFlowApprove(HashMap<String, String> activityResource, LinkRestriction linkRestriction) {
        HashSet<String> approvedResIdList = new HashSet<>();
        ResProtocol resProtocol = linkRestriction.getResProtocol();
        if (resProtocol.toString().equals("All")) return (HashSet<String>) activityResource.keySet();
        if (resProtocol.toString().equals("None")) return new HashSet<>();
        //这里需要修改
        ArrayList<String> types = linkRestriction.getTypes();
        for (Map.Entry<String, String> res : activityResource.entrySet()) {
            String resId = res.getKey();
            String resTagStr = res.getValue();
            HashMap<String, String> resTagMap = recoveryResTag(resTagStr);
            String type = resTagMap.get("type");
            if (types.contains(type)) {
                approvedResIdList.add(resId);
            }
        }
        return approvedResIdList;
    }

    private HashSet<String> resFlowApprove(String flowNodeId, LinkRestriction linkRestriction) {
        ActivityNode flowNode = nodeRepository.findById(flowNodeId).get();
        HashMap<String, String> activityResourceMap = flowNode.getResources();

        HashSet<String> approvedResIdSet = new HashSet<>();
        ResProtocol resProtocol = linkRestriction.getResProtocol();
        if (resProtocol.toString().equals("All")) return (HashSet<String>) activityResourceMap.keySet();
        if (resProtocol.toString().equals("None")) return new HashSet<>();
        //这里需要修改
        ArrayList<String> types = linkRestriction.getTypes();
        for (Map.Entry<String, String> res : activityResourceMap.entrySet()) {
            String resId = res.getKey();
            String resTagStr = res.getValue();
            HashMap<String, String> resTagMap = recoveryResTag(resTagStr);
            String type = resTagMap.get("type");
            if (types.contains(type)) {
                approvedResIdSet.add(resId);
            }
        }
        return approvedResIdSet;
    }


    /**
     * 判断用户或资源是否符合条件(适用于Resource)
     *
     * @param tagStr
     * @param linkRestriction
     * @return
     */
    private boolean resFlowApproveByTagStr(String tagStr, LinkRestriction linkRestriction) {
        HashMap<String, String> tagMap = recoveryResTag(tagStr);
        String type = tagMap.get("type");
        String suffix = tagMap.get("suffix");
        ArrayList<String> types = linkRestriction.getTypes();
        if (types.contains(type)) {
            return true;
        }
        return false;
    }

    /**
     * 节点间用户流动判断
     *
     * @param activityUser
     * @param linkRestriction
     * @return
     */
    private HashSet<String> userFlowApprove(HashMap<String, String> activityUser, LinkRestriction linkRestriction) {
        HashSet<String> approvedUserIdList = new HashSet<>();
        RoleProtocol roleProtocol = linkRestriction.getRoleProtocol();
        if (roleProtocol.toString().equals("All")) return (HashSet<String>) activityUser.keySet();
        if (roleProtocol.toString().equals("None")) return new HashSet<>();
        ArrayList<String> roles = linkRestriction.getRoles();
        ArrayList<String> organizations = linkRestriction.getOrganizations();
        ArrayList<String> domains = linkRestriction.getDomains();

        for (Map.Entry<String, String> user : activityUser.entrySet()) {
            String userId = user.getKey();
            String userTagStr = user.getValue();
            HashMap<String, ArrayList<String>> userTagMap = recoveryUserTag(userTagStr);
            ArrayList<String> role = userTagMap.get("role");
            if (checkTagContain(role, roles)) {
                approvedUserIdList.add(userId);
                continue;
            }
            ArrayList<String> domain = userTagMap.get("domain");
            if (checkTagContain(domain, domains)) {
                approvedUserIdList.add(userId);
                continue;
            }
            ArrayList<String> org = userTagMap.get("org");
            if (checkTagContain(org, organizations)) {
                approvedUserIdList.add(userId);
                continue;
            }
        }
        return approvedUserIdList;
    }


    /**
     * 单个用户流动判断
     * 拆分tag字符串
     * 如果不把构造的时候写好，读取的时候肯定会出错
     *
     * @param tagStr
     * @param linkRestriction
     * @return
     */
    private boolean userFlowApprove(String tagStr, LinkRestriction linkRestriction) {
        HashMap<String, ArrayList<String>> userTagMap = recoveryUserTag(tagStr);
        ArrayList<String> role = userTagMap.get("role");
        ArrayList<String> roles = linkRestriction.getRoles();
        if (checkTagContain(roles, role)) {
            return true;
        }
        ArrayList<String> domain = userTagMap.get("domain");
        ArrayList<String> domains = linkRestriction.getDomains();
        if (checkTagContain(domains, domain)) {
            return true;
        }
        ArrayList<String> org = userTagMap.get("org");
        ArrayList<String> organizations = linkRestriction.getOrganizations();
        if (checkTagContain(organizations, org)) {
            return true;
        }
        return false;
    }

    /**
     * 将构造的用户字符串还原
     *
     * @param userTagStr
     * @return
     */
    private HashMap<String, ArrayList<String>> recoveryUserTag(String userTagStr) {
        HashMap<String, ArrayList<String>> tagMap = new HashMap<>();
        String[] tags = userTagStr.split("O517");
        String roles = tags[0];
        String domains = tags[1];
        String organizations = tags[2];

        String[] roleArray = roles.split("\\.");
        String[] domainArray = domains.split("\\.");
        String[] orgArray = organizations.split("\\.");

        ArrayList<String> roleList = Lists.newArrayList(roleArray);
        ArrayList<String> domainList = Lists.newArrayList(domainArray);
        ArrayList<String> orgList = Lists.newArrayList(orgArray);

        tagMap.put("role", roleList);
        tagMap.put("domain", domainList);
        tagMap.put("org", orgList);
        return tagMap;
    }


    /**
     * （资源）从标签字符串恢复层单个标签
     *
     * @param resTagStr
     * @return
     */
    private HashMap<String, String> recoveryResTag(String resTagStr) {
        HashMap<String, String> resTag = new HashMap<>();
        String[] tagArray = resTagStr.split("O517");
        resTag.put("type", tagArray[0]);
        resTag.put("suffix", tagArray[1]);
        return resTag;
    }

    /**
     * 检查两个list中是否有重复项
     *
     * @param source
     * @param target
     * @return
     */
    private boolean checkTagContain(ArrayList source, ArrayList target) {
        int originalSize = source.size();
        source.removeAll(target);
        if (originalSize != source.size()) {
            return true;
        }
        return false;
    }

    /**
     * 判断用户是否可以流动到此节点
     * 实现逻辑
     * 1. 查询有此用户的节点（写成一个方法，尽量将操作都拆分成原子操作，万一后面就要多线程分布式了喃）
     * 2. 查找有此用户的节点到当前节点的路径(写成一个方法，可能这个相对麻烦一点，但也不存在嘛)
     * 3. 根据此路径走嘛，看走得到不嘛
     * 如果走得通喃，就直接可以直接加入
     * 走不通喃，就申请加入嘛
     *
     * @param nodeId
     * @param userId
     * @param graphId
     * @return
     */
    private boolean userIsBeRefuse(String nodeId, String userId, String graphId) {
        return true;
    }


    /**
     * 文件上传的自动更新
     * 这个应该是这一块内容最麻烦的地方了
     * 实现逻辑
     * 1. 查询与此节点来连通的节点（深度/广度搜索？？？或者我直接递归进去就完事了可能还简单一些）
     * 2. 依次判断每条边是否拦得住这个资源，限制不住就放行
     *
     * @param nodeId
     * @param graphId
     * @param uid
     * @return
     */
    private Object resFlowAutoUpdate(String nodeId, String graphId, String uid) {

        return null;
    }

    /**
     * 获取与此节点连通的节点及它的边
     * 直接递归就可以
     * 可以先看一下深度、广度搜索
     *
     * @param adjacencyMap
     * @param nodeId
     * @return
     */
    private HashMap<String, HashMap<String, LinkRestriction>> getConnectedNode(HashMap<String, HashMap<String, LinkRestriction>> adjacencyMap, String nodeId) {
        HashMap<String, HashMap<String, LinkRestriction>> connectionLinkMap = new HashMap<>();

        return connectionLinkMap;
    }


    /**
     * 根据子图关系，构建邻接表
     *
     * @param nodeIdList
     * @param type
     * @return
     */
    private HashMap<String, HashMap<String, LinkRestriction>> createSubgraphByRelation(ArrayList<String> nodeIdList, String type, LinkRestriction linkRestriction) {
        // return createSubgraphByRelation(nodeIdList, type, linkRestriction, null);
        return null;
    }


    /**
     * @param adjacencyMap 图的邻接表
     * @param nodeList     子图节点列表
     *                     似乎不用子图关系，干脆直接将关系内流动给整个拆分出来算了
     * @return
     */
    private HashMap<String, HashMap<String, LinkRestriction>> getRelevantLowerNode(HashMap<String, HashMap<String, LinkRestriction>> adjacencyMap, ArrayList<String> nodeList) {
        HashMap<String, HashMap<String, LinkRestriction>> lowerNodeAndRestriction = new HashMap<>();
        return null;
    }


    /**
     * 合并重复边的限制性条件
     * 先使用笨方法进行处理
     * 后续改用反射吧。。。
     * 终于有能用到反射的地方了，giao
     * <p>
     * 为什么要使用并喃？有点忘了😀
     * 这里还可以进行拓展，再在外面封装一层可以输入字段参数的部分
     * <p>
     * 以旧的规则为基准，将 source 内容合并到target 中
     *
     * @param source
     * @param target
     * @return
     */
    private LinkRestriction mergeLinkProperties(LinkRestriction source, LinkRestriction target) {
        // 获取用户限制性操作模式
        String sourceRoleProtocol = source.getRoleProtocol().toString();
        String targetRoleProtocol = target.getRoleProtocol().toString();

        /*
        有以下几种情况
        1. target 为 None
         */
        if (targetRoleProtocol.equals("All") && sourceRoleProtocol.equals("None")) {
            target.setRoleProtocol(RoleProtocol.valueOf("None"));
        } else if (targetRoleProtocol.equals("Constraints") && (sourceRoleProtocol.equals("None") || sourceRoleProtocol.equals("All"))) {
            target.setRoleProtocol(RoleProtocol.valueOf(sourceRoleProtocol));
        } else if (targetRoleProtocol.equals("Constraints") && sourceRoleProtocol.equals("Constraints")) {
            //用户三个部分合并
            ArrayList<String> sourceDomains = source.getDomains();
            ArrayList<String> sourceRoles = source.getRoles();
            ArrayList<String> sourceOrganizations = source.getOrganizations();

            ArrayList<String> targetDomains = target.getDomains();
            ArrayList<String> targetRoles = target.getRoles();
            ArrayList<String> targetOrganizations = target.getOrganizations();

            ArrayList<String> domains = mergeStringList(sourceDomains, targetDomains);
            ArrayList<String> roles = mergeStringList(sourceRoles, targetRoles);
            ArrayList<String> organizations = mergeStringList(sourceOrganizations, targetOrganizations);

            //  资源部分限制条件合并
            ArrayList<String> sourceFormats = source.getFormats();
            ArrayList<String> sourceReferences = source.getReferences();
            ArrayList<String> sourceScales = source.getScales();

            ArrayList<String> targetFormats = target.getFormats();
            ArrayList<String> targetReferences = target.getReferences();
            ArrayList<String> targetScales = target.getScales();

            ArrayList<String> format = mergeStringList(sourceFormats, targetFormats);
            ArrayList<String> references = mergeStringList(sourceReferences, targetReferences);
            ArrayList<String> scales = mergeStringList(sourceScales, targetScales);

            target.setDomains(domains);
            target.setRoles(roles);
            target.setOrganizations(organizations);
            target.setFormats(format);
            target.setReferences(references);
            target.setScales(scales);
        }
        return target;
    }

    /**
     * 合并两个List
     * 并且去除其中重复的部分
     *
     * @param source
     * @param target
     * @return
     */
    private ArrayList<String> mergeStringList(ArrayList<String> source, ArrayList<String> target) {
        if (source == null) {
            source = new ArrayList<>();
        }
        if (target == null) {
            target = new ArrayList<>();
        }
        //去掉重复项
        target.removeAll(source);
        // 合并
        target.addAll(source);
        return target;
    }

    /**
     * 通过 activityId 构造 Node user 部分内容
     * <p>
     * 注意 null 处理
     * 被用于构造 activityNode
     *
     * @param aid
     * @return
     */
    private HashMap<String, String> getActivityUserTag(String aid) {
        //从活动中读取成员，然后去用户服务器拿成员的tag(domain, organization)
        Optional<Activity> byId = activityRepository.findById(aid);
        if (!byId.isPresent()) {
            return null;
        }
        HashMap<String, String> userTag = new HashMap<>();
        Activity activity = byId.get();
        JSONArray members = activity.getMembers();
        for (int i = 0; i < members.size(); i++) {
            JSONObject member = (JSONObject) members.get(i);
            String userId = member.getString("userId");
            String role = member.getString("role");
            //通过 userId 从用户服务器获取用户对应的字段内容(目前是 domain & organization)
            String tagStr = setUserTag(userId, role);
            userTag.put(userId, tagStr);
        }
        return userTag;
    }

    /**
     * @param userId
     * @param role
     * @return 返回生成好的用户标签字符串
     */
    private String setUserTag(String userId, String role) {
        HashMap<String, String> userTagMap = getUserTagByIdFromUserServer(userId);
        String domain = userTagMap.get("domain");
        String organization = userTagMap.get("organization");
        return addFlagInTag(role, domain, organization);
    }

    /**
     * 从用户服务器获取用户 tag
     * 并将其转换为字符串
     *
     * @param userId
     * @return
     */
    private HashMap<String, String> getUserTagByIdFromUserServer(String userId) {
        String getTagUrl = "http://" + userServer + "/user/tag/" + userId;
        JSONObject userTagJson = restTemplate.getForObject(getTagUrl, JSONObject.class);
        JSONObject userTags = userTagJson.getJSONObject("data");
        JSONArray domain = userTags.getJSONArray("domain");
        String domainStr = jsonArrToString(domain, "#");
        JSONArray organization = userTags.getJSONArray("organization");
        String orgStr = jsonArrToString(organization, "#");
        HashMap<String, String> userTagMap = new HashMap<>();
        userTagMap.put("domain", domainStr);
        userTagMap.put("organization", orgStr);
        return userTagMap;
    }

    /**
     * JSONArray 转字符串
     *
     * @param jsonArray
     * @param splitSymbol 字符串分隔符
     * @return
     */
    private String jsonArrToString(JSONArray jsonArray, String splitSymbol) {
        String arrStr = "";
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                String item = (String) jsonArray.get(i);
                if (i != jsonArray.size() - 1) {
                    arrStr += item + splitSymbol;
                } else {
                    arrStr += item;
                }
            }
        }

        return arrStr;
    }

    /**
     * 获取 activity 中文件的id，以及对应的标签内容
     * 注意 null 的处理
     *
     * @param aid
     * @return
     */
    private HashMap<String, String> getActivityResourceTag(String aid) {
        //返回值不可能为 null
        ArrayList<ResourceEntity> files = activityResService.getAllFileInProject(aid);
        HashMap<String, String> fileTagMap = new HashMap<>();
        //如 size=0 则会跳过此处
        for (ResourceEntity file : files) {
            //只流动 public 的资源
            if (file.getPrivacy().equals("private")) {
                continue;
            }
            String fileTagStr = setResourceTag(file);
            String uid = file.getUid();
            fileTagMap.put(uid, fileTagStr);
        }
        return fileTagMap;
    }

    /**
     * 生成资源标签字符串
     * 后续统一资源限定条件后，就在此处进行修改即可
     * 正
     *
     * @param res
     * @return 资源的标签字符串
     */
    private String setResourceTag(ResourceEntity res) {
        String type = res.getType();
        String suffix = res.getSuffix();
        String resTagStr = addFlagInTag(type, suffix);
        return resTagStr;
    }

// ======================节点层面上的操作结束===============================================================================

    /**
     * 通过 activityId 设置节点
     * 在连接节点的时候调用
     * 实际不必要在节点的
     *
     * @param aid
     * @return
     */
    @Override
    public ActivityNode createActivityNode(String aid) {
        ActivityNode node = new ActivityNode();
        node.setId(aid);
        //获取该节点下用户与资源所包含的tag,value为id
        HashMap<String, String> activityUserTag = getActivityUserTag(aid);
        HashMap<String, String> activityResourceTag = getActivityResourceTag(aid);
        node.setMembers(activityUserTag);
        node.setResources(activityResourceTag);
        return nodeRepository.save(node);
    }

    /**
     * 更新节点
     * 只有更新用户或者资源
     * 应用场景
     * 只要是活动资源或用户有所变化的时候都需要进行处理
     *
     * @param aid           节点 id
     * @param userId        userId or uid
     * @param oldTag        旧的标签值(put操作时使用)
     * @param newTag        新的标签值（put操作时使用）
     * @param operationType insert, del and put
     * @return 节点的 member
     */
    private HashMap<String, String> putActivityNodeUser(String aid, String userId, String userRole, String operationType, String oldTag, String newTag) {
        //初始化用于存储用户标签,value 存userId
        Optional<ActivityNode> nodeRepositoryById = nodeRepository.findById(aid);
        ActivityNode node = nodeRepositoryById.get();
        if (!nodeRepositoryById.isPresent()) {
            //如果无此节点则新建此节点
            node = createActivityNode(aid);
        }
        //不可能为空
        HashMap<String, String> members = node.getMembers();

        if (operationType.equals("insert")) {
            //无需判断原本有无此用户，map 数据结构决定后续 push 的会覆盖前面同key的值
            String tagStr = setUserTag(userId, userRole);
            members.put(userId, tagStr);
        } else if (operationType.equals("del")) {
            members.remove(userId);
        } else if (operationType.equals("put")) {
            //更新，先验证是否有此用户，若无此用户则 warning
            if (!members.containsKey(userId)) {
                return null;
            }
            String userTag = members.get(userId);
            /*
            可能会出现问题
            不该替换的地方被替换掉
             */
            String replacedStr = userTag.replace(oldTag, newTag);
            members.put(userId, replacedStr);

        }
        return members;
    }

    /**
     * 节点中资源变化的修改
     * 只针对节点层面的操作
     * 不处理活动层面的内容
     * <p>
     * 资源流动的处理真挺麻烦的
     *
     * @param aid               节点id
     * @param res               资源
     * @param putUid            put 操作时仅需要传 uid 过来即可
     * @param oldTag(ArrayList) put 操作时会使用到旧的标签
     * @param newTag(ArrayList) put 操作时会使用到新的标签(两者一一对应)
     * @param operationType
     * @return 节点更新后的 resource 字段
     */
    private HashMap<String, String> putActivityNodeResource(String aid, ResourceEntity res, String operationType, String putUid, ArrayList<String> oldTag, ArrayList<String> newTag) {
        //初始化用于存储用户标签,value 存userId
        Optional<ActivityNode> nodeRepositoryById = nodeRepository.findById(aid);
        ActivityNode node = nodeRepositoryById.get();
        if (!nodeRepositoryById.isPresent()) {
            //如果无此节点则新建此节点
            node = createActivityNode(aid);
        }
        // 资源的流动，实际上是对 node 中 resource 字段的处理
        HashMap<String, String> resourceTagMap = node.getResources();
        if (operationType.equals("insert")) {
            /*
            addResource 需要的参数,针对节点层面的操作
            resourceEntity(uid,限制性标签内容)
            附加在活动层面的操作放在更高层次一点的方法中
             */
            String uid = res.getUid();
            String resTag = setResourceTag(res);
            resourceTagMap.put(uid, resTag);
        } else if (operationType.equals("del")) {
            /*
            是否需要处理活动层面的内容？ 不需要
            活动中的资源自有自己的一套管理方法，节点的处理，就不用管活动中的逻辑了
            整个节点的操作过程更相对更加底层一点，相对于活动的操作
            只需要 uid 将条目删除即可
             */
            resourceTagMap.remove(putUid);
        } else if (operationType.equals("put")) {
            /*
            将原限制条目取出来，进行修改
            uid,(oldTag,newTag)一一对应
             */
            String resTagStr = resourceTagMap.get(putUid);
            /*
            这样写的话仅仅支持单个条件的修改，当一次性修改多个的时候会有问题
            这样的话，可能需要我再上层进行封装，或者修改此处的代码
            这里的问题等待和前端进行调试的时候再来确认
             */
            String replacedResTagStr = resTagStr;
            for (int i = 0; i < oldTag.size(); i++) {
                replacedResTagStr = replacedResTagStr.replace(oldTag.get(i), newTag.get(i));
            }
            resourceTagMap.put(putUid, replacedResTagStr);
        }
        return resourceTagMap;
    }

    /**
     * 在节点中添加资源
     * 因适用场景不同 重载了两个添加函数
     * 1。本节点添加资源
     * 从活动层往下走
     * 2。流动到此节点的资源
     *
     * @param aid
     * @param res
     * @return
     */
    private HashMap<String, String> addResource(String aid, ResourceEntity res) {
        /*
        1. 添加到当前节点
        2. 获得连通的节点
        3. 遍历将资源流动过去
         */
        // HashMap<String, String> nodeRes = putActivityNodeResource(aid, res, "insert", null, null, null);
        // getRelevantNode();
        // addResource(aid);
        return null;
    }

    /**
     * 流动资源的添加
     * 资源是流动过去的
     * 所以也需要操作活动层的内容
     * 先将资源存入活动层，然后再操作节点层的内容
     *
     * @param startAid 资源来源
     * @param endAid   当前节点 id
     * @param uid      资源id(资源来源节点出获取)
     * @return 当前节点的资源标签
     */
    private HashMap<String, String> addResource(String startAid, String endAid, String uid) {
        //1.在源节点中查询得到文件信息
        ResourceEntity res = activityResService.getFileById(startAid, uid);
        //2.将资源放在当前节点中
        ResourceEntity flowFolder = activityResService.getFlowFolder(endAid, "Flowing Resources");
        String flowFolderId;
        //若无此文件夹则生成此文件夹
        if (flowFolder == null) {
            flowFolder = new ResourceEntity();
            flowFolderId = UUID.randomUUID().toString();
            flowFolder.setUid(flowFolderId);
            flowFolder.setName("Flowing Resources");
            flowFolder.setPrivacy("public");
            flowFolder.setParent("0");
            flowFolder.setChildren(new ArrayList<>());
        }
        flowFolderId = flowFolder.getUid();
        ArrayList<ResourceEntity> flowFolderChildren = flowFolder.getChildren();
        //将资源放入 Flowing Resources 中
        res.setParent(flowFolderId);
        if (flowFolderChildren == null) flowFolderChildren = new ArrayList<>();
        flowFolderChildren.add(res);
        flowFolder.setChildren(flowFolderChildren);
        activityResDao.addResource(flowFolder);
        //第二步：更新当前节点的资源标签
        return putActivityNodeResource(endAid, res, "insert", null, null, null);
    }

    /**
     * 节点间多资源流动
     * ps: uids.size() 等于 0
     * 说明无资源可以流动，则直接将endNodeResourceTagMap 返回来
     * 可优化的地方：
     * 如果没有资源需要流动，那就没必要更新了
     * 代码整洁点更好，这一次 put 就不节约了
     *
     * @param startAid 流动的节点
     * @param endAid   接收资源的点
     * @param uids     流动资源的 id
     * @return 终点的资源map
     */
    private HashMap<String, String> addResourceToNode(String startAid, String endAid, HashSet<String> uids) {
        // uids 为空，直接将 endNode 的 resourceTagMap返回回来即可
        ActivityNode endNode = nodeRepository.findById(endAid).get();
        HashMap<String, String> endNodeResourcesTagMap = endNode.getResources();
        if (uids.size() == 0) {
            return endNodeResourcesTagMap;
        }
        //更新活动层内容
        ArrayList<ResourceEntity> approvedFilesEntity = activityResService.getFilesByIds(startAid, uids);
        //获取流动资源文件夹，该文件夹位于项目的根目录，可以直接 query
        ResourceEntity flowFolder = activityResService.getFlowFolder(endAid, "Flowing Resources");
        if (flowFolder == null) {
            flowFolder = new ResourceEntity();
            flowFolder.setUid(UUID.randomUUID().toString());
            flowFolder.setName("Flowing Resources");
            flowFolder.setPrivacy("public");
            flowFolder.setParent("0");
            flowFolder.setChildren(new ArrayList<>());
        }
        String flowFolderUid = flowFolder.getUid();
        ArrayList<ResourceEntity> flowFolderChildren = flowFolder.getChildren();
        if (flowFolderChildren == null) flowFolderChildren = new ArrayList<>();
        //key 存 id, value 存对应的tag
        HashMap<String, String> flowResourceTagMap = new HashMap<>();
        ArrayList<String> flowFolderFileIds = new ArrayList<>();
        //重复资源的处理
        for (ResourceEntity item : flowFolderChildren) {
            flowFolderFileIds.add(item.getUid());
        }
        for (ResourceEntity approvedFile : approvedFilesEntity) {
            String uid = approvedFile.getUid();
            /*
            节约一个循环
            在这里获取用户资源的 resourceTagMap
             */
            String resourceTag = setResourceTag(approvedFile);
            flowResourceTagMap.put(uid, resourceTag);

            approvedFile.setParent(flowFolderUid);
            //不用去重，直接覆盖，后来的覆盖掉先来的
            int index = flowFolderFileIds.indexOf(uid);
            if (index != -1) {
                //则说明有此资源，则用后来的资源替换掉
                flowFolderChildren.remove(index);
                flowFolderChildren.add(approvedFile);
                continue;
            }
            flowFolderChildren.add(approvedFile);
        }
        flowFolder.setChildren(flowFolderChildren);
        //活动层内容更新成功
        activityResDao.addResource(flowFolder);

        //putAll 同 key 去掉重复的
        endNodeResourcesTagMap.putAll(flowResourceTagMap);
        return endNodeResourcesTagMap;
    }


    private HashMap<String, String> delResource(String aid, String uid) {
        return putActivityNodeResource(aid, null, "del", uid, null, null);
    }

    private HashMap<String, String> putResource(String aid, JSONObject putInfo) {
        //在 controller 层面使用resourceDto 进行接收，然后处理成这边需要的结构即可
        String putUid = putInfo.getString("uid");
        JSONArray oldTag = putInfo.getJSONArray("oldTag");
        JSONArray newTag = putInfo.getJSONArray("newTag");
        ArrayList<String> oldTagList = Lists.newArrayList();
        ArrayList<String> newTagList = Lists.newArrayList();
        oldTag.forEach(item -> {
            String tempTag = (String) item;
            oldTagList.add(tempTag);
        });
        newTag.forEach(item -> {
            String tempTag = (String) item;
            newTagList.add(tempTag);
        });
        return putActivityNodeResource(aid, null, "put", putUid, oldTagList, newTagList);
    }

    /**
     * 流动用户加入
     * 1. 更新活动 member
     * 2. 更新节点
     * 用户流动过来是什么 role？
     * 采用默认的 role
     *
     * @param aid
     * @param userId
     * @return
     */
    private HashMap<String, String> addUser(String aid, String userId) {
        //更新活动中的 member
        Activity activity = activityRepository.findById(aid).get();
        JSONArray members = activity.getMembers();
        JSONObject member = new JSONObject();
        member.put("role", "ordinary-member");
        member.put("userId", userId);
        members.add(member);
        activity.setMembers(members);
        activityRepository.save(activity);
        //肯定是有关系才有流动，有关系，肯定就有这个节点
        return putActivityNodeUser(aid, userId, "ordinary-member", "insert", null, null);
    }

    private HashMap<String, String> delUser(String aid, String userId) {

        return null;
    }

    /**
     * role 可能会变得相对较多
     * 用户这个也是真的麻烦
     * 用户在个人空间修改了自己的个人信息
     * 还需要更新节点中的信息，omg 这里也是挺麻烦的
     * 不过，可以写一个更新方法，当第一次使用到的时候set一下 user 即可
     *
     * @param aid
     * @param putInfo
     * @return
     */
    private HashMap<String, String> putUser(String aid, JSONObject putInfo) {

        return null;
    }


// ======================节点层面上的操作结束===============================================================================

    /**
     * 给限定条件字符串添加分割标记
     * 给每一类条件添加分割标记
     * 每一类的分割标记 0(不是零)517
     *
     * @param strings
     * @return
     */
    private String addFlagInTag(String... strings) {
        String tagStr = "";
        for (String tag : strings) {
            tagStr += tag + "O517";
        }
        return tagStr;
    }

    /**
     * 判断当前节点是否已经存在于图中
     * 无的话则返回为空
     *
     * @param aid
     * @return
     */
    private ActivityNode nodeIsPresent(String aid) {
        Optional<ActivityNode> byId = nodeRepository.findById(aid);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }

    /**
     * 获取指定节点的所有下层路径，用于资源流动
     * 资源流动不会往上层流动
     * <p>
     * 直接递归进去
     * 返回结果的组织形式
     * 按层次结构存储
     * 0->[{1,a},{2,b}]
     * 2->[{4,c},{5,d}]
     * 还得添加一个资源流动函数
     * 返回能到达的节点列表
     *
     * @param graphId
     * @param nodeId
     * @return
     */
    private Stack<Stack<HashMap<String, LinkRestriction>>> getRelevantNodeRoute(String graphId, String nodeId) {
        Optional<ActivityGraph> byId = graphRepository.findById(graphId);
        if (!byId.isPresent()) {
            return null;
        }
        ActivityGraph activityGraph = byId.get();
        HashMap<String, HashMap<String, LinkRestriction>> adjacencyMap = activityGraph.getAdjacencyMap();
        /*
        将第一层拼接好后，然后再进去递归完后拼接，每一层都往endNode的hashMap中去存
        0->[{1,a},{2,b},{3,c},{5,b}] 这就是从0到5的第一条路径
        不能同 key，所以把起点放在 value  中，然后进行存储即可
         */
        //初始化用于存储路径
        Stack<Stack<HashMap<String, LinkRestriction>>> pathStack = new Stack<>();
        Stack<HashMap<String, LinkRestriction>> nodeStack = new Stack<>();
        return depthFirstSearch(adjacencyMap, nodeId, nodeStack, pathStack);
    }


    /**
     * 深度遍历该节点能到达的所有路
     *
     * @param adjacencyMap
     * @param startId
     * @param pathStack
     * @param path
     * @return 有多条路径 key路径
     * bug: 环的处理
     */
    private Stack<Stack<HashMap<String, LinkRestriction>>> depthFirstSearch(HashMap<String, HashMap<String, LinkRestriction>> adjacencyMap,
                                                                            String startId,
                                                                            Stack<HashMap<String, LinkRestriction>> pathStack,
                                                                            Stack<Stack<HashMap<String, LinkRestriction>>> path
    ) {
        //起点不用管,有肯定就不为空
        HashMap<String, LinkRestriction> endNode = adjacencyMap.get(startId);
        if (endNode != null) {
            for (Map.Entry<String, LinkRestriction> item : endNode.entrySet()) {
                String nextStartId = item.getKey();
                LinkRestriction linkRestriction = item.getValue();
                //到这一层了，入栈
                HashMap<String, LinkRestriction> endNodeAndRestriction = new HashMap<>();
                endNodeAndRestriction.put(nextStartId, linkRestriction);
                pathStack.push(endNodeAndRestriction);

                Stack<Stack<HashMap<String, LinkRestriction>>> stacks = depthFirstSearch(adjacencyMap, nextStartId, pathStack, path);
                path = stacks;
            }
        } else {
            //到达遍历的终点
            //返回上层,将这层节点出栈
            pathStack.pop();
            //将路径入库
            path.push(pathStack);
        }
        return path;
    }

    /**
     * 获取两点之间的所有路径
     *
     * @param graphId
     * @param startId
     * @param endId
     * @return
     */
    private Stack<Stack<HashMap<String, LinkRestriction>>> getRelevantNodeRoute(String graphId, String startId, String endId) {
        Stack<Stack<HashMap<String, LinkRestriction>>> stacks = new Stack<>();
        Optional<ActivityGraph> byId = graphRepository.findById(graphId);
        if (!byId.isPresent()) {
            return null;
        }
        ActivityGraph activityGraph = byId.get();
        HashMap<String, HashMap<String, LinkRestriction>> adjacencyMap = activityGraph.getAdjacencyMap();
        Stack<Stack<HashMap<String, LinkRestriction>>> pathStack = new Stack<>();
        Stack<HashMap<String, LinkRestriction>> nodeStack = new Stack<>();
        return gRelevantNode(adjacencyMap, startId, endId, nodeStack, pathStack);
    }

    private Stack<Stack<HashMap<String, LinkRestriction>>> gRelevantNode(HashMap<String, HashMap<String, LinkRestriction>> adjacencyMap,
                                                                         String startId,
                                                                         String endId,
                                                                         Stack<HashMap<String, LinkRestriction>> pathStack,
                                                                         Stack<Stack<HashMap<String, LinkRestriction>>> route
    ) {
        HashMap<String, LinkRestriction> endNodeAndRestriction = adjacencyMap.get(startId);
        if (endNodeAndRestriction != null) {
            //先遍历尾节点，看这一层是否有终点
            Set<String> endNodeIdSet = endNodeAndRestriction.keySet();
            for (String endNodeId : endNodeIdSet) {
                if (endNodeId.equals(endId)) {
                    //终点
                    LinkRestriction linkRestriction = endNodeAndRestriction.get(endNodeId);
                    HashMap<String, LinkRestriction> hashMap = new HashMap<>();
                    hashMap.put(endNodeId, linkRestriction);
                    pathStack.push(hashMap);
                    route.push(pathStack);
                    pathStack.pop();
                    return route;
                }
            }
            //还未达到终点
            for (Map.Entry<String, LinkRestriction> item : endNodeAndRestriction.entrySet()) {
                LinkRestriction linkRestriction = item.getValue();
                String startNodeId = item.getKey();
                // 设置好了，将其返回
                gRelevantNode(adjacencyMap, startNodeId, endId, pathStack, route);
            }
        } else {
            //退出，进入下一层的下一个
            pathStack.pop();
        }
        return route;
    }


    /**
     * 获得可以流动的节点
     * <p>
     * 可优化
     *
     * @param paths
     * @param linkRestriction
     * @param resApproveMap
     * @return
     */
    private HashMap<String, HashSet<String>> getFlowNode(Stack<Stack<HashMap<String, LinkRestriction>>> paths, LinkRestriction linkRestriction, HashMap<String, String> resApproveMap) {
        HashMap<String, HashSet<String>> flowNodeList = new HashMap<>();
        /*
        初始化资源可到达的数据结构，用于存储 key 为resId, value为可以到达的节点
        key 在resId 前面添加是否能继续向深处走的标记
        wx(yes),xw(no)
        T(yes), F(no)
         */
        Set<String> resIds = resApproveMap.keySet();
        //生成标记字符串
        int pathNum = paths.size();
        int pathNumTemp = paths.size();
        String flags = "";
        while (pathNumTemp != 0) {
            pathNumTemp--;
            flags += "T";
        }
        for (String resId : resIds) {
            //初始化的时候当然是所有的资源都是yes状态
            resId = flags + resId;
            flowNodeList.put(resId, new HashSet<>());
        }
        /*
        开始遍历每一条路径，判断能到达的节点，需要去重
        优化点：不用走相同的路
         */
        for (int i = 0; i < paths.size(); i++) {
            //获得第 i 条路径
            Stack<HashMap<String, LinkRestriction>> path = paths.pop();
            for (int j = 0; j < path.size(); j++) {
                //第 i 条路径的第 j 个节点
                HashMap<String, LinkRestriction> nodeInPath = path.get(j);
                LinkRestriction edgeRestriction = new LinkRestriction();
                String flowNodeId = "";
                //获取该边的节点以及限制性条件
                for (Map.Entry<String, LinkRestriction> item : nodeInPath.entrySet()) {
                    flowNodeId = item.getKey();
                    edgeRestriction = item.getValue();
                }
                //遍历所有资源，判断是能够通过
                for (Map.Entry<String, String> restriction : resApproveMap.entrySet()) {
                    String resId = restriction.getKey();
                    Set<String> resIdAndFlags = flowNodeList.keySet();
                    String resIdAndFlag = "";
                    String flag = "";
                    String tempFlags = "";
                    for (String item : resIdAndFlags) {
                        String tempResId = item.substring(pathNum);
                        if (tempResId.equals(resId)) {
                            //获取此资源 此条路径的通路情况
                            flag = item.substring(i, i + 1);
                            //将该资源的标记字符串取出
                            tempFlags = item.substring(0, pathNum);
                            //获取此资源含有通路标记对的resId
                            resIdAndFlag = item;
                        }
                    }
                    //该资源的此路径已断
                    if (flag.equals("F")) {
                        continue;
                    }
                    HashSet<String> canFlowNodeSet = flowNodeList.get(resIdAndFlag);
                    //已经到达过该节点，不再判断
                    if (canFlowNodeSet.contains(resId)) {
                        continue;
                    }
                    String resTagStr = restriction.getValue();
                    //若能通过，则将节点id，放到对应的资源的节点中（注意去重）
                    if (resFlowApproveByTagStr(resTagStr, edgeRestriction)) {
                        canFlowNodeSet.add(flowNodeId);
                    } else {
                        //不能通过，将此路径标记替换为 F
                        flowNodeList.remove(resIdAndFlag);
                        StringBuilder builder = new StringBuilder();
                        builder.append(tempFlags);
                        builder.replace(i, i + 1, "F");
                        flowNodeList.put(builder.toString(), canFlowNodeSet);
                    }
                }
            }
        }
        return flowNodeList;
    }

}
