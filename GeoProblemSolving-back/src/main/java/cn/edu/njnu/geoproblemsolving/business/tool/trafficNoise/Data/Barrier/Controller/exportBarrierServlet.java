package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Barrier.Controller;

//import cn.edu.njnu.trafficNoiseCaculating.UserData.Domain.EDataType;

import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.service.GeoAnalysisProcess;
import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.service.NodeService;
import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.service.TagUtil;
import cn.edu.njnu.geoproblemsolving.business.activity.service.ActivityDocParser;
import cn.edu.njnu.geoproblemsolving.business.resource.dao.ActivityResDaoImpl;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.resource.util.RestTemplateUtil;
import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.fileUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.prepareData.copyDbfFile;
import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.prepareData.prepareBarrierData;
import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Model.Service.runModelService.genShpZipFile;

@RestController
@RequestMapping(value = "/exportBarrierServlet")
public class exportBarrierServlet extends HttpServlet {

    @Autowired
    ActivityResDaoImpl ripDao;

    @Autowired
    GeoAnalysisProcess geoAnalysisProcess;

    @Autowired
    ActivityDocParser docParser;

    @Autowired
    NodeService nodeService;

    @Value("${dataContainer}")
    String dataContainerIp;

    final static String BARRIER_FILE_NAME = "Barrier";  //上传后道路文件命名

    @RequestMapping(method = RequestMethod.POST)
    protected String doPost(@RequestBody String data, HttpServletRequest req) throws ServletException, IOException {

        JSONObject respJson = new JSONObject();
        respJson.put("respCode", 0);

        // 解析shapefile压缩包
        try {
            JSONObject map = JSONObject.parseObject(data);
            String aid = map.getString("aid");
            String graphId = map.getString("graphId");
            String id = map.getString("uid");
            String name = map.getString("name");
            ResourceEntity input = map.getObject("input", ResourceEntity.class);
            String toolId = map.getString("toolId");
            JSONArray participant = map.getJSONArray("participant");


            String inputDataDir = req.getServletContext().getRealPath("./") + "data" + File.separator + "TrafficNoise" + File.separator;
            String barrierDir = inputDataDir + id + File.separator + "temp" + File.separator;
            if(!new File(barrierDir).exists()) {
                genShpZipFile(barrierDir, inputDataDir + id, BARRIER_FILE_NAME);
            }
            File barrierFile = new File(barrierDir + BARRIER_FILE_NAME + ".zip");


            RestTemplateUtil httpUtil = new RestTemplateUtil();
            if (barrierFile.exists()) {
                LinkedMultiValueMap<String, Object> valueMap = new LinkedMultiValueMap<>();
                String fileTemp = barrierDir + BARRIER_FILE_NAME + ".zip";
                FileSystemResource multipartFile = new FileSystemResource(fileTemp);      //临时文件
                valueMap.add("datafile", multipartFile);
                valueMap.add("name", BARRIER_FILE_NAME);
                String uploadRemoteUrl = "http://" + dataContainerIp + ":8082/data";
                //向dataContainer传输数据
                JSONObject uploadRemoteResult = httpUtil.postRequestMap(uploadRemoteUrl, valueMap).getBody();

                Integer uploadResultInfo = uploadRemoteResult.getInteger("code");
                String dataIdInContainer = uploadRemoteResult.getJSONObject("data").getString("id");
                if (!uploadResultInfo.equals(1)) {
                    respJson.put("respCode", 0);
                    respJson.put("msg", "failed.");
                    return respJson.toJSONString();
                }
                String address = "http://" + dataContainerIp + ":8082" + "/data/" + dataIdInContainer;

                // 将资源保存至活动中
                ResourceEntity outputEntity = new ResourceEntity();
                String uid = UUID.randomUUID().toString();
                outputEntity.setUserUpload(false);
                outputEntity.setUid(uid);
                outputEntity.setName(name+"_RLS90");
                outputEntity.setSuffix(".zip");
                outputEntity.setType("data");
                outputEntity.setUploadTime(new Date());
                outputEntity.setPrivacy("private");
                outputEntity.setAddress(address);
                outputEntity.setActivityId(aid);
                outputEntity.setFolder(false);
                outputEntity.setDescription("Barrier data for RLS-90 model.");
                ripDao.addResource(outputEntity);

                //文档相关操作
                HashSet<String> userIds = new HashSet<>();
                for (Object item : participant){
                    JSONObject userInfo = JSONObject.parseObject(JSONObject.toJSONString(item));
                    userIds.add(userInfo.getString("userId"));
                }
                docParser.geoAnalysis(aid, toolId, userIds, "Data processing", input, outputEntity);
                // 更新节点
                nodeService.addResToNode(aid, uid);
                //资源自动更新
                if(graphId != null && graphId != ""){
                    geoAnalysisProcess.batchResFlowAutoUpdate(graphId, aid, uid);
                }


                respJson.put("respCode", 1);
                respJson.put("path", address);
            } else {
                respJson.put("respCode", 0);
                respJson.put("msg", "failed.");
                return respJson.toJSONString();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return respJson.toString();
    }

    @RequestMapping(method = RequestMethod.GET)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
