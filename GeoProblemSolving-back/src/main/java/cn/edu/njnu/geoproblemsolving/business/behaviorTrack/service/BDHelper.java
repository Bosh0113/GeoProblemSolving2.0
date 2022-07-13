package cn.edu.njnu.geoproblemsolving.business.behaviorTrack.service;

import cn.edu.njnu.geoproblemsolving.business.behaviorTrack.enums.InteractiveBehavior;
import cn.edu.njnu.geoproblemsolving.business.behaviorTrack.enums.Scene;
import cn.edu.njnu.geoproblemsolving.business.behaviorTrack.enums.SupportElement;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.util.LinkedMultiValueMap;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 交互行为文档工具类
 * @ClassName BDHelper
 * @Description 工具类
 * @Author zhngzhng
 * @Date 2022/6/29
 **/
@Slf4j
public class BDHelper {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 初始化交互行为文档
     * @param pid
     * @return
     */
    public static Document createBehaviorDoc(String pid) {
        Document document = DocumentHelper.createDocument();
        Element rootEle = document.addElement("BehaviorCollection");
        rootEle.addAttribute("id", pid);
        return document;
    }

    public static void appendOperation(Element bEle, String type, LinkedHashMap<String, String> operationInfo) {
        Element operationEle = DocumentHelper.createElement("Operation");
        //操作的类型: 输入、选择、点击等
        operationEle.addAttribute("type", type);
        for (Map.Entry<String, String> item : operationInfo.entrySet()) {
            //受影响部分名字
            String key = item.getKey();
            //受影响部分的对应的值
            String value = item.getValue();
            Element infoEle = operationEle.addElement(key);
            infoEle.addText(value);
        }
        bEle.add(operationEle);
    }
    /**
     * 创建操作节点
     *
     * @param type 操作的类型
     * @param operationInfo operationInfo 操作的内容，Text/支撑要素，
     *                      需要与 Object 中的内容顺序一一对应
     * key: 节点名,Text/Number/Resource/Tool/Activity/Person
     * value: Text/Number话是具体的内容，支撑要素的话就是 id
     * @return 操作节点
     * @description
     */
    public static Element createOperationEle(String type, LinkedMultiValueMap<String, String> operationInfo) {
        Element operationEle = DocumentHelper.createElement("Operation");
        //操作的类型: 输入、选择、点击等
        operationEle.addAttribute("type", type);
        for (Map.Entry<String, List<String>> item : operationInfo.entrySet()) {
            String key = item.getKey();
            List<String> values = item.getValue();
            for (String value : values) {
                operationEle.addElement(key).addText(value);
            }
        }
        return operationEle;
    }

    /**
     * 创建交互行为节点
     *
     * @param aid    交互行为所处的活动
     * @param scene  交互行为情景
     * @param ibType 交互行为类别
     * @return
     */
    public static Element createBehaviorEle(String aid, Scene scene, InteractiveBehavior ibType) {
        Element behaviorEle = DocumentHelper.createElement("Behavior");
        behaviorEle.addAttribute("id", UUID.randomUUID().toString());
        behaviorEle.addAttribute("aId", aid);
        behaviorEle.addAttribute("scene", scene.toString());
        behaviorEle.addAttribute("type", ibType.toString());
        behaviorEle.addAttribute("time", DATE_FORMAT.format(new Date()));
        return behaviorEle;
    }

    /**
     * 添加 subject
     *
     * @param bEle   交互行为节点
     * @param seType 主体支撑要素类型
     * @param id     主体支撑要素 id
     */
    public static void appendSubject(Element bEle, SupportElement seType, String id) {
        Element subjectEle = DocumentHelper.createElement("Subject");
        subjectEle.add(createSupEleByType(seType, id));
        bEle.add(subjectEle);
    }

    /**
     * 根据支撑要素类别生成支撑要素节点
     *
     * @param seType 支撑要素类别
     * @param id     支持要素 id
     * @return
     */
    private static Element createSupEleByType(SupportElement seType, String id) {
        Element element;
        if (seType.equals(SupportElement.activity)) {
            element = DocumentHelper.createElement("Activity");
        } else if (seType.equals(SupportElement.person)) {
            element = DocumentHelper.createElement("Person");
        } else if (seType.equals(SupportElement.resource)) {
            element = DocumentHelper.createElement("Resource");
        } else {
            element = DocumentHelper.createElement("Tool");
        }
        element.addAttribute("id", id);
        return element;
    }

    /**
     * 添加状态变化的客体节点
     *
     * @param bEle   交互行为节点
     * @param seType 客体支撑要素类型
     * @param id     客体支撑要素 id
     */
    public static void appendObjectAndState(Element bEle, SupportElement seType, String id) {
        Element objectEle = DocumentHelper.createElement("Object");
        Element supEle = createSupEleByType(seType, id);
        supEle.addElement("State");
        objectEle.add(objectEle);
        bEle.add(objectEle);
    }

    /**
     * 添加属性变化的客体节点
     *
     * @param bEle   交互行为节点
     * @param seType 客体支撑要素类型
     * @param id     客体支撑要素 id
     * @param attrs  变化的属性,需要保证和操作中的内容一样的顺序
     */
    public static void appendObjectAndAttrs(Element bEle, SupportElement seType, String id, LinkedHashSet<String> attrs) {
        Element objectEle = DocumentHelper.createElement("Object");
        Element supEle = createSupEleByType(seType, id);
        appendSupAttr(supEle, attrs);
        objectEle.add(supEle);
        bEle.add(objectEle);
    }

    /**
     * 添加简单客体节点，包括属性与状态变化
     * 简单客体节点：客
     * @param bEle
     * @param seType 客体支撑要素类型,受影响的客体类型
     * @param id 客体支撑要素 id
     * @param attrs 变化的属性或状态
     */
    public static void appendObjectSimple(Element bEle, SupportElement seType, String id, ArrayList<String> attrs) {
        Element objectEle = DocumentHelper.createElement("Object");



    }

    public static void appendObjectComplex(Element bEle, SupportElement seType, String id) {

    }


    /**
     * 添加支撑要素受影响的属性节点
     *
     * @param supEle
     * @param attrs
     */
    private static void appendSupAttr(Element supEle, HashSet<String> attrs) {
        for (Iterator<String> it = attrs.iterator(); it.hasNext(); ) {
            supEle.addElement(it.next());
        }
    }
}
