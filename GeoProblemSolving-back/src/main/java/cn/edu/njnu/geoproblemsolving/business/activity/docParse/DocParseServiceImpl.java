package cn.edu.njnu.geoproblemsolving.business.activity.docParse;

import cn.edu.njnu.geoproblemsolving.business.activity.entity.Project;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Template.workflow.Operation;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Template.workflow.OperationRes;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Template.workflow.ProjectInfo;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Template.workflow.WorkflowTemplate;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.ActivityType;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ActivityRepository;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ProjectRepository;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.SubprojectRepository;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Activity;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.ActivityDoc;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.DocPerson;
import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.service.UserDispatch;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ActivityDocRepository;
import cn.edu.njnu.geoproblemsolving.business.collaboration.entity.MsgRecords;
import cn.edu.njnu.geoproblemsolving.business.tool.generalTool.entity.Tool;
import cn.edu.njnu.geoproblemsolving.business.user.dao.UserDao;
import cn.edu.njnu.geoproblemsolving.business.user.entity.UserEntity;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.dom4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @ClassName DocParseService
 * @Description 服务层与其他部分进行耦合
 * @Author zhngzhng
 * @Date 2021/11/22
 **/
@Service
public class DocParseServiceImpl implements DocParseServe {
    private final Logger LOGGER = LoggerFactory.getLogger(DocParseServiceImpl.class);

    @Autowired
    ActivityDocRepository docRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    SubprojectRepository subprojectRepository;

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    UserDao userDao;

    @Autowired
    UserDispatch userDispatch;

    private ActivityDoc operatingDoc;

    private Document loadXML(String aid) {
        Optional<ActivityDoc> byId = docRepository.findById(aid);
        if (!byId.isPresent()) {
            LOGGER.warn("Failed to load document: No such activity document, id ---> " + aid);
            return null;
        }
        operatingDoc = byId.get();
        try {
            return DocumentHelper.parseText(operatingDoc.getDocument());
        } catch (DocumentException e) {
            LOGGER.error("Failed to parse document string.");
            e.printStackTrace();
            return null;
        }
    }

    private void saveXml(String docString) {
        if (operatingDoc == null) return;
        operatingDoc.setDocument(docString);
        docRepository.save(operatingDoc);
        operatingDoc = null;
    }

    private HashSet<DocPerson> setDocPerson(JSONArray members) {
        try {
            HashSet<DocPerson> docPersons = new HashSet<>();
            for (Iterator<Object> it = members.iterator(); it.hasNext(); ) {
                DocPerson member = new DocPerson();
                JSONObject memberJson = JSONObject.parseObject(JSONObject.toJSONString(it.next()));
                String userId = memberJson.getString("userId");
                member.setUserId(userId);
                member.setRole(memberJson.getString("role"));
                UserEntity user = userDao.findUserByIdOrEmail(userId);
                if (user == null) {
                    LOGGER.warn("Failed to init activity document: No such creator, id " + userId);
                    return null;
                }
                member.setEmail(user.getEmail());
                member.setName(user.getName());
                ArrayList<String> domain = user.getDomain();
                HashSet<String> domainSet = new HashSet<>();
                ArrayList<String> organizations = user.getOrganizations();
                HashSet<String> orgSet = new HashSet<>();
                if (domain != null) domainSet = (HashSet<String>) domain.stream().collect(Collectors.toSet());
                if (organizations != null)
                    orgSet = (HashSet<String>) organizations.stream().collect(Collectors.toSet());
                member.setDomain(domainSet);
                member.setOrganization(orgSet);

                docPersons.add(member);
            }
            return docPersons;
        } catch (NullPointerException e) {
            LOGGER.error("Failed to set document person: members is null.");
            throw e;
        }
    }


    @Override
    public void initActivityDoc(Activity activity) {
        HashSet<DocPerson> docPeoples = setDocPerson(activity.getMembers());
        if (docPeoples == null) return;
        Iterator<DocPerson> it = docPeoples.iterator();
        if (it.hasNext()) {
            String xmlStr = DocInterpret.initActivityDoc(activity, it.next());
            ActivityDoc activityDoc = new ActivityDoc();
            activityDoc.setAid(activity.getAid());
            activityDoc.setDocument(xmlStr);

            docRepository.insert(activityDoc);
        }

        return;
    }

    @Override
    public Integer changeActivityType(String aid, Activity activity) {
        Document docXml = loadXML(aid);
        if (docXml == null) return null;
        Element activityEle = docXml.getRootElement();
        activityEle.clearContent();
        String type = activity.getType().toString();
        activityEle.attribute("type").setValue(type);
        HashSet<DocPerson> docPeoples = setDocPerson(activity.getMembers());
        DocInterpret.initActivityDocContent(activityEle, activity, docPeoples);
        saveXml(docXml.asXML());
        return 0;
    }

    @Override
    public Object userJoin(String aid, String userId) {
        //Acquire user information from user server.
        JSONObject userTag = userDispatch.getUserTag(userId);
        if (userTag == null) return null;
        HashSet<String> domains = userTag.getObject("domain", HashSet.class);
        HashSet<String> organizations = userTag.getObject("organization", HashSet.class);
        UserEntity user = userDao.findUserByIdOrEmail(userId);
        DocPerson docPerson = new DocPerson();
        docPerson.setRole("ordinary-member");
        docPerson.setName(user.getName());
        docPerson.setEmail(user.getEmail());
        docPerson.setUserId(userId);
        docPerson.setDomain(domains);
        docPerson.setOrganization(organizations);

        Document activityDocXml = loadXML(aid);
        DocInterpret.appendParticipant(activityDocXml, docPerson);
        saveXml(activityDocXml.asXML());

        return null;
    }

    @Override
    public Object userJoin(String aid, HashSet<String> userIds) {
        if (userIds == null || userIds.isEmpty()) return null;
        JSONObject usersTag = userDispatch.getUsersTag(userIds);
        HashSet<DocPerson> docPeoples = new HashSet<>();
        for (String userId : userIds){
            UserEntity user = userDao.findUserByIdOrEmail(userId);
            if (user == null) continue;
            DocPerson docPerson = new DocPerson();
            docPerson.setUserId(userId);
            docPerson.setName(user.getName());
            docPerson.setEmail(user.getEmail());
            docPerson.setRole("ordinary-member");

            JSONObject userTag = usersTag.getJSONObject(userId);
            HashSet<String> domains = userTag.getObject("domain", HashSet.class);
            HashSet<String> organizations = userTag.getObject("organization", HashSet.class);

            docPerson.setDomain(domains);
            docPerson.setOrganization(organizations);

            docPeoples.add(docPerson);
        }

        Document docXml = loadXML(aid);
        DocInterpret.appendParticipant(docXml, docPeoples);
        saveXml(docXml.asXML());

        return null;
    }

    @Override
    public Object uploadResource(String aid, HashMap<String, String> resInfo) {
        return null;
    }

    @Override
    public ArrayList<HashMap<String, String>> uploadResource(String aid,
                                                             ArrayList<ResourceEntity> resList) {
        Document docXml = loadXML(aid);
        return DocInterpret.appendResource(docXml, resList, null);
    }

    @Override
    public Object resFlow(String fromAid, String endAid, HashSet<String> uids) {
        return null;
    }

    @Override
    public HashMap<String, String> resFlow(String fromAid, String endAid, String uid) {
        Document fromDocXml = loadXML(fromAid);
        Document endDocXml = loadXML(endAid);

        Element resEle = DocInterpret.getResElement(fromDocXml, uid);
        if (resEle == null) return null;
        Element endResEle = resEle.createCopy();
        DocInterpret.appendResource(endDocXml, endResEle);
        saveXml(endDocXml.asXML());
        return DocInterpret.resNode2Map(resEle);
    }

    @Override
    public ArrayList<HashMap<String, String>> getResInfo(String aid, HashSet<String> uids) {
        Document docXml = loadXML(aid);
        if (uids == null) return null;
        ArrayList<HashMap<String, String>> resInfos = new ArrayList<>();
        for (String uid : uids){
            HashMap<String, String> resInfo = DocInterpret.getResInfo(docXml, uid);
            if (resInfo != null) resInfos.add(resInfo);
        }
        return resInfos;
    }

    @Override
    public HashMap<String, String> getResInfo(String aid, String uid) {
        Document docXml = loadXML(aid);
        return DocInterpret.getResInfo(docXml, uid);
    }

    @Override
    public ArrayList<HashMap<String, String>> getResInfo(String aid) {
        Document docXml = loadXML(aid);
        return DocInterpret.getResAllResInfo(docXml);
    }

    @Override
    public void appendChildActivity(String aid, String childId, String name, String creator) {
        Document docXml = loadXML(aid);
        DocInterpret.appendChild(docXml, childId, name, creator);
        saveXml(docXml.asXML());
    }

    @Override
    public Object addTool(String aid, List<Tool> tools) {
        Document docXml = loadXML(aid);
        DocInterpret.appendTool(docXml, tools);
        saveXml(docXml.asXML());
        return null;
    }

    @Override
    public Object removeTool(String aid, String tid) {
        Document docXml = loadXML(aid);
        DocInterpret.removeTool(docXml, tid);
        saveXml(docXml.asXML());
        return null;
    }

    @Override
    public Object removeTool(String aid, HashSet<String> tids) {
        return null;
    }

    @Override
    public String messageRecord(String tid, MsgRecords msgRecords) {
        Document docXml = loadXML(msgRecords.getAid());
        DocInterpret.createCommunicationOperation(tid, msgRecords);
        saveXml(docXml.asXML());
        return null;
    }

    @Override
    public String geoAnalysis(String aid, String toolId,
                              HashSet<String> inResId, ArrayList<ResourceEntity> outRes,
                              HashSet<String> participants) {
        Document docXml = loadXML(aid);
        DocInterpret.appendResource(docXml, outRes, null);
        HashSet<String> outResId = new HashSet<>();
        for (ResourceEntity item : outRes){
            outResId.add(item.getUid());
        }
        HashMap<String, String> toolInfo = DocInterpret.getToolInfo(docXml, toolId);
        String purpose = toolInfo.get("function");
        String oid = DocInterpret.appendGeoAnalysisOperation(docXml, toolId, participants, purpose, inResId, outResId);
        saveXml(docXml.asXML());
        return oid;
    }

    @Override
    public String geoAnalysisRLS90(String aid,
                                   String toolId,
                                   HashSet<String> onlineMembers,
                                   String purpose,
                                   ResourceEntity input,
                                   ResourceEntity output) {
        Document docXml = loadXML(aid);
        DocInterpret.appendResource(docXml, output, null);
        HashSet<String> inResId = new HashSet<>();
        HashSet<String> outResId = new HashSet<>();
        inResId.add(input.getUid());
        outResId.add(output.getUid());
        DocInterpret.appendGeoAnalysisOperation(docXml, toolId, onlineMembers, purpose, inResId, outResId);
        saveXml(docXml.asXML());
        return null;
    }

    /**
     * 活动文档生成工作流模板
     * 活动文档 ---> 工作流模板: 参与式平台的实现方式
     *
     * 具体面向的对象应该是参与式地理分析过程
     *
     * 加入部分内容
     * @param aid
     * @return
     */
    @Override
    public Object toWorkflowTemplate(String aid) {
        Document docXml = loadXML(aid);
        Optional<Project> byId = projectRepository.findById(aid);
        if (!byId.isPresent()){
            LOGGER.warn("No such project, activity id is ---> " + aid);
            return null;
        }
        Project project = byId.get();
        //工作流模板中地理问题描述
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setAid(aid);
        projectInfo.setDescription(project.getDescription());
        projectInfo.setTag(project.getTag());
        WorkflowTemplate workflowTemplate = new WorkflowTemplate();
        workflowTemplate.setId(aid);
        workflowTemplate.setProjectInfo(projectInfo);
        workflowTemplate.setResVisible(true);
        workflowTemplate.setName("test");
        int level = 0;
        WorkflowTemplate template = a2WorkflowTemplate(docXml, workflowTemplate, level);

        return template;
        /*
        工具需可见，资源可不可见
        将八个步骤也给放进去
        {
            name: String,
            resVisible: boolean,
            processStep: String,
            //地理问题的简单描述
            projectInfo: {
                aid: string,
                description: string,
                tag: sting
            },
            //Geo-operator
            operations: [
            //输入无对应输入时则增加对应input的输入
                {
                oid: string,
                //模型
                toolId: string,
                description: string,
                //数据
                input: [
                    {
                        uid: string,
                        name: string,
                        description: string
                        type: string[file/param],
                        value: string(address/value)
                    }...
                ],
                //输出结果
                output: [
                    {
                        id: string,
                        name: string,
                        description: string
                        value: String
                    }
                ]
                },
            ],
            resBehavior: [
                {
                    uid: String,
                    name: String,
                    behavior: String,
                    value: String
                }
            ]
         }
         */
    }

    /*
    递归函数
     */
    private WorkflowTemplate a2WorkflowTemplate(Document docXml, WorkflowTemplate workflowTemplate, int level){
        //传入的是活动文档，所以取到的是 activity
        //通过递归层数判断属于哪个层次 project(0)-subProject(1)-activity(>=2)
        HashMap<String, String> activityInfo = DocInterpret.getRootInfo(docXml);
        String aType = activityInfo.get("type");
        if (aType.equals(ActivityType.Activity_Group.toString())){
            //group 遍历其中的子活动取出其中的操作
            HashSet<HashMap<String, String>> childActivitiesList = DocInterpret.getChildActivities(docXml);
            if (childActivitiesList == null) return workflowTemplate;
            //递归进入下一层
            for (Iterator<HashMap<String, String>> it = childActivitiesList.iterator();it.hasNext();){
                HashMap<String, String> aInfo = it.next();
                String aid = aInfo.get("id");
                Document childDocXml = loadXML(aid);
                ++ level;
                a2WorkflowTemplate(childDocXml, workflowTemplate, level);
            }
        }else if (aType.equals(ActivityType.Activity_Unit.toString())){
            //获取 geoAnalysis 节点
            List<Node> geoAnalysisOperations = DocInterpret.getGeoAnalysisOperation(docXml);
            if (geoAnalysisOperations == null || geoAnalysisOperations.size() == 0) return workflowTemplate;
            HashSet<Operation> operations = new HashSet<>();
            List<Node> inputs = Lists.newLinkedList();
            for (Node operationNode : geoAnalysisOperations){
                Element operationEle = (Element)operationNode;
                String oid = operationEle.attributeValue("id");
                String toolRef = operationEle.attributeValue("toolRef");
                String description = operationEle.attributeValue("purpose");

                //获取 geoAnalysis 的 input 信息
                List<Node> inputResRefs = operationEle.selectNodes(".//ResRef[@type= 'input']");
                HashSet<OperationRes> inputRes = operationRes2Entity(inputResRefs, docXml, "file");
                List<Node> outputResRefs = operationEle.selectNodes(".//ResRef[@type = 'output']");
                HashSet<OperationRes> outputRes = operationRes2Entity(outputResRefs, docXml, "file");
                //输入中会有类型为参数的部分
                List<Node> paramRefs = operationEle.selectNodes(".//ResRef[@type = 'param']");
                HashSet<OperationRes> inParam = operationRes2Entity(paramRefs, docXml, "param");
                //throw nullPointException,将参数信息加入输入
                inputRes.addAll(inParam);

                Operation operation = new Operation();
                operation.setInput(inputRes);
                operation.setOutput(outputRes);
                operation.setOid(oid);
                operation.setToolId(toolRef);
                operation.setDescription(description);
                operations.add(operation);
                //添加purpose 信息
                //活动 id
                String aid = activityInfo.get("id");
                Activity activity;
                if (level == 0 ){
                    activity = projectRepository.findById(aid).get();
                }else if (level == 1){
                    activity = subprojectRepository.findById(aid).get();
                }else {
                    activity = activityRepository.findById(aid).get();
                }
                if (activity != null) operation.setPurpose(activity.getPurpose());

                //提取 upload operation
                inputs.addAll(inputResRefs);
                inputs.addAll(paramRefs);
            }
            //将此子活动的操作添加到整个活动流中去
            HashSet<Operation> previous = workflowTemplate.getOperations();
            if (previous != null) operations.addAll(previous);
            workflowTemplate.setOperations(operations);

            //
            HashSet<HashMap<String, String>> resBehavior = toResBehavior(inputs, docXml);
            HashSet<HashMap<String, String>> previousResBehaviors = workflowTemplate.getResBehavior();
            if (previousResBehaviors != null) resBehavior.addAll(previousResBehaviors);
            workflowTemplate.setResBehavior(resBehavior);
        }
        //当前层次内容处理结束，返回上一层
        --level;
        return workflowTemplate;
    }

    /*
    将 xml analysis 中 in/out 转换为操作资源实体
     */
    private HashSet<OperationRes> operationRes2Entity(List<Node> inRes, Document docXml, String type){
        if (inRes == null) return null;
        HashSet<OperationRes> operationResList = new HashSet<>();
        for (Node resNode : inRes){
            Element resEle = (Element)resNode;
            OperationRes operationRes = new OperationRes();
            String uid = resEle.attributeValue("idRef");
            String eventName = resEle.attributeValue("name");
            String eventDescription = resEle.attributeValue("description");
            operationRes.setName(eventName);
            operationRes.setDescription(eventDescription);
            operationRes.setUid(uid);
            operationRes.setType(type);
            Element resElement = DocInterpret.getResElement(docXml, uid);
            if (resElement != null){
                operationRes.setValue(resElement.attributeValue("href"));
            }
            operationResList.add(operationRes);
        }
        return operationResList;
    }

    /*
    将资源操作转换为所需的格式
     */
    private HashSet<HashMap<String, String>> toResBehavior(List<Node> inRes, Document docXml){
        if (inRes == null) return null;
        HashSet<HashMap<String, String>> resBehaviors = new HashSet<>();
        for (Node resNode : inRes){
            Element resOperationEle = (Element)resNode;
            String uid = resOperationEle.attributeValue("idRef");
            //获取资源的上传操作
            Element resUploadEle = DocInterpret.getResourceOperationByResId(docXml, uid);
            if (resUploadEle != null){
                Element resInfoEle = DocInterpret.getResElement(docXml, uid);
                if (resInfoEle != null){
                    HashMap<String, String> resBehavior = new HashMap<>();
                    resBehavior.put("uid", resInfoEle.attributeValue("id"));
                    resBehavior.put("name", resInfoEle.attributeValue("name"));
                    resBehavior.put("value", resInfoEle.attributeValue("href"));
                    resBehavior.put("behavior", "upload");
                    resBehaviors.add(resBehavior);
                }
            }
        }
        return resBehaviors;
    }
}
