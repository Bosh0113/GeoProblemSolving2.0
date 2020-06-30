package cn.edu.njnu.geoproblemsolving.domain.modeltask;

//import cn.edu.njnu.geoproblemsolving.Dao.CModel.ModelItemDao;

import cn.edu.njnu.geoproblemsolving.Enums.ResultEnum;
import cn.edu.njnu.geoproblemsolving.Exception.MyException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


@Service
public class ModelTaskService {

//    @Autowired
//    ModelItemDao modelItemDao;

//    @Value("${managerServerIpAndPort}")
//    private String managerServer;

    @Value("${managerServerIpAndPort}")
    private String managerServerIpAndPort;

    @Resource
    private MongoTemplate mongoTemplate;
//    public JSONArray getAllModel(){
//        String urlStr = "http://" + managerServerIpAndPort + "/GeoModeling/taskNode/getAllServices";//获得服务容器中的所有模型
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.getForEntity(urlStr,  JSONObject.class);
//        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
//            throw new MyException(ResultEnum.ERROR);
//        }
//        JSONArray result = jsonObjectResponseEntity.getBody().getJSONArray("data");
//
//        ModelItemDaoImpl modelItemDao = new ModelItemDaoImpl(mongoTemplate);
//        JSONArray modelList = new JSONArray();
//        for (int i = 0; i < result.size(); i++) {
//            String modelPid = result.getString(i);
//            Object computableModel = modelItemDao.readComputableModel(modelPid);
//            modelList.add(computableModel);
//        }
//
//        return modelList;
//    }

    public JSONObject getComputeModel(String doi) {//根据pid.md5获得
        RestTemplate restTemplate = new RestTemplate();
        String urlStr = "http://geomodeling.njnu.edu.cn/computableModel/getInfo/" + doi; ////Step0:根据MD5获取可用的任务服务器

        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.getForEntity(urlStr, JSONObject.class);//虚拟http请求
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.ERROR);
        }
        JSONObject result = jsonObjectResponseEntity.getBody().getJSONObject("data");

        String md5 = result.getString("md5");
        String modelname = result.getString("name");
//        String mdl = result.getString("mdl");
        JSONObject mdlJson = JSON.parseObject(JSONObject.toJSONString(result.get("mdlJson")));
        JSONObject behavior = mdlJson.getJSONArray("ModelClass").getJSONObject(0).getJSONArray("Behavior").getJSONObject(0).getJSONArray("StateGroup").getJSONObject(0);
        JSONObject result2 = JSON.parseObject(JSONObject.toJSONString(behavior));
        return result;
    }

    public JSONObject createTask(String pid ,String userId) {
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
        initTaskJson.put("username", userId);

        urlStr = "http://" + managerServerIpAndPort + "/GeoModeling/computableModel/createTask";//Step1:createTask
        jsonObjectResponseEntity = restTemplate.postForEntity(urlStr, initTaskJson, JSONObject.class);
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.ERROR);
        }
        result = jsonObjectResponseEntity.getBody().getJSONObject("data");
        //保存url到相应的event和value
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

    public JSONObject getRecord(JSONObject data) {
        String urlStr = "http://" + managerServerIpAndPort + "/GeoModeling/computableModel/refreshTaskRecord"; //Step4:根据task id去查询模型运行记录
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity(urlStr, data, JSONObject.class);
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.ERROR);
        }
        JSONObject result = jsonObjectResponseEntity.getBody();
        return result;
    }

    public String upload(File file) {
        FileSystemResource resource = new FileSystemResource(file);      //临时文件
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("serverNode", "china");
        form.add("userId", 2);
        form.add("ogmsdata", resource);
        form.add("name", "zzyTest");
        form.add("origination","GeoProblemSolving");

     String urlStr = "http://111.229.14.128:8899/data";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity(urlStr, form, JSONObject.class);
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.ERROR);
        }
        JSONObject result = jsonObjectResponseEntity.getBody();//获得上传数据的URL
        String urlResult = result.getJSONObject("data").getString("url");
        return urlResult;
    }

    public Object uploadFileForm(Collection<Part> parts) throws IOException {
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        for (Part part : parts) {
            String header = part.getHeader("Content-Disposition");
            String filename2 = header.substring(header.indexOf("filename=\"") + 10,header.lastIndexOf("\""));//filename=" (整个字符串长度为10，所以要加10)
            // 获取文件名
            String fileName = part.getName();
            //  获取文件后缀名
            String suffix ="." + FilenameUtils.getExtension(filename2);
            File file=File.createTempFile(part.getName(),suffix);//创建临时文件
            FileUtils.copyInputStreamToFile(part.getInputStream(),file);
            FileSystemResource fileSystemResource=new FileSystemResource(file);
            form.add("ogmsdata",fileSystemResource);
        }

        form.add("serverNode", "china");
        form.add("userId", "2");
        form.add("name", "");
        form.add("origination","GeoProblemSolving");

        String urlStr = "http://111.229.14.128:8899/data";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity(urlStr, form, JSONObject.class);
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.ERROR);
        }
        JSONObject result = jsonObjectResponseEntity.getBody();//获得上传数据的URL
        String resultId = result.getJSONObject("data").getString("source_store_id");
        return resultId;
    }

//    public JSONObject slefInvoke(JSONObject obj) {
//        //传入task在初始化页面时已经创建了Task 不需要再创建且获得了上传数据的URL
//        JSONObject invokeBody = new JSONObject();
//        JSONObject modelInstance =  obj.getJSONObject("modelItem");
//
//        modelItemDao.updateComputableModel(modelInstance);//更新数据库
//
//        invokeBody.put("inputs", getInvokeItem(modelInstance));//上传数据的 inputs数组
//        invokeBody.put("ip", obj.getString("ip"));
//        invokeBody.put("port", obj.getString("port"));
//        invokeBody.put("pid", obj.getString("pid"));
//        invokeBody.put("username", "testzzy");
//        String tid = invoke(invokeBody);
//
//        JSONObject refreshBody = new JSONObject();
//        refreshBody.put("tid", tid);
//        refreshBody.put("ip",obj.getString("ip"));
//        refreshBody.put("port", obj.getString("port"));
//        return refresh(refreshBody);
//    }

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

//    public Object add() {
//        ComputableModelDaoImpl modelItemDao = new ComputableModelDaoImpl(mongoTemplate);
//        return modelItemDao.addDataTemplate();
//    }

//    public Object download(String uid) {
//        String urlStr = "http://111.229.14.128:8899/data";
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity(urlStr, form, JSONObject.class);
//        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
//            throw new MyException(ResultEnum.ERROR);
//        }
//        JSONObject result = jsonObjectResponseEntity.getBody();//获得上传数据的URL
//        String resultId = result.getJSONObject("data").getString("source_store_id");
//        return resultId;
//    }
}

