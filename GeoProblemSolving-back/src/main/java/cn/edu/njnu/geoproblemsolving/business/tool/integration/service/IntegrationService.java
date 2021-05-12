package cn.edu.njnu.geoproblemsolving.business.tool.integration.service;

import cn.edu.njnu.geoproblemsolving.business.tool.integration.entity.DataProcessing;
import cn.edu.njnu.geoproblemsolving.business.tool.integration.entity.IntegratedTask;
import cn.edu.njnu.geoproblemsolving.business.tool.integration.entity.ModelAction;
import cn.edu.njnu.geoproblemsolving.business.tool.integration.repository.IntegratedTaskDao;
import cn.edu.njnu.geoproblemsolving.business.tool.support.dto.DataApplicationFindDTO;
import cn.edu.njnu.geoproblemsolving.business.tool.support.entity.ModelService;
import cn.edu.njnu.geoproblemsolving.business.tool.support.entity.TaskRecord;
import cn.edu.njnu.geoproblemsolving.business.tool.support.repository.TaskRecordDao;
import cn.edu.njnu.geoproblemsolving.common.enums.ResultEnum;
import cn.edu.njnu.geoproblemsolving.common.exception.MyException;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.*;

import static cn.edu.njnu.geoproblemsolving.business.modeltask.Utils.convertMdl;


@Service
public class IntegrationService {

//    @Autowired
//    ComputableModelService computableModelService;
//
//    @Autowired
//    UserService userService;
//
//    @Autowired
//    ComputableModelDao computableModelDao;
//
//    @Autowired
//    UserDao userDao;
//
    @Autowired
    TaskRecordDao taskRecordDao;
//
//    @Autowired
//    DataItemDao dataItemDao;

    @Autowired
    IntegratedTaskDao integratedTaskDao;

    @Value("${managerServerIpAndPort}")
    private String managerServer;

    @Value("${resourcePath}")
    private String resourcePath;

    @Value("${managerServerIpAndPort}")
    private String managerServerIpAndPort;

    @Value("${dataServerManager}")
    private String dataServerManager;

    @Value("${dataContainerIpAndPort}")
    private String dataContainerIpAndPort;

    //可以可视化的数据模板
    @Value("#{'${visualTemplateIds}'.split(',')}")
    private String[] visualTemplateIds;

    public String save(TaskRecord task) {

        taskRecordDao.save(task);
        return "suc";
    }

    public JSONObject PageIntegrateTaskByActivity(String aid, int pageNum, int pageSize, int asc, String sortElement){
        Sort sort = new Sort(asc==1? Sort.Direction.ASC:Sort.Direction.DESC,sortElement);
        Pageable pageable = PageRequest.of(pageNum,pageSize,sort);
        Page<IntegratedTask> integratedTaskPage = integratedTaskDao.findByAid(aid, pageable);

        JSONObject result = new JSONObject();
        result.put("total",integratedTaskPage.getTotalElements());
        result.put("content",integratedTaskPage.getContent());

        return result;
    }

    public JSONObject PageIntegrateTaskByUser(String userId, int pageNum, int pageSize, int asc, String sortElement){
        Sort sort = new Sort(asc==1? Sort.Direction.ASC:Sort.Direction.DESC,sortElement);
        Pageable pageable = PageRequest.of(pageNum,pageSize,sort);
        Page<IntegratedTask> integratedTaskPage = integratedTaskDao.findByUserId(userId, pageable);

        JSONObject result = new JSONObject();
        result.put("total",integratedTaskPage.getTotalElements());
        result.put("content",integratedTaskPage.getContent());

        return result;
    }

    public String saveIntegratedTask(String xml, String mxgraph, List<Map<String,String>> models, List<Map<String,String>> processingTools,
                                     List<ModelAction> modelActions, List<DataProcessing> dataProcessings, List<Map<String,Object>> dataItems, List<Map<String,String>> dataLinks, String aid, String userId, String taskName, String description){
        IntegratedTask integratedTask = new IntegratedTask();

        integratedTask.setOid(UUID.randomUUID().toString());
        integratedTask.setModels(models);
        integratedTask.setProcessingTools(processingTools);
        integratedTask.setModelActions(modelActions);
        integratedTask.setDataProcessings(dataProcessings);
        integratedTask.setDataItems(dataItems);
        integratedTask.setDataLinks(dataLinks);
        integratedTask.setXml(xml);
        integratedTask.setMxGraph(mxgraph);
        integratedTask.setStatus(0);
        integratedTask.setAid(aid);
        integratedTask.setUserId(userId);
        integratedTask.setTaskName(taskName);
        integratedTask.setDescription(description);
        Date now = new Date();
        if(integratedTask.getCreateTime()==null){
            integratedTask.setCreateTime(now);
        }
        integratedTask.setLastModifiedTime(now);

        return integratedTaskDao.save(integratedTask).getOid();

    }

    public IntegratedTask updateIntegratedTask( String taskOid, String xml, String mxgraph, List<Map<String,String>> models,
                                                List<ModelAction> modelActions,List<DataProcessing> dataProcessings, List<Map<String,Object>> dataItems,List<Map<String,String>> dataLinks,String userName,String taskName,String description){
        IntegratedTask integratedTask = integratedTaskDao.findByOid(taskOid);

        integratedTask.setModels(models);
        integratedTask.setModelActions(modelActions);
        integratedTask.setDataProcessings(dataProcessings);
        integratedTask.setDataLinks(dataLinks);
        integratedTask.setDataItems(dataItems);
        integratedTask.setXml(xml);
        integratedTask.setMxGraph(mxgraph);
        integratedTask.setTaskName(taskName);
        integratedTask.setDescription(description);

        Date now = new Date();
        integratedTask.setLastModifiedTime(now);

        return integratedTaskDao.save(integratedTask);

    }

    public int deleteIntegratedTask(String oid){
        IntegratedTask integratedTask = integratedTaskDao.findByOid(oid);
        if (integratedTask != null) {
            integratedTaskDao.delete(integratedTask);
            return 1;
        } else {
            return -1;
        }
    }

    public String updateIntegrateTaskName(String taskOid,String taskName) {
        IntegratedTask integratedTask = integratedTaskDao.findByOid(taskOid);

        integratedTask.setTaskName(taskName);
        Date now = new Date();
        integratedTask.setLastModifiedTime(now);

        integratedTaskDao.save(integratedTask);
        return taskName;
    }

    public String updateIntegrateTaskDescription(String taskOid,String taskDescription) {
        IntegratedTask integratedTask = integratedTaskDao.findByOid(taskOid);

        integratedTask.setDescription(taskDescription);
        Date now = new Date();
        integratedTask.setLastModifiedTime(now);

        integratedTaskDao.save(integratedTask);
        return taskDescription;
    }

    //从managerserver获取task的最新状态
    public JSONObject checkIntegratedTask(String taskId){
        RestTemplate restTemplate=new RestTemplate();
        String url="http://" + managerServerIpAndPort + "/GeoModeling/task/checkTaskStatus?taskId={taskId}";//远程接口
        Map<String, String> params = new HashMap<>();
        params.put("taskId", taskId);
        ResponseEntity<JSONObject> responseEntity=restTemplate.getForEntity(url,JSONObject.class,params);
        if (responseEntity.getStatusCode()!= HttpStatus.OK){
            throw new MyException("远程服务出错");
        }
        else {
            IntegratedTask task=integratedTaskDao.findByTaskId(taskId);
            JSONObject data = responseEntity.getBody().getJSONObject("data");
            int status = data.getInteger("status");
            JSONObject taskInfo = data.getJSONObject("taskInfo");

            //更新output
            JSONObject j_modelActionList = taskInfo.getJSONObject("modelActionList");
            List<ModelAction> finishedModelActions = converseOutputModelAction(j_modelActionList.getJSONArray("completed"));
            List<ModelAction> failedModelActions = converseOutputModelAction(j_modelActionList.getJSONArray("failed"));
            updateIntegratedTaskOutput(task,finishedModelActions,failedModelActions);

            // save to resource
            // ????????????

            //todo common task 与 integrated task的合并
            TaskRecord comTask = taskRecordDao.findFirstByTaskId(task.getOid());
            switch (status){
                case 0:
                    break;
                case -1:
                    task.setStatus(-1);
                    comTask = taskRecordDao.findFirstByTaskId(task.getOid());
                    comTask.setStatus(-1);
                    integratedTaskDao.save(task);
                    taskRecordDao.save(comTask);
                    break;
                case 1:
                    task.setStatus(2);
                    integratedTaskDao.save(task);
                    comTask = taskRecordDao.findFirstByTaskId(task.getOid());
                    comTask.setStatus(2);
                    integratedTaskDao.save(task);
                    taskRecordDao.save(comTask);
                    break;
            }
            return data;
        }
    }

    private List<ModelAction> converseOutputModelAction(JSONArray modelActionArray) {
        List<ModelAction> modelActionList = new ArrayList<>();
        for (int i = 0; i < modelActionArray.size(); i++) {
            JSONObject fromModelAction = modelActionArray.getJSONObject(i);
            ModelAction modelAction = new ModelAction();
            modelAction.setId(fromModelAction.getString("id"));
            // output data
            JSONArray output = fromModelAction.getJSONObject("outputData").getJSONArray("outputs");
            List<Map<String,Object>> outputDatas = new ArrayList<>();
            for(int j=0;j<output.size();j++){
                Map<String,Object> outputData = new HashMap<>();
                JSONObject j_dataContent = ((JSONObject)output.get(j)).getJSONObject("dataContent");
                outputData.put("value",j_dataContent.getString("value"));
                outputData.put("type",j_dataContent.getString("type"));
                outputData.put("fileName",j_dataContent.getString("fileName"));
                outputData.put("suffix",j_dataContent.getString("suffix"));
                outputDatas.add(outputData);
            }
            modelAction.setOutputData(outputDatas);
            modelActionList.add(modelAction);
        }

        return modelActionList;
    }

    private String updateIntegratedTaskOutput(IntegratedTask integratedTask, List<ModelAction> finishedModelActions,List<ModelAction> failedModelActions ){
        List<ModelAction> modelActions = integratedTask.getModelActions();

        for(ModelAction modelAction:modelActions){
            for(ModelAction finishedModelAction:finishedModelActions){
                if(modelAction.getId().equals(finishedModelAction.getId())){
                    modelAction.setStatus(finishedModelAction.getStatus());
                    modelAction.setPort(finishedModelAction.getPort());
                    modelAction.setTaskIp(finishedModelAction.getTaskIp());
                    for(Map<String,Object> output:modelAction.getOutputData()){
                        for(Map<String,Object> newOutput:finishedModelAction.getOutputData()){
                            output.put("value",newOutput.get("value"));
                            output.put("fileName",newOutput.get("fileName"));
                            output.put("suffix",newOutput.get("suffix"));
                        }
                    }
                }
            }
            for(ModelAction failedModelAction:failedModelActions){
                if(modelAction.getId().equals(failedModelAction.getId())){
                    modelAction.setStatus(-1);
                }
            }
        }

        Date now = new Date();
        integratedTask.setLastModifiedTime(now);

        return integratedTaskDao.save(integratedTask).getOid();
    }

    public String updateIntegrateTaskId(String taskOid, String taskId){
        IntegratedTask integratedTask = integratedTaskDao.findByOid(taskOid);

        integratedTask.setTaskId(taskId);
        Date now = new Date();
        integratedTask.setLastModifiedTime(now);

        integratedTaskDao.save(integratedTask);
        return taskId;
    }

    public IntegratedTask getIntegratedTaskByOid(String taskOid){
        IntegratedTask integratedTask = integratedTaskDao.findByOid(taskOid);

        return integratedTask;
    }

    public JSONObject pageByClassi(int asc, int page, int pageSize, String sortEle, String searchText, String classification){
        RestTemplate restTemplate = new RestTemplate();
        String urlStr = "http://172.21.212.167:8080/computableModel/pageByClassi?asc=" + asc + "&page=" +page+ "&size=" + pageSize +"&sortEle=" + sortEle + "&searchText=" + searchText + "&classification=" + classification;

        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.getForEntity(urlStr, JSONObject.class);//虚拟http请求
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.ERROR);
        }
        JSONObject result = jsonObjectResponseEntity.getBody().getJSONObject("data");
        return result;
    }

    public JSONObject searchDataProcessing(DataApplicationFindDTO dataApplicationFindDTO){
        RestTemplate restTemplate = new RestTemplate();
        String urlStr = "http://172.21.212.167:8080/dataApplication/methods/getApplicationInvokable";

        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity(urlStr, dataApplicationFindDTO, JSONObject.class);
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.ERROR);
        }
        JSONObject result = jsonObjectResponseEntity.getBody().getJSONObject("data");
        return result;
    }

    public JSONObject checkNodeContent(String serverId, String token, String type){

        String contentType = type.equals("Visualization")?"Visualization":"Processing";

        String url = "http://"+dataServerManager+"/capability?"+"id="+serverId+"&token="+ URLEncoder.encode(token)+"&type="+contentType;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json");
        Map<String,String> mheader = new HashMap<>();
        mheader.put("Content-Type","application/json");

        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<MultiValueMap>(null, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(6000);// 设置超时
        requestFactory.setReadTimeout(6000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        JSONObject result = new JSONObject();

        try{
            ResponseEntity<JSONObject> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, JSONObject.class);
            JSONObject j_result = response.getBody();

            try{
                int code = j_result.getInteger("code");
                if(code==0){
                    JSONObject capability = j_result.getJSONObject("Capability");
                    result.put("content",capability.get("data"));
                }else{
                    result.put("content","offline");
                }

            }catch (Exception e){
                result.put("content","offline");
            }
        }catch (Exception e){
            result.put("content","offline");
        }
        return result;
    }

//    public JSONArray getNodeContentCheck(String token, String type) throws Exception{
//        //因为dataservice不提供直接查询接口，因此只能先找token再遍历
//        String baseUrl = "http://" + dataServerManager + "/onlineNodesAllPcs";
//        JSONArray j_nodes = new JSONArray();
//
//        String url = baseUrl + "?token=" + URLEncoder.encode(token) + "&type=" + type;
//        String xml = null;
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type","application/json");
//        Map<String,String> mheader = new HashMap<>();
//        mheader.put("Content-Type","application/json");
//
//        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<MultiValueMap>(null, headers);
//        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//        requestFactory.setConnectTimeout(6000);// 设置超时
//        requestFactory.setReadTimeout(6000);
//        RestTemplate restTemplate = new RestTemplate(requestFactory);
//        ResponseEntity<JSONObject> response = restTemplate.exchange(url,HttpMethod.GET, requestEntity, JSONObject.class);
//        JSONObject j_result = response.getBody();
//
//        try{
//            xml = MyHttpUtils.GET(url, "utf-8", null);
//        }catch (Exception e){
//            return null;
//        }
//
//        JSONObject jsonObject = XmlTool.xml2Json(xml);
//        JSONArray j_processings = new JSONArray();
//        JSONArray result = new JSONArray();
//        try {
//            j_processings = jsonObject.getJSONArray("AvailablePcs");
//            result = updateUserNodeContent(j_processings,token,type);
//        } catch (Exception e) {
//            JSONObject j_processing = jsonObject.getJSONObject("AvailablePcs");
//            j_processing.put("token", token);
//            DataNodeContent dataNodeContent = dataNodeContentDao.findAllByServerIdAndToken(j_processing.getString("id"),token);
//
//            if(dataNodeContent!=null){
//                dataNodeContent.setChecked(1);
//                j_processing.put("bindItems",dataNodeContent.getBindItems());
//            }
//            List<DataNodeContent> dataNodeContentList = dataNodeContentDao.findAllByTokenAndType(token,type);
//            for(int i=0;i<dataNodeContentList.size();i++){
//                DataNodeContent content = dataNodeContentList.get(i);
//                if(dataNodeContent.getChecked()==0){
//                    dataNodeContentDao.delete(content);
//                }else {
//                    content.setChecked(0);
//                }
//
//            }
//            result.add(j_processing);
//        }
//        return result;
//    }
//
//    public JSONArray updateUserNodeContent(JSONArray nodeContents,String token,String type){
//        JSONArray result = new JSONArray();
//        for (int i = 0; nodeContents != null && i < nodeContents.size(); i++) {
//            JSONObject j_process = nodeContents.getJSONObject(i);
//            j_process.put("token", token);
//            DataNodeContent dataNodeContent = dataNodeContentDao.findAllByServerIdAndToken(j_process.getString("id"),token);
//
//            if(dataNodeContent!=null){
//                dataNodeContent.setChecked(1);
//                dataNodeContentDao.save(dataNodeContent);
//                j_process.put("bindItems",dataNodeContent.getBindItems());
//            }
//            result.add(j_process);
//        }
//
//        List<DataNodeContent> dataNodeContentList = dataNodeContentDao.findAllByTokenAndType(token,type);
//        for(int i=0;i<dataNodeContentList.size();i++){
//            DataNodeContent dataNodeContent = dataNodeContentList.get(i);
//            if(dataNodeContent.getChecked()==0){
//                dataNodeContentDao.delete(dataNodeContent);
//            }else {
//                dataNodeContent.setChecked(0);
//            }
//
//        }
//
//        return result;
//    }
}
