package cn.edu.njnu.geoproblemsolving.business.activity.service;

import cn.edu.njnu.geoproblemsolving.business.activity.entity.Activity;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.ActivityDoc;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.Project;
import cn.edu.njnu.geoproblemsolving.business.activity.enums.ActivityType;
import cn.edu.njnu.geoproblemsolving.business.activity.repository.ActivityDocRepository;
import cn.edu.njnu.geoproblemsolving.business.user.entity.UserEntity;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName ActivityDocsOperation
 * @Description 活动文档解析，文档具有一定的可读性
 * @Author zhngzhng
 * @Date 2021/9/14
 **/
@Service
public class ActivityDocsOperation {
    @Autowired
    ActivityDocRepository docRepository;

    //全局的 document，类似于 html 的读组织方式
    private Document activityDoc;
    private final  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private final DocumentBuilder builder = factory.newDocumentBuilder();

    public ActivityDocsOperation() throws ParserConfigurationException {
    }

    public void xml2json(String xmlDoc) throws IOException, SAXException {
        activityDoc = builder.parse(xmlDoc);
    }

    //初始化活动文档，新建 Activity 的时候会用到
    public Object activityDocInit(JSONObject activity, JSONObject user){
        activityDoc = builder.newDocument();
        //根节点
        Element rootElement = activityDoc.createElement("Activity");
        rootElement.setAttribute("id", activity.getString("aid"));
        rootElement.setAttribute("name", activity.getString("name"));
        rootElement.setAttribute("description", activity.getString("description"));
        rootElement.setAttribute("type", activity.getString("type"));
        activityDoc.appendChild(rootElement);

        //参与者节点
        Element participants = activityDoc.createElement("Participants");
        Element person = activityDoc.createElement("Person");
        person.setAttribute("id", user.getString("userId"));
        person.setAttribute("email", user.getString("email"));
        person.setAttribute("name", user.getString("name"));
        person.setAttribute("role", user.getString("role"));
        person.setAttribute("state", user.getString("state"));

        JSONArray domains = user.getJSONArray("domain");
        if (domains != null){
            for (int i = 0; i < domains.size(); i++) {
                String domain = domains.getString(i);
                Element domainElement = activityDoc.createElement("Domain");
                domainElement.setAttribute("description", domain);
                person.appendChild(domainElement);
            }
        }
        JSONArray organizations = user.getJSONArray("organization");
        if (organizations != null){
            for (int i = 0; i < organizations.size(); i++) {
                String organization = organizations.getString(i);
                Element organizationElement = activityDoc.createElement("Organization");
                organizationElement.setAttribute("description", organization);
                person.appendChild(organizationElement);
            }
        }
        rootElement.appendChild(participants);
        rootElement.appendChild(activityDoc.createElement("ResourceCollection"));
        rootElement.appendChild(activityDoc.createElement("OperationRecords"));

        String activityType = activity.getString("type");
        if (activityType.equals(ActivityType.Activity_Group.toString())){
            rootElement.appendChild(activityDoc.createElement("ChildActivities"));
            rootElement.appendChild(activityDoc.createElement("ActivityDependencies"));
            rootElement.appendChild(activityDoc.createElement("TaskDependency"));
        }else if (activityType.equals(ActivityType.Activity_Unit.toString())){
            rootElement.appendChild(activityDoc.createElement("ToolBox"));
            rootElement.appendChild(activityDoc.createElement("TaskList"));
        }else {

        }
        ActivityDoc doc = new ActivityDoc();
        doc.setAid(activity.getString("aid"));
        doc.setDocument(doc.toString());
        docRepository.save(doc);
        return null;
    }


}
