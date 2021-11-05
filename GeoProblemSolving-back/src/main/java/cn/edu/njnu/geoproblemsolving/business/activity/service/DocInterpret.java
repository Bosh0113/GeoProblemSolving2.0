package cn.edu.njnu.geoproblemsolving.business.activity.service;

import cn.edu.njnu.geoproblemsolving.business.activity.entity.Activity;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.ActivityDoc;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.ActivityType;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ActivityDocRepository;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ActivityRepository;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ProjectRepository;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.SubprojectRepository;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.resource.service.Impl.ActivityResServiceImpl;
import cn.edu.njnu.geoproblemsolving.business.user.dao.UserDao;
import cn.edu.njnu.geoproblemsolving.business.user.entity.UserEntity;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import sun.awt.image.GifImageDecoder;

import java.util.*;

/**
 * @ClassName DocInterpret
 * @Description 活动文档解析
 * 1. 活动文档解析
 * 2. 地理问题求解模板文档
 * 3. 文档间的映射方法
 * <p>
 * 后台解析与前台解析的区别
 * 1. 后台解析肯定是更快
 * 2.
 * <p>
 * 封装的工具类
 * @Author zhngzhng
 * @Date 2021/10/20
 **/
@Service
public class DocInterpret implements ActivityDocParser {
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

    @Value("${userServerLocation}")
    String userServer;

    @Autowired
    RestTemplate restTemplate;

    //Used to simplify operations, avoid loading every time.
    private Document activityDocXml;
    private ActivityDoc operatingDoc;

    private void loadXML(String aid) throws DocumentException {
        Optional<ActivityDoc> byId = docRepository.findById(aid);
        if (!byId.isPresent()) {
            return;
        }
        operatingDoc = byId.get();
        activityDocXml = DocumentHelper.parseText(operatingDoc.getDocument());
    }

    private void saveXML() {
        String docXMLStr = activityDocXml.asXML();
        operatingDoc.setDocument(docXMLStr);
        docRepository.save(operatingDoc);
    }

    private void syncGlobalVariables(String aid) throws DocumentException {
        if (operatingDoc == null || !operatingDoc.getAid().equals(aid)) {
            if (operatingDoc != null) saveXML();
            loadXML(aid);
        }
    }

    //Fast looping which avoids the cost of creating an Iterator object for each loop.
    private void treeWalk(Element element) {
        for (int i = 0, size = element.nodeCount(); i < size; i++) {
            Node node = element.node(i);
            if (node instanceof Element) {

            }
        }
    }

    //===================Document level operations===================================================
    /*
    Document level operations
    Initialize document
    Change the document type
     */

    //Initialize document
    public void initActivityDoc(String aid, String level) {
    }

    public void initActivityDoc(Activity activity) {
        Document document = DocumentHelper.createDocument();
        String activityType = activity.getType().toString();
        Element activityEle = document.addElement("Activity");
        activityEle.addAttribute("id", activity.getAid());
        activityEle.addAttribute("name", activity.getName());
        activityEle.addAttribute("type", activityType);
        activityEle.addAttribute("description", activity.getDescription());

        //participants
        Element participantsEle = activityEle.addElement("Participants");
        JSONObject memberJson = JSONObject.parseObject(JSONObject.toJSONString(activity.getMembers().get(0)));
        Element personEle = participantsEle.addElement("person");
        String userId = memberJson.getString("userId");
        personEle.addAttribute("id", userId);
        personEle.addAttribute("role", memberJson.getString("role"));
        personEle.addAttribute("state", "in");
        UserEntity user = userDao.findUserByIdOrEmail(userId);
        if (user != null) {
            personEle.addAttribute("email", user.getEmail());
            personEle.addAttribute("name", user.getName());
            ArrayList<String> domains = user.getDomain();
            if (domains != null && domains.size() != 0) {
                for (String domain : domains) {
                    Element domainEle = personEle.addElement("Domain");
                    domainEle.addAttribute("description", domain);
                }

            }
            ArrayList<String> organizations = user.getOrganizations();
            if (organizations != null && organizations.size() != 0) {
                for (String organization : organizations) {
                    Element domainEle = personEle.addElement("Organization");
                    domainEle.addAttribute("description", organization);
                }
            }
        }

        Element resCollectionEle = activityEle.addElement("ResourceCollection");
        Element operationsEle = activityEle.addElement("OperationRecords");

        if (activityType.equals("Activity_Unit")) {
            Element toolBoxEle = activityEle.addElement("ToolBox");
            Element taskListEle = activityEle.addElement("TaskList");
            Element taskDependencies = activityEle.addElement("TaskDependency");

        } else if (activityType.equals("Activity_Group")) {
            Element childEle = activityEle.addElement("ChildActivities");
            Element acDependenciesEle = activityEle.addElement("ActivityDependencies");
        }

        //
        String xmlStr = document.asXML();
        ActivityDoc activityDoc = new ActivityDoc();
        activityDoc.setAid(activity.getAid());
        activityDoc.setDocument(xmlStr);
        docRepository.save(activityDoc);
    }

    @Override
    public void changeActivityType(String aid, String type) {

    }

    //Change the document type.
    private void activityOperation(Activity activity, String... params) throws DocumentException {
        //param: operationType, String aid, String level
        if (activityDocXml == null) {
            loadXML(activity.getAid());
        }
        String operationType = params[0];
        if (operationType.equals("type")) {
            Element activityEle = activityDocXml.getRootElement();
            String acType = activityEle.valueOf("@type");
            if (acType.equals("Activity_Group")) {
                Element childActivitiesEle = activityEle.element("ChildActivities");
                childActivitiesEle.getParent().remove(childActivitiesEle);
                Element activityDependenciesEle = activityEle.element("ActivityDependencies");
                activityDependenciesEle.getParent().remove(activityDependenciesEle);
            } else if (acType.equals("Activity_Unit")) {
                Element toolBoxEle = activityEle.element("ToolBox");
                toolBoxEle.getParent().remove(toolBoxEle);
                Element taskListEle = activityEle.element("TaskList");
                taskListEle.getParent().remove(taskListEle);
                Element taskDependencyEle = activityEle.element("TaskDependency");
                taskDependencyEle.getParent().remove(taskDependencyEle);
            }
        }
        if (activity == null) {
            String aid = params[1];
            String level = params[2];
        }
    }


    //=================Generic operation==============================================================
    /*
    Generic operation
    Common item between multi and signal activity:
    Participants
    ResourceCollection
    OperationRecords(Multi: process, activity, communication; Signal: resource, tool, communication, geo-analysis)
     */

    /*
        1. Participants related operations
     */
    @Override
    public Object inActivity(String aid, HashMap<String, String> userInfo) throws DocumentException {
        syncGlobalVariables(aid);
        if (activityDocXml == null) return null;
        Element participantsEle = (Element) activityDocXml.selectNodes("/Activity/Participants").get(0);
        Element personEle = participantsEle.addElement("Person");
        personEle.addAttribute("id", userInfo.get("userId"));
        personEle.addAttribute("email", userInfo.get("email"));
        personEle.addAttribute("name", userInfo.get("name"));
        personEle.addAttribute("role", userInfo.get("role"));
        personEle.addAttribute("state", "in");

        //Acquire user's domain and organization
        String userId = userInfo.get("userId");
        String getTagUrl = "http://" + userServer + "/user/tag/" + userId;
        try {
            JSONObject response = restTemplate.getForObject(getTagUrl, JSONObject.class);
            if (response.getInteger("code") != 0) return null;
            JSONObject result = response.getJSONObject("data");
            JSONArray domain = result.getJSONArray("domain");
            if (domain != null) {
                for (Object item : domain) {
                    Element domainEle = personEle.addElement("Domain");
                    domainEle.addAttribute("description", (String) item);
                }
            }
            JSONArray organization = result.getJSONArray("organization");
            if (organization != null) {
                for (Object item : organization) {
                    Element organizationEle = personEle.addElement("Organization");
                    organizationEle.addAttribute("description", (String) item);
                }
            }
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }
        return null;
    }


    @Value("client_id")
    String clientId;
    @Value("client_secret")
    String clientSecret;

    public Object insActivity(String aid, HashSet<String> userIds) throws DocumentException {
        for (String userId : userIds){
            String reqUrl = "http://" + userServer + "/user/" + userId + "/" + userId + "/" + clientId + "/" + clientSecret;
            JSONObject response = restTemplate.getForObject(reqUrl, JSONObject.class);
            if (response.getInteger("code") == 0){
                UserEntity user = response.getObject("data", UserEntity.class);
                inActivity(aid, user);
            }
        }
        return null;
    }

    public Object inActivity(String aid, UserEntity user) throws DocumentException {
        syncGlobalVariables(aid);
        if (activityDocXml == null) return null;
        Element participantsEle = (Element) activityDocXml.selectNodes("/Activity/Participants").get(0);
        Element personEle = participantsEle.addElement("Person");
        personEle.addAttribute("id", user.getUserId());
        personEle.addAttribute("email", user.getEmail());
        personEle.addAttribute("name", user.getName());
        personEle.addAttribute("role", "ordinary-member");
        personEle.addAttribute("state", "in");

        ArrayList<String> domain = user.getDomain();
        if (domain != null) {
            for (String item : domain) {
                Element domainEle = personEle.addElement("Domain");
                domainEle.addAttribute("description",  item);
            }
        }
        ArrayList<String> organization = user.getOrganizations();
        if (organization != null) {
            for (String item : organization) {
                Element organizationEle = personEle.addElement("Organization");
                organizationEle.addAttribute("description", (String) item);
            }
        }
        return null;
    }


    @Override
    public Object outActivity(String aid, String userId) {
        return null;
    }

    @Override
    public Object rolePut(String aid, String userId, String role) {
        return null;
    }

    @Override
    public Object userDomainPut(String aid, String userId, HashSet<String> domains) {
        return null;
    }

    @Override
    public Object userOrganizationPut(String aid, String userId, HashSet<String> organizations) {
        return null;
    }

    /*
        2.Resources related operations
     */

    @Override
    public Object uploadResource(String aid, HashMap<String, String> resInfo) throws DocumentException {
        syncGlobalVariables(aid);
        if (activityDocXml == null) return null;
        Element resourceCollectionNode = (Element) activityDocXml.selectNodes("/Activity/ResourceCollection").get(0);
        if (resourceCollectionNode == null) resourceCollectionNode = DocumentHelper.createElement("ResourceCollection");
        Element resourceEle = resourceCollectionNode.addElement("Resource");
        resourceEle.addAttribute("id", resInfo.get("uid"));
        resourceEle.addAttribute("name", resInfo.get("name"));
        resourceCollectionNode.addAttribute("type", resInfo.get("type"));
        resourceCollectionNode.addAttribute("href", resInfo.get("address"));
        resourceCollectionNode.addAttribute("state", "accessible");
        return null;
    }

    @Override
    public Object uploadResource(String aid, ResourceEntity res) throws DocumentException {
        syncGlobalVariables(aid);
        if (activityDocXml == null) return null;
        Element resourceCollectionNode = (Element) activityDocXml.selectNodes("/Activity/ResourceCollection").get(0);
        if (resourceCollectionNode == null) resourceCollectionNode = DocumentHelper.createElement("ResourceCollection");
        Element resourceEle = resourceCollectionNode.addElement("Resource");
        resourceEle.addAttribute("id", res.getUid());
        resourceEle.addAttribute("name", res.getName());
        resourceCollectionNode.addAttribute("type", res.getType());
        resourceCollectionNode.addAttribute("href", res.getAddress());
        resourceCollectionNode.addAttribute("state", "accessible");
        return null;
    }

    @Autowired
    ActivityResServiceImpl ripServer;
    //Store the flow resource in the document.
    public Object flowResourceUpload(String fromAid, String endAid, HashSet<String> uids) throws DocumentException {
        if (uids == null || uids.size() == 0) return null;
        syncGlobalVariables(endAid);
        ActivityDoc activityDoc = docRepository.findById(fromAid).get();
        Document fromDocXML = DocumentHelper.parseText(activityDoc.getDocument());
        Element resCollectionEle = (Element)activityDocXml.selectNodes("/Activity/ResourceCollection").get(0);
        for (String uid : uids){
            Element resEle = (Element)fromDocXML.selectNodes("/Activity/ResourceCollection//Resource[@id = uid]").get(0);
            resCollectionEle.add(resEle);
        }
        return null;
    }

    public Object flowResourceUpload(String fromAid, String endAid, String resId) throws DocumentException {
        HashSet<String> strings = new HashSet<>();
        strings.add(resId);
        flowResourceUpload(fromAid, endAid, strings);
        return null;
    }


        @Override
    public Object putRes(String aid, HashMap<String, String> putInfo) {
        return null;
    }

    @Override
    public Object removeRes(String aid, String rid) {
        return null;
    }


    //========================Multi activity operation===================================================
    /*
        1.Operation records
     */



    /*
        2. ChildActivities and activity dependencies
     */

    //=======================Signal activity operation===================================================
    /*
        1.Tool related operation, include toolbox
     */

    /*
        2.Task related operation, include task list and task dependency.
     */

    @Override
    public Object bindOperation(String aid, String oid) {
        return null;
    }

    @Override
    public Object unbindOperation(String aid, String oid) {
        return null;
    }

    @Override
    public Object linkTask(String aid, String fromId, String toId) {
        return null;
    }

    /*
        3.Operation records, remain geo-analysis.
     */





    //Mapping method
    public Object activity2WorkflowTemplate(String rootAid) throws DocumentException {
        syncGlobalVariables(rootAid);
        if (activityDocXml == null) return "Fail";
        Element activityEle = activityDocXml.getRootElement();
        String type = activityEle.attributeValue("type");

        Document workflowTemplate = DocumentHelper.createDocument();
        Element workflowEle = workflowTemplate.addElement("workflow");
        Element resourceCollectionEle = workflowEle.addElement("ResourceCollection");
        Element participantsEle = workflowEle.addElement("Participants");
        Element toolBoxEle = workflowEle.addElement("ToolBox");

        if (type.equals(ActivityType.Activity_Unit)){
            List<Node> geoAnalysisNodes = activityEle.selectNodes("/OperationRecords//Operation[@type = geo-analysis]");
            for (int i = 0; i < geoAnalysisNodes.size(); i++){
                Element gaNode = (Element)geoAnalysisNodes.get(i);
                String toolId = gaNode.attributeValue("toolRef");
                Element toolEle =  (Element)activityEle.selectSingleNode("/ToolBox/Tool[@id = toolId]");
                toolBoxEle.add(toolEle);
                List<Node> modelRelationNode = gaNode.selectNodes("/ResRef[@tyep != param]");
                for (int j = 0; j < modelRelationNode.size(); j++){
                    Element inoutNode =  (Element)modelRelationNode.get(i);
                    String resId = inoutNode.attributeValue("idRef");
                    Element resEle =  (Element)activityEle.selectSingleNode("/ResourceCollection/Resource[@id = 'resId']");
                    resourceCollectionEle.add(resEle);
                }
                List<Node> personNodes = gaNode.selectNodes("/PersonRef");
                for (int k = 0; k < personNodes.size(); k++){
                    Element modelPersonEle =  (Element)personNodes.get(i);
                    String personId = modelPersonEle.attributeValue("idRef");
                    Element personEle =  (Element)activityEle.selectSingleNode("/Participants/Person[@id = 'personId']");
                    participantsEle.add(personEle);
                }
            }
        }
        if (type.equals(ActivityType.Activity_Group)){
            List<Node> childNodes = activityEle.selectNodes("/ChildActivities/Child");
        }
        return null;
    }

    // private Element a2WorkflowTemplate(List<Node> childActivitiesNode, Document rootActivity) throws DocumentException {
    //     for (int i = 0; i < childActivitiesNode.size(); i++) {
    //         Element childEle = (Element)childActivitiesNode.get(0);
    //         String aid = childEle.attributeValue("id");
    //         Optional<ActivityDoc> byId = docRepository.findById(aid);
    //         if (!byId.isPresent()) continue;
    //         ActivityDoc activityDoc = byId.get();
    //         Document docXML = DocumentHelper.parseText(activityDoc.getDocument());
    //         Element activityEle = docXML.getRootElement();
    //         String type = activityEle.attributeValue("type");
    //
    //
    //         if (type.equals(ActivityType.Activity_Unit)){
    //             List<Node> geoAnalysisNodes = activityEle.selectNodes("/OperationRecords//Operation[@type = 'geo-analysis']");
    //             for (int i = 0; i < geoAnalysisNodes.size(); i++){
    //                 Element gaNode = (Element)geoAnalysisNodes.get(i);
    //                 String toolId = gaNode.attributeValue("toolRef");
    //                 Element toolEle =  (Element)activityEle.selectSingleNode("/ToolBox/Tool[@id = 'toolId']");
    //                 toolBoxEle.add(toolEle);
    //                 List<Node> modelRelationNode = gaNode.selectNodes("/ResRef[@tyep != 'param']");
    //                 for (int j = 0; j < modelRelationNode.size(); j++){
    //                     Element inoutNode =  (Element)modelRelationNode.get(i);
    //                     String resId = inoutNode.attributeValue("idRef");
    //                     Element resEle =  (Element)activityEle.selectSingleNode("/ResourceCollection/Resource[@id = 'resId']");
    //                     resourceCollectionEle.add(resEle);
    //                 }
    //                 List<Node> personNodes = gaNode.selectNodes("/PersonRef");
    //                 for (int k = 0; k < personNodes.size(); k++){
    //                     Element modelPersonEle =  (Element)personNodes.get(i);
    //                     String personId = modelPersonEle.attributeValue("idRef");
    //                     Element personEle =  (Element)activityEle.selectSingleNode("/Participants/Person[@id = 'personId']");
    //                     participantsEle.add(personEle);
    //                 }
    //             }
    //         }
    //         if (type.equals(ActivityType.Activity_Group)){
    //             List<Node> childNodes = activityEle.selectNodes("/ChildActivities/Child");
    //             a2WorkflowTemplate(childNodes, rootActivity);
    //         }
    //     }
    // }


}
