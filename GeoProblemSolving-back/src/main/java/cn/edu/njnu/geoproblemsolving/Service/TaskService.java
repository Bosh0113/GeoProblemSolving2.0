package cn.edu.njnu.geoproblemsolving.Service;

import cn.edu.njnu.geoproblemsolving.Dao.ComputerModel.*;
import cn.edu.njnu.geoproblemsolving.Entity.Model.JsonResult;
import cn.edu.njnu.geoproblemsolving.Entity.ModelItem.ModelItemEntity;
import cn.edu.njnu.geoproblemsolving.Entity.ModelItem.Support.State;
import cn.edu.njnu.geoproblemsolving.Enums.ResultEnum;
import cn.edu.njnu.geoproblemsolving.Exception.MyException;
import cn.edu.njnu.geoproblemsolving.Utils.ResultUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Service
public class TaskService {

    @Autowired
    ModelItemDao modelItemDao;

    @Value("${managerServerIpAndPort}")
    private String managerServer;

    @Value("${managerServerIpAndPort}")
    private String managerServerIpAndPort;

    @Value("${openGMSIpAndPort}")
    private String openGMSIpAndPort;

    @Value("${resourcePath}")
    private String resourcePath;

    @Resource
    private MongoTemplate mongoTemplate;

    public JsonResult getComputeModel(String oid) {//根据oid获得
        ModelItemDaoImpl modelItemDao = new ModelItemDaoImpl(mongoTemplate);
        Object computableModel = modelItemDao.readComputableModel(oid);

//        Object data=computableModel.getJSON;
        return ResultUtils.success(((ArrayList) computableModel).get(0));
    }


    public JSONObject createTask(String pid) {
        // 获得任务服务器
        RestTemplate restTemplate = new RestTemplate();
        String urlStr = "http://" + managerServerIpAndPort + "/GeoModeling/taskNode/getServiceTask/" + pid; ////Step0:根据MD5获取可用的任务服务器

        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.getForEntity(urlStr, JSONObject.class);//虚拟http请求
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.ERROR);
        }
        JSONObject result = jsonObjectResponseEntity.getBody().getJSONObject("data");
        String ip = result.getString("host");
        String port = result.getString("port");

//        Map<String, Object> createTaskJson = new HashMap<>();
        JSONObject initTaskJson = new JSONObject();
        initTaskJson.put("ip", ip);
        initTaskJson.put("port", port);
        initTaskJson.put("pid", pid);
        initTaskJson.put("username", "testzzy");

        urlStr = "http://" + managerServerIpAndPort + "/GeoModeling/computableModel/createTask";//Step1:createTask
        jsonObjectResponseEntity = restTemplate.postForEntity(urlStr, initTaskJson, JSONObject.class);
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.ERROR);
        }
        result = jsonObjectResponseEntity.getBody().getJSONObject("data");
        //保存url到相应的event 和 value

        return result;
    }


    public String invoke(JSONObject obj) {
        String urlStr = "http://" + managerServerIpAndPort + "/GeoModeling/computableModel/invoke";//Step3:配置完数据数据，开始调用模型
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity(urlStr, obj, JSONObject.class);
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.ERROR);
        }
        String taskTid = jsonObjectResponseEntity.getBody().getJSONObject("data").getString("tid");
        return taskTid;
    }

    public JSONObject refresh(JSONObject data) {
        String urlStr = "http://" + managerServerIpAndPort + "/GeoModeling/computableModel/refreshTaskRecord"; //Step4:根据task id去查询模型运行记录
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity(urlStr, data, JSONObject.class);
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.ERROR);
        }

        JSONObject result = jsonObjectResponseEntity.getBody();
        return result;
    }

    public String upload(File file, String ip, String port) {
        FileSystemResource resource = new FileSystemResource(file);      //临时文件
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("host", ip);             //dxServer里面的
        form.add("port", port);
        form.add("type", 2);
        form.add("file", resource);
        form.add("UserName", "zzy");

        String urlStr = "http://" + managerServer + "/GeoModeling/computableModel/uploadData";// Step2：上传输入数据到特定的数据交换服务器

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity(urlStr, form, JSONObject.class);
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.ERROR);
        }
        JSONObject result = jsonObjectResponseEntity.getBody();//获得上传数据的URL
        String urlResult = result.getJSONObject("data").getString("url");
        return urlResult;
    }

    public JSONObject slefInvoke(JSONObject obj) {
        //传入task在初始化页面时已经创建了Task 不需要再创建且获得了上传数据的URL
        JSONObject invokeBody = new JSONObject();
        JSONObject modelInstance = new JSONObject();
        modelInstance = obj.getJSONObject("modelItem");

        modelItemDao.updateComputableModel(modelInstance);//更新数据库

        invokeBody.put("inputs", getInvokeItem(modelInstance));//上传数据的 inputs数组
        invokeBody.put("ip", obj.getString("ip"));
        invokeBody.put("port", obj.getString("port"));
        invokeBody.put("pid", obj.getString("pid"));
        invokeBody.put("username", "testzzy");
        String tid = invoke(invokeBody);

        JSONObject refreshBody = new JSONObject();
        refreshBody.put("tid", tid);
        refreshBody.put("ip",obj.getString("ip"));
        refreshBody.put("port", obj.getString("port"));

        refresh(refreshBody);
        return refresh(refreshBody);
    }

    public ArrayList<Object> getInvokeItem(JSONObject obj) {
        ArrayList<Object> inputList = new ArrayList<>();
        JSONArray stateList = obj.getJSONArray("stateList");
        for (int i = 0; i < stateList.size(); i++) {
            JSONObject state = stateList.getJSONObject(i);

            JSONArray eventList = state.getJSONArray("eventList");
            for (int j = 0; j < eventList.size(); j++) {
                if(eventList.getJSONObject(j).getString("url") != "" && eventList.getJSONObject(j).getString("url") != null ){
                    //筛选出有url的event
                    JSONObject input = new JSONObject();
                    input.put("statename", state.getString("name"));//获得statename字段
                    input.put("event", eventList.getJSONObject(j).getString("name"));//获得event字段
                    input.put("url", eventList.getJSONObject(j).getString("url"));//获得url
                    input.put("tag", eventList.getJSONObject(j).getString("name"));//tag
                    inputList.add(input);
                } else {
                    continue;
                }

            }
        }
        return inputList;
    }

    public Object add() {
        ComputableModelDaoImpl modelItemDao = new ComputableModelDaoImpl(mongoTemplate);
        return modelItemDao.addDataTemplate();
    }
}

