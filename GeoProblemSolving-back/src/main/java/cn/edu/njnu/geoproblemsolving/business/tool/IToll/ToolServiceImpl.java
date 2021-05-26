package cn.edu.njnu.geoproblemsolving.business.tool.IToll;

import cn.edu.njnu.geoproblemsolving.Dao.Bulletin.IBulletinDaoImpl;
import cn.edu.njnu.geoproblemsolving.business.collaboration.compute.entity.ToolTestData;
import cn.edu.njnu.geoproblemsolving.business.collaboration.compute.util.CommonUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @ClassName ToolServiceImpl
 * @Description
 * @Author zhngzhng
 * @Date 2021/5/13
 **/
@Service
public class ToolServiceImpl implements ToolService {
    @Autowired
    ToolDaoImpl toolDao;

    @Autowired
    RestTemplate restTemplate;

    @Value("${portalLocation}")
    String portalIpAndPort;

    @Value("${dataMethodProxyServerLocation}")
    String proxyServerLocation;

    /**
     * 通过userId 在门户获取计算模型
     *
     * @param page
     * @return
     */
    @Override
    public List<JSONObject> getComputeModelByUserEmail(String email, int page, int size) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(headers);
        String url = "http://" + portalIpAndPort + "/computableModel/searchDeployedModelByUser?page=" + page
                + "&size="+ size +"&asc=-1&searchText=&userName=" + email;
        ResponseEntity<JSONObject> getResult = restTemplate.exchange(url, HttpMethod.GET, httpEntity, JSONObject.class);
        JSONObject resultData = getResult.getBody().getJSONObject("data");
        int computableModelNum = resultData.getInteger("total");
        JSONArray computableArrays = resultData.getJSONArray("content");
        List<JSONObject> computableModelList = computableArrays.toJavaList(JSONObject.class);
        //只需要返回部分特定内容即可
        return computableModelList;
    }

    @Override
    public List<JSONObject> getPublicComputeModel(int page, int size) {
        String url = "http://" + portalIpAndPort + "/computableModel/searchDeployedModel?page="+ page +"&size="+ size +"&asc=-1&searchText=";
        JSONObject getResult = restTemplate.getForEntity(url, JSONObject.class).getBody();
        JSONArray jsonArray = getResult.getJSONObject("data").getJSONArray("content");
        if (jsonArray == null){
            return null;
        }
        return jsonArray.toJavaList(JSONObject.class);
    }


    @Override
    public JSONObject queryPublicComputeModelByName(String searchText, int page) {
        String url = "http://" + portalIpAndPort + "/computableModel/searchDeployedModel?page=" + page
                + "&size=20&asc=-1&searchText=" + searchText;
        //默认 pageSize = 20
        JSONObject getResult = restTemplate.getForEntity(url, JSONObject.class).getBody();
        return getResult.getJSONObject("data");
    }

    @Override
    public List<JSONObject> getDataMethodByUserEmail(String email, int page, int size) {
        //第一步通过通过email获取用户oid
        String email2OidUrl = "http://" + portalIpAndPort + "/user/getOidByEmail?email=" + email;
        Integer oid = restTemplate.getForEntity(email2OidUrl, JSONObject.class).getBody().getInteger("data");
        //此用户从未在门户登录过，故门户无此用户
        if (oid == null) {
            return null;
        }
        String userDataMethodUrl = "http://" + portalIpAndPort + "/dataApplication/getApplication?userOid=" + oid +
                "&page="+ page +"&pagesize="+ size +"&asc=-1&type=";
        JSONObject getResult = restTemplate.getForEntity(userDataMethodUrl, JSONObject.class).getBody();
        JSONObject publicModelJson = getResult.getJSONObject("data");
        if (publicModelJson.getBoolean("empty")){
            return null;
        }
        List<JSONObject> userDataMethodList = publicModelJson.getJSONArray("content").toJavaList(JSONObject.class);
        return userDataMethodList;
    }

    @Override
    public List<JSONObject> getPublicDataMethod(int page, int size) {
        JSONObject jsonObject = queryDataMethodReq("", "", page, size);
        JSONArray dataMethodArray = jsonObject.getJSONArray("list");
        if (dataMethodArray == null){
            return null;
        }
        return dataMethodArray.toJavaList(JSONObject.class);
    }


    @Override
    public JSONObject queryPublicDataMethodByName(String searchText, int page) {
        //查询，返回
        return queryDataMethodReq(searchText, "", page, 5);
    }

    private JSONObject queryDataMethodReq(String searchText, String methodType, int page, int size){
        String url = "http://" + portalIpAndPort + "/dataApplication/methods/getApplicationInvokable";
        JSONObject searchJson = new JSONObject();
        searchJson.put("asc", false);
        searchJson.put("curQueryField", "name");
        searchJson.put("searchText",searchText);
        searchJson.put("method", methodType);
        searchJson.put("page", page);
        searchJson.put("pageSize", size);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(searchJson, headers);
        try {
            ResponseEntity<JSONObject> searchRes = restTemplate.exchange(url, HttpMethod.POST, httpEntity, JSONObject.class);
            return searchRes.getBody().getJSONObject("data");
        }catch (Exception e){
            return null;
        }
    }


    @Override
    public JSONObject getComputableModelList(String email, int page, int size) {
        List<JSONObject> userModelList = getComputeModelByUserEmail(email, page, size);
        List<JSONObject> publicModelList = getPublicComputeModel(page, size);
        JSONObject modelJson = new JSONObject();
        modelJson.put("userModel", userModelList);
        modelJson.put("publicModel", publicModelList);
        return modelJson;
    }

    @Override
    public JSONObject getDataMethodList(String email, int page, int size) {
        List<JSONObject> userDataMethod = getDataMethodByUserEmail(email, page, size);
        List<JSONObject> publicDataMethod = getPublicDataMethod(page, size);
        JSONObject modelJson = new JSONObject();
        modelJson.put("userMethod", userDataMethod);
        modelJson.put("publicMethod", publicDataMethod);
        return modelJson;
    }


    @Override
    public Tool createTool(JSONObject toolJson) throws UnsupportedEncodingException {
        Tool tool = new Tool();
        String tid = UUID.randomUUID().toString();
        String toolName = toolJson.getString("toolName");
        String description = toolJson.getString("description");
        String creator = toolJson.getString("creator");
        String privacy = toolJson.getString("privacy");
        JSONArray recomSteps = toolJson.getJSONArray("recomStep");
        ArrayList<String> recommendation = Lists.newArrayList();
        if (recomSteps != null){
            for (int i =0; i< recomSteps.size(); i++){
                String recomStep = (String)recomSteps.get(i);
                recommendation.add(recomStep);
            }
        }
        JSONArray categoryTags = toolJson.getJSONArray("categoryTag");
        ArrayList<String> tags = Lists.newArrayList();
        if (categoryTags!=null){
            for (int j = 0; j< categoryTags.size(); j++){
                String tag = (String)categoryTags.get(j);
                tags.add(tag);
            }
        }
        Date createDate = new Date();
        tool.setTid(tid);
        tool.setToolName(toolName);
        tool.setDescription(description);
        tool.setProvider(creator);
        tool.setPrivacy(privacy);
        tool.setCreatedTime(createDate);
        tool.setRecommendation(recommendation);
        tool.setTags(tags);

        //toolImage暂时先不放

        //不同类型工具特有内容
        String backendType = toolJson.getString("backendType");
        tool.setBackendType(backendType);
        JSONObject backendService = toolJson.getJSONObject("backendService");
        if (backendType.equals("webTool")){
            String toolUrl = toolJson.getString("toolUrl");
            tool.setToolUrl(toolUrl);
        }else {
            if (backendType.equals("modelItem")){
                String modelId = backendService.getString("oid");
                String modelMd5 = backendService.getString("md5");
                String author = backendService.getString("author");
                JSONObject modelMdlJson = backendService.getJSONObject("mdlJson");
                String backendServiceName = backendService.getString("name");
                //load test data 咋做的？


                tool.setBackendName(backendServiceName);
                tool.setComputableModelId(modelId);
                tool.setComputableModelMd5(modelMd5);
                tool.setBackendProvider(author);
                tool.setMdlJson(modelMdlJson);
            }else if (backendType.equals("dataMethod")){
                String backendProvider = backendService.getString("authorName");
                JSONArray invokeServices = backendService.getJSONArray("invokeServices");
                //
                if (invokeServices == null){
                    return null;
                }
                String backendServiceName = backendService.getString("name");
                JSONObject service = JSONObject.parseObject(JSONObject.toJSONString(invokeServices.get(0)));
                String dataMethodType = service.getString("method");
                String dataMethodId = service.getString("serviceId");
                String serviceToken = service.getString("token");
                tool.setBackendName(backendServiceName);
                tool.setBackendProvider(backendProvider);
                tool.setDataMethodId(dataMethodId);
                tool.setDataMethodType(dataMethodType);
                tool.setToken(serviceToken);
                String encodeToken = URLEncoder.encode(serviceToken, "UTF-8");
                //数据处理方法元数据
                String metaUrl = "http://111.229.14.128:8898/capability?id="+ dataMethodId + "&type=" + dataMethodType + "&token=" + encodeToken;
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type","application/json");
                HttpEntity httpEntity = new HttpEntity<>(headers);
                ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.exchange(metaUrl, HttpMethod.GET, httpEntity, JSONObject.class);
                if (jsonObjectResponseEntity.getStatusCode().is2xxSuccessful()){
                    JSONObject dataMethodMeta = jsonObjectResponseEntity.getBody();
                    tool.setDataMethodMeta(dataMethodMeta);
                }
                // tool.setDataMethodMeta(dataMethodMeta);

                //从数据容器读取测试数据直接存入工具库中
                ArrayList<ToolTestData> toolTestDatas = new ArrayList<>();
                String url = "http://111.229.14.128:8898/files?id=" + dataMethodId + "&token=" + encodeToken;
                JSONObject testDataJson = restTemplate.getForEntity(url, JSONObject.class).getBody();
                JSONArray testIds = testDataJson.getJSONArray("id");
                for (int i = 0; i<testIds.size(); i++){
                    ToolTestData toolTestData = new ToolTestData();
                    toolTestData.setTestDataId(UUID.randomUUID().toString());
                    JSONObject testDataId = JSONObject.parseObject(JSONObject.toJSONString(testIds.get(i)));
                    String fileName = testDataId.getString("file_name");
                    String[] fileNameSplit= fileName.split("\\.");
                    String dataUrl = testDataId.getString("url");
                    toolTestData.setFileName(fileNameSplit[0]);
                    toolTestData.setSuffix(fileNameSplit[1]);
                    toolTestData.setUrl(dataUrl);
                    toolTestDatas.add(toolTestData);
                }

                tool.setToolTestData(toolTestDatas);
            }
        }
        return toolDao.createTool(tool);
    }

    @Override
    public List<Tool> getToolByProviderService(String provider) {
        return toolDao.findAllByToolCreator(provider);
    }

    @Override
    public Tool updateToolService(Tool putTool) {
        CommonUtil commonUtil = new CommonUtil();
        String[] nullPropertyNames = commonUtil.getNullPropertyNames(putTool);
        return toolDao.updateTool(putTool, nullPropertyNames);
    }

    @Override
    public long delToolService(String tid) {
        return toolDao.deleteById(tid);
    }
}
