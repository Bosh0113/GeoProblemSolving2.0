package cn.edu.njnu.geoproblemsolving.domain.remote;

import cn.edu.njnu.geoproblemsolving.domain.remote.CModel.ToolRecordDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.ModelTools.ToolRecords.ToolRecordsEntity;
import cn.edu.njnu.geoproblemsolving.Enums.ResultEnum;
import cn.edu.njnu.geoproblemsolving.Exception.MyException;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName modelItemService
 * @Description
 * @Author Zhiyi
 * @Date 2019/12/16  22:06
 * @Version 1.0.0
 */
@Service
public class ModelItemService {
    @Value("${sunlingzhiIp}")
    private String sunlingzhiIp;

    @Resource
    private MongoTemplate mongoTemplate;

    public JSONObject getModelItem(String id){
        // 获得任务服务器
        RestTemplate restTemplate = new RestTemplate();
        String urlStr = "http://" + sunlingzhiIp  + ":8082" + "/modelitem/" +id ;

        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.getForEntity(urlStr, JSONObject.class);//虚拟http请求
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.ERROR);
        }
        JSONObject result = jsonObjectResponseEntity.getBody().getJSONObject("data");
        return result;
    }

    public JSONObject getModelInstance(String id) {
        RestTemplate restTemplate = new RestTemplate();
        String urlStr = "http://" + sunlingzhiIp  + ":8082" + "/modelinstance/" + id ;

        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.getForEntity(urlStr, JSONObject.class);
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.ERROR);
        }
        JSONObject result = jsonObjectResponseEntity.getBody().getJSONObject("data");
        return result;
    }

    public JSONObject addModelInstance(JSONObject modelinstance) {
        RestTemplate restTemplate = new RestTemplate();
        String urlStr = "http://" + sunlingzhiIp  + ":8082" + "/modelinstance" ; ////Step0:根据MD5获取可用的任务服务器

        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity(urlStr,modelinstance, JSONObject.class);//虚拟http请求
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.ERROR);
        }
        JSONObject result = jsonObjectResponseEntity.getBody().getJSONObject("data");
        return result;
    }

    public JSONObject invokeModelInstance(String id) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        String urlStr = "http://" + sunlingzhiIp  + ":8082" + "/modelinstance/" +id +"/invoke";

        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity(urlStr,id, JSONObject.class);//虚拟http请求
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.ERROR);
        }
        JSONObject result = jsonObjectResponseEntity.getBody().getJSONObject("data");
        return  result;
    }

    public String saveToolRecord(JSONObject toolRecord) {
        ToolRecordDaoImpl toolRecordDao = new ToolRecordDaoImpl(mongoTemplate);
        return toolRecordDao.saveRecord(toolRecord);
    }

    public List<ToolRecordsEntity> getAllRecords(String stepId) {
        ToolRecordDaoImpl toolRecordDao = new ToolRecordDaoImpl(mongoTemplate);
        return  toolRecordDao.getAllRecords(stepId);
    }

    public JSONObject activate(JSONObject obj) {
        RestTemplate restTemplate = new RestTemplate();
        String urlStr = "http://" + sunlingzhiIp  + ":7999" + "/resource/activity" ;

        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity(urlStr,obj, JSONObject.class);//虚拟http请求
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.ERROR);
        }
        JSONObject result = jsonObjectResponseEntity.getBody().getJSONObject("data");
        return result;
    }

    public JSONArray getModelItems() {
        RestTemplate restTemplate = new RestTemplate();
        String urlStr = "http://" + sunlingzhiIp  + ":8082" + "/modelitem/all" ;

        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.getForEntity(urlStr, JSONObject.class);
        if (!jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new MyException(ResultEnum.ERROR);
        }
        JSONArray result = jsonObjectResponseEntity.getBody().getJSONArray("data");
        return result;
    }
}
