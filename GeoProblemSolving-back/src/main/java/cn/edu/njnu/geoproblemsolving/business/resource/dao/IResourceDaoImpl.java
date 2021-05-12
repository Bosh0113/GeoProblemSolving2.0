package cn.edu.njnu.geoproblemsolving.business.resource.dao;

import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.IResourceEntity;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Repository
public class IResourceDaoImpl implements IResourceDao {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    IResourceEntity resourceEntity;

    @Value("${dataContainer}")
    String dataContainerIp;


    @Override
    public String UploadRemote(LinkedMultiValueMap<String, Object> paramMap) {
        RestTemplate restTemplate = new RestTemplate();
        String uploadUrl = "http://" + dataContainerIp + ":8082/data";
        try {
            JSONObject uploadResp = restTemplate.postForObject(uploadUrl, paramMap, JSONObject.class);
            String uploadResult = (String) uploadResp.get("msg");
            return uploadResult;
        } catch (Exception e) {
            return "ERROR";
        }

    }

    @Override
    public IResourceEntity saveResDetails(IResourceEntity resourceEntity) {
        try {
            mongoTemplate.save(resourceEntity);
            return resourceEntity;
        } catch (Exception e) {
            return null;
        }
    }



    @Override
    public ResponseEntity downSomeRemote(ArrayList<String> sourceStoreIds) {
        String oids = "";
        for (int i = 0; i < sourceStoreIds.size(); i++) {
            String oid = sourceStoreIds.get(i);
            if (i != sourceStoreIds.size() - 1) {
                oids += oid + ",";
            } else {
                oids += oid;
            }
        }
        String downRemoteUrl = "http://" + dataContainerIp + "/bulkDownload?oids=" + oids;

        // return httpUtil.getDownRemoteResult(downRemoteUrl);
        return null;
    }


    // /**
    //  * 文件下载请求
    //  * @param downRemoteUrl
    //  * @return
    //  */
    // private ResponseEntity getResponseEntity(String downRemoteUrl) {
    //     RestTemplate restTemplate = new RestTemplate();
    //     restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
    //     HttpEntity<String> entity = new HttpEntity<>(headers);
    //     ResponseEntity<byte[]> response = restTemplate.exchange(downRemoteUrl, HttpMethod.GET, entity, byte[].class);
    //     return response;
    // }


    //===============================================================================================================
    @Override
    public Object deleteRemote(String uid) {

        return null;
    }

    @Override
    public Object deleteSomeRemote(ArrayList<String> oids) {
        return null;
    }

    @Override
    public Object delResById(String resId) {
        try {
            Query query = new Query(Criteria.where("resourceId").is(resId));
            DeleteResult deleteResult = mongoTemplate.remove(query, IResourceEntity.class);
            return deleteResult.getDeletedCount()+"";
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public JsonResult inquiryResource(Map<String, String> inquiryKeyAndValue) {
        Query query = new Query();
        for (Map.Entry<String, String> entry: inquiryKeyAndValue.entrySet()){
            query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
        }
        try {
            return ResultUtils.success(mongoTemplate.find(query, ResourceEntity.class));
        }catch (Exception e){
            return ResultUtils.error(-2, "Fail");
        }
    }

    @Override
    public Object delResByPathUrl(String pathUrl) {
        DeleteResult deleteResult = mongoTemplate.remove(new Query(Criteria.where("pathURL").is(pathUrl)), IResourceEntity.class);
        return deleteResult.getDeletedCount();
    }

    /*
    ======================Resource:latest version,zheng zhong 2021/1/8 ======================     ======================
     */

    @Override
    public ArrayList<ResourceEntity> getRes(ArrayList<String> rids) {
        ArrayList<ResourceEntity> resourcePojos = new ArrayList<>();
        for (String rid: rids){
            Query query = new Query(Criteria.where("uid").is(rid));
            ResourceEntity resourcePojo = mongoTemplate.findOne(query, ResourceEntity.class);
            resourcePojos.add(resourcePojo);
        }
        return resourcePojos;
    }

    @Override
    public JsonResult saveResDetail(ResourceEntity resourcePojo) {
        ResourceEntity resource = mongoTemplate.save(resourcePojo);
        return ResultUtils.success(resource);
    }

    @Override
    public IResourceEntity findById(String rid) {
        try {
            return mongoTemplate.findOne(new Query(Criteria.where("resourceId").is(rid)), IResourceEntity.class);
        }catch(Exception e){
            throw e;
        }
    }

    @Override
    public UpdateResult updateRes(String rid, Update update) {
        Query query =  new Query(Criteria.where("resourceId").is(rid));
        return mongoTemplate.updateFirst(query, update, IResourceEntity.class);
    }

    /*
    ====================================================================================================================
     */
    // @Override
    // public Object delResByIds(ArrayList<String> resIds) {
    //     Query query = new Query();
    //     for (int i=0; i< resIds.size(); i++){
    //         String resId = resIds.get(i);
    //         Criteria criteria = Criteria.where("resourceId").is(resId);
    //         query.addCriteria(criteria);
    //     }
    //     System.out.println(query);
    //     DeleteResult deleteResult = mongoTemplate.remove(query, IResourceEntity.class);
    //     return deleteResult;
    // }
    //===============================================================================================================


}
