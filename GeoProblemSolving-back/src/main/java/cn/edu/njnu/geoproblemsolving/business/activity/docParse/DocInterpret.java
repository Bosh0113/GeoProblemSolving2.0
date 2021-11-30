package cn.edu.njnu.geoproblemsolving.business.activity.docParse;


import cn.edu.njnu.geoproblemsolving.business.activity.entity.Activity;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.DocPerson;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.HashSet;

/**
 * 将文档解析的相关内容全部拆分为原子操作
 * 在服务层通过组装这些原子操作来满足业务逻辑需求
 * 以此对外实现独立，只读
 * 这个类包含了操作活动文档的众多方法
 *
 * 基于参与式平台的操作才有此文档，是否脱离了参与式平台该文档就没有作用了
 * 如何能使该此文档的内容能说得脱离参与式平台？
 * 文档只是工具，如果用这个文档？
 *
 * 有点像是为了解决问题而提出问题
 */
public class DocInterpret {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocInterpret.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //采用单例模式
    private DocInterpret() {}
    private volatile static DocInterpret docInterpret;

    public static DocInterpret getInstance(){
        if (docInterpret == null){
            synchronized (DocInterpret.class){
                if (docInterpret == null){
                    docInterpret = new DocInterpret();
                }
            }
        }
        return docInterpret;
    }

    /**
     * 初始化活动文档
     * @param activity 项目、子项目、活动对象
     * @return 活动文档的 xml
     */
    public static String initActivityDoc(Activity activity, JSONObject creator){
        Document document = DocumentHelper.createDocument();
        String activityType = activity.getType().toString();

        //add rootElement
        Element rootEle = document.addElement("Activity");
        rootEle.addAttribute("id", activity.getAid());
        rootEle.addAttribute("name", activity.getName());
        rootEle.addAttribute("type", activityType);
        rootEle.addAttribute("description", activity.getDescription());

        if (creator == null) {
            LOGGER.info("Activity creator is null.");
            return "fail";
        }
        LOGGER.info("Create activity xml document successfully.");
        return document.asXML();
    }

    /**
     * 更改活动类型
     *
     * @param xmlDoc
     * @param activity
     * @return
     */
    public static String changeActivityType(Document xmlDoc, Activity activity, HashSet<DocPerson> members){
        Element rootEle = xmlDoc.getRootElement();
        rootEle.clearContent();
        rootEle.attribute("type").setValue(activity.getType().toString());

        initActivityDocContent(rootEle, activity, members);

        return rootEle.asXML();
    }

    private static void initActivityDocContent(Element rootEle, Activity activity, HashSet<DocPerson> members){
        //add general element
        Element participantsEle = rootEle.addElement("Participants");

        for (DocPerson member : members){
            Element personEle = participantsEle.addElement("Person");
            personEle.addAttribute("id", member.getUserId());
            personEle.addAttribute("role", member.getRole());
            personEle.addAttribute("state", "in");
            personEle.addAttribute("email", member.getEmail());
            personEle.addAttribute("name", member.getName());
            HashSet<String> domains = member.getDomain();
            if (domains != null && !domains.isEmpty()) {
                for (String domain : domains) {
                    Element domainEle = personEle.addElement("Domain");
                    domainEle.addAttribute("description", domain);
                }
            }
            HashSet<String> organizations = member.getOrganization();
            if (organizations != null && !organizations.isEmpty()) {
                for (String organization : organizations) {
                    Element domainEle = personEle.addElement("Organization");
                    domainEle.addAttribute("description", organization);
                }
            }
        }

        rootEle.addElement("ResourceCollection");
        rootEle.addElement("OperationRecords");

        String activityType = activity.getType().toString();
        if (activityType.equals("Activity_Unit")) {
            rootEle.addElement("ToolBox");
            rootEle.addElement("TaskList");
            rootEle.addElement("TaskDependency");

        } else if (activityType.equals("Activity_Group")) {
            rootEle.addElement("ChildActivities");
            rootEle.addElement("ActivityDependencies");
        }

    }

    //=================Generic operation==============================================================
    public static Element appendParticipant(Element participantEle, DocPerson member){

        return participantEle;
    }

    private static Element createParticipantElement(DocPerson member){
        Element personEle = DocumentHelper.createElement("Person");
        personEle.addAttribute("id", member.getUserId());
        personEle.addAttribute("email", member.getEmail());
        personEle.addAttribute("name", member.getName());
        personEle.addAttribute("role", member.getRole());
        personEle.addAttribute("state", "in");

        return personEle;
    }

}
