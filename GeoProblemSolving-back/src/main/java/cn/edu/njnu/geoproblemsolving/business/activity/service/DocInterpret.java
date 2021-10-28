package cn.edu.njnu.geoproblemsolving.business.activity.service;

import cn.edu.njnu.geoproblemsolving.business.activity.entity.Activity;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.ActivityDoc;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ActivityDocRepository;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ActivityRepository;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ProjectRepository;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.SubprojectRepository;
import cn.edu.njnu.geoproblemsolving.business.user.dao.UserDao;
import cn.edu.njnu.geoproblemsolving.business.user.entity.UserEntity;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName DocInterpret
 * @Description 活动文档解析
 * 提供后台解析活动文档功能
 * 为后续从活动文档的复杂特征中提取有用的信息提供基础
 *
 * 后台解析与前台解析的区别
 * 1. 后台解析肯定是更快
 * 2.
 *
 * @Author zhngzhng
 * @Date 2021/10/20
 **/
@Service
public class DocInterpret {
    @Autowired
    ActivityDocRepository docRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    SubprojectRepository subprojectRepository;
    @Autowired
    ActivityRepository  activityRepository;
    @Autowired
    UserDao userDao;


    private Document activityDocXml;

    public void loadXML(String aid) throws DocumentException {
        Optional<ActivityDoc> byId = docRepository.findById(aid);
        if (!byId.isPresent()){
            return;
        }
        String xmlStr = byId.get().getDocument();
        activityDocXml = DocumentHelper.parseText(xmlStr);
    }


    public void initActivityDoc(Activity activity){
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
        if (user != null){
            personEle.addAttribute("email", user.getEmail());
            personEle.addAttribute("name", user.getName());
            ArrayList<String> domains = user.getDomain();
            if (domains != null && domains.size() != 0){
                for (String domain : domains){
                    Element domainEle = personEle.addElement("Domain");
                    domainEle.addAttribute("description", domain);
                }

            }
            ArrayList<String> organizations = user.getOrganizations();
            if (organizations != null && organizations.size() != 0){
                for (String organization : organizations){
                    Element domainEle = personEle.addElement("Organization");
                    domainEle.addAttribute("description", organization);
                }
            }
        }

        Element resCollectionEle = activityEle.addElement("ResourceCollection");
        Element operationsEle = activityEle.addElement("OperationRecords");

        if (activityType.equals("Activity_Unit")){
            Element toolBoxEle = activityEle.addElement("ToolBox");
            Element taskListEle = activityEle.addElement("TaskList");
            Element taskDependencies = activityEle.addElement("TaskDependency");

        }else if (activityType.equals("Activity_Group")){
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
    //Generic operation
    //1.Operating activity on the xml, all the operation is based on the loadXML.
    private void activityOperation(Activity activity, String... params) throws DocumentException {
        //param: operationType, String aid, String level
        if (activityDocXml == null){
            loadXML(activity.getAid());
        }
        String operationType = params[0];
        if (operationType.equals("type")){
            Element activityEle = activityDocXml.getRootElement();
            String acType = activityEle.valueOf("@type");
            if (acType.equals("Activity_Group")){
                Element childActivitiesEle = activityEle.element("ChildActivities");
                childActivitiesEle.getParent().remove(childActivitiesEle);
                Element activityDependenciesEle = activityEle.element("ActivityDependencies");
                activityDependenciesEle.getParent().remove(activityDependenciesEle);
            }else if (acType.equals("Activity_Unit")){
                Element toolBoxEle = activityEle.element("ToolBox");
                toolBoxEle.getParent().remove(toolBoxEle);
                Element taskListEle = activityEle.element("TaskList");
                taskListEle.getParent().remove(taskListEle);
                Element taskDependencyEle = activityEle.element("TaskDependency");
                taskDependencyEle.getParent().remove(taskDependencyEle);
            }

        }
        if (activity == null){
            String aid = params[1];
            String level = params[2];
        }
    }
    /*
    2.Operating resource on the xml
    first:
     */

    //========================Multi activity operation===================================================

}
