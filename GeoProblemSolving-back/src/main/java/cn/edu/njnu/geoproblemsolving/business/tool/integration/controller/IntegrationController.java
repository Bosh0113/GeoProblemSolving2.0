package cn.edu.njnu.geoproblemsolving.business.tool.integration.controller;

import cn.edu.njnu.geoproblemsolving.business.tool.support.dto.DataApplicationFindDTO;
import cn.edu.njnu.geoproblemsolving.business.tool.support.entity.TaskRecord;
import cn.edu.njnu.geoproblemsolving.business.tool.integration.dto.IntegratedTaskAddDto;
import cn.edu.njnu.geoproblemsolving.business.tool.integration.entity.DataProcessing;
import cn.edu.njnu.geoproblemsolving.business.tool.integration.entity.ModelAction;
import cn.edu.njnu.geoproblemsolving.business.tool.integration.service.IntegrationService;
import cn.edu.njnu.geoproblemsolving.common.exception.MyException;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping(value = "/integration")
public class IntegrationController {

    @Autowired
    IntegrationService integrationService;

//    @Autowired
//    UserService userService;
//
//    @Autowired
//    ComputableModelService computableModelService;
//
//    @Autowired
//    ComputableModelDao computableModelDao;
//
    @Value("${managerServerIpAndPort}")
    private String managerServerIpAndPort;
//
//    @Value("${dataContainerIpAndPort}")
//    private String dataContainerIpAndPort;
//

    @RequestMapping(value = "/pageIntegrateTaskByUser", method = RequestMethod.GET)
    JsonResult pageIntegrateTaskByUser(@RequestParam(value = "pageNum") int pageNum,
                                       @RequestParam(value = "pageSize") int pageSize,
                                       @RequestParam(value = "asc") int asc,
                                       @RequestParam(value = "sortElement") String sortElement,
                                       HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("userId") == null) {
            return ResultUtils.error(-1, "no login");
        } else {
            String userId= session.getAttribute("userId").toString();
            return ResultUtils.success(integrationService.PageIntegrateTaskByUser(userId, pageNum, pageSize, asc, sortElement));
        }

    }

    @RequestMapping(value = "/pageIntegrateTaskByActivity", method = RequestMethod.GET)
    JsonResult pageIntegrateTaskByActivity(@RequestParam(value = "aid") String aid,
                                           @RequestParam(value = "pageNum") int pageNum,
                                       @RequestParam(value = "pageSize") int pageSize,
                                       @RequestParam(value = "asc") int asc,
                                       @RequestParam(value = "sortElement") String sortElement,
                                       HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("uid") == null) {
            return ResultUtils.error(-1, "no login");
        } else {
            return ResultUtils.success(integrationService.PageIntegrateTaskByActivity(aid, pageNum, pageSize, asc, sortElement));
        }

    }

    @RequestMapping(value = "/saveIntegratedTask", method = RequestMethod.POST)
    JsonResult saveIntegratedTask(@RequestBody IntegratedTaskAddDto integratedTaskAddDto,
                                  HttpServletRequest request
    ){
        HttpSession session = request.getSession();
        if(session.getAttribute("uid") == null){
            return ResultUtils.error(-1, "no login");
        }else {
            String userName = session.getAttribute("uid").toString();
            String xml = integratedTaskAddDto.getXml();
            String mxgraph = integratedTaskAddDto.getMxgraph();
            List<Map<String,String>> models = integratedTaskAddDto.getModels();
            List<Map<String,String>> processingTools = integratedTaskAddDto.getProcessingTools();
            List<ModelAction> modelActions = integratedTaskAddDto.getModelActions();
            List<DataProcessing> dataProcessings = integratedTaskAddDto.getDataProcessings();
            List<Map<String,Object>> dataItems = integratedTaskAddDto.getDataItems();
            List<Map<String,String>> dataLinks = integratedTaskAddDto.getDataLinks();
            String description = integratedTaskAddDto.getDescription();
            String taskName = integratedTaskAddDto.getTaskName();

            return ResultUtils.success(integrationService.saveIntegratedTask(xml, mxgraph, models,processingTools, modelActions,dataProcessings,dataItems,dataLinks,userName,taskName,description));
        }
    }

    @RequestMapping(value = "/updateIntegratedTaskInfo", method = RequestMethod.POST)
    JsonResult updateIntegratedTaskInfo(@RequestBody IntegratedTaskAddDto integratedTaskAddDto,
                                        HttpServletRequest request
    ){
        HttpSession session = request.getSession();
        if(session.getAttribute("uid") == null){
            return ResultUtils.error(-1, "no login");
        }else {
            String userName = session.getAttribute("uid").toString();
            String taskOid = integratedTaskAddDto.getTaskOid();
            String xml = integratedTaskAddDto.getXml();
            String mxgraph = integratedTaskAddDto.getMxgraph();
            List<Map<String,String>> models = integratedTaskAddDto.getModels();
            List<ModelAction> modelActions = integratedTaskAddDto.getModelActions();
            List<DataProcessing> dataProcessings = integratedTaskAddDto.getDataProcessings();
            List<Map<String,Object>> dataItems = integratedTaskAddDto.getDataItems();
            List<Map<String,String>> dataLinks = integratedTaskAddDto.getDataLinks();
            String description = integratedTaskAddDto.getDescription();
            String taskName = integratedTaskAddDto.getTaskName();

            return ResultUtils.success(integrationService.updateIntegratedTask(taskOid, xml, mxgraph, models, modelActions,dataProcessings,dataItems,dataLinks,userName,taskName,description));
        }
    }

    @RequestMapping(value = "/deleteIntegratedTask", method = RequestMethod.DELETE)
    JsonResult saveIntegratedTask(@RequestParam(value = "taskOid") String oid,
                                  HttpServletRequest request
    ){
        HttpSession session = request.getSession();
        if(session.getAttribute("uid") == null){
            return ResultUtils.error(-1, "no login");
        }else {

            return ResultUtils.success(integrationService.deleteIntegratedTask(oid));
        }
    }

    @RequestMapping(value="/runIntegratedTask",method = RequestMethod.POST)
    JsonResult runIntegratedModel(@RequestParam("file") MultipartFile file,
                                  @RequestParam("name") String name,
                                  @RequestParam("taskOid") String taskOid,
                                  HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("uid")==null) {
            return ResultUtils.error(-1, "no login");
        }
        else {
            String username = session.getAttribute("uid").toString();
            RestTemplate restTemplate=new RestTemplate();
            String url="http://" + managerServerIpAndPort + "/GeoModeling/task/runTask";//远程接口
            String suffix="."+ FilenameUtils.getExtension(file.getOriginalFilename());
            File temp=File.createTempFile("temp",suffix);
            file.transferTo(temp);
            FileSystemResource resource = new FileSystemResource(temp);
            MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
            param.add("file", resource);
            param.add("userName",username);
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param);
            ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, JSONObject.class);
            if (responseEntity.getStatusCode()!= HttpStatus.OK){
                throw new MyException("远程服务出错");
            }
            else{

                JSONObject body=responseEntity.getBody();
                if(body.getInteger("code")==-1){
                    return ResultUtils.error(-2,body.getString("msg"));
                }
                else {
                    String taskId = responseEntity.getBody().getString("data");
                    TaskRecord taskRecord = new TaskRecord();
                    taskRecord.setOid(taskOid);
                    taskRecord.setTaskId(taskId);
                    taskRecord.setComputableName(name);
                    taskRecord.setIntegrate(true);
                    taskRecord.setStatus(1);
                    taskRecord.setUserId(username);
                    taskRecord.setRunTime(new Date());
                    taskRecord.setPermission("private");
                    integrationService.save(taskRecord);
                    return ResultUtils.success(taskId);
                }
            }

        }
    }

    @RequestMapping(value = "/updateIntegrateTaskName",method = RequestMethod.POST)
    JsonResult updateIntegrateTaskName(@RequestParam(value = "taskOid")String taskOid,
                                       @RequestParam(value = "taskName")String taskName,
                                       HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session.getAttribute("uid") == null){
            return ResultUtils.error(-1, "no login");
        }else {
            String userName = session.getAttribute("uid").toString();
            return ResultUtils.success(integrationService.updateIntegrateTaskName(taskOid,taskName));
        }

    }

    @RequestMapping(value = "/updateIntegrateTaskDescription",method = RequestMethod.POST)
    JsonResult updateIntegrateTaskDescription(@RequestParam(value = "taskOid")String taskOid,
                                              @RequestParam(value = "taskDescription")String taskDescription,
                                              HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session.getAttribute("uid") == null){
            return ResultUtils.error(-1, "no login");
        }else {
            String userName = session.getAttribute("uid").toString();
            return ResultUtils.success(integrationService.updateIntegrateTaskDescription(taskOid,taskDescription));
        }

    }

    @RequestMapping(value="/checkIntegratedTask/{taskId}", method = RequestMethod.GET)
    JsonResult checkIntegratedTask(@PathVariable("taskId") String taskId,HttpServletRequest request){

        return ResultUtils.success(integrationService.checkIntegratedTask(taskId));
    }

    @RequestMapping(value = "/updateIntegrateTaskId", method = RequestMethod.POST)//把managerserver返回的taskid更新到门户数据库
    JsonResult updateIntegrateTaskId(@RequestParam("taskOid") String taskOid,
                                     @RequestParam("taskId") String taskId){
        return ResultUtils.success(integrationService.updateIntegrateTaskId(taskOid,taskId));
    }

    @RequestMapping(value = "/getIntegrateTaskByOid", method = RequestMethod.GET)
    JsonResult getIntegrateTaskByOid(@RequestParam("taskOid") String taskOid){
        return ResultUtils.success(integrationService.getIntegratedTaskByOid(taskOid));
    }

    @RequestMapping(value="/pageByClassi",method = RequestMethod.GET)
    public JsonResult pageByClassi(@RequestParam(value="asc") int asc,
                                   @RequestParam(value = "page") int page,
                                   @RequestParam(value = "size") int size,
                                   @RequestParam(value = "sortEle") String sortEle,
                                   @RequestParam(value = "searchText") String searchText,
                                   @RequestParam(value = "classification") String classification
    )
    {
        return ResultUtils.success(integrationService.pageByClassi(asc,page,size,sortEle,searchText,classification));
    }

    @RequestMapping(value = "/methods/getApplication",method = RequestMethod.POST)
    JsonResult getApplication(@RequestBody DataApplicationFindDTO dataApplicationFindDTO){
        return  ResultUtils.success(integrationService.searchDataProcessing(dataApplicationFindDTO));
    }
}
