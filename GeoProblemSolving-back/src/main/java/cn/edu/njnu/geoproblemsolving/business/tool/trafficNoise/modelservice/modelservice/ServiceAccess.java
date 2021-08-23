package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice;

import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.utils.HttpHelper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceAccess extends Service {

	 public ServiceAccess(String ip, int port){
		 super(ip,port);
	 }
	 
	 public List<ModelService> getModelServicesList(){
		 String url = this.getBaseUrl();
		 url = url + "modelser/json/all";
		 List<ModelService> list = new ArrayList<ModelService> ();
		 String response = HttpHelper.request_get(url, null);
		 //转为json对象
		 JSONObject jResponse = JSONObject.parseObject(response);
		 if(jResponse.getString("result").equals("suc")){
			 JSONArray jMss = jResponse.getJSONArray("data");
			 int count = jMss.size();
			 for(int i = 0; i < count; i++){
				 JSONObject jms = jMss.getJSONObject(i);
				 list.add(ModelServiceFactory.creatModelServiceByJSON(jms, this.getIP(), this.getPort()));
			 }
		 }else{
			 return null;
		 }
		 
		 return list;
	 }
	 
	 public ModelService getModelServiceByOID(String oid){
		 String url = this.getBaseUrl();
		 url = url + "modelser/json/" + oid;
		 String response = HttpHelper.request_get(url, null);
		 JSONObject jResponse = JSONObject.parseObject(response);
		 if(jResponse.getString("result").equals("suc")){
			 JSONObject jMs = jResponse.getJSONObject("data");
			 return ModelServiceFactory.creatModelServiceByJSON(jMs, this.getIP(),this.getPort());
		 }else{
			 return null;
		 }
	 }
	 
	 public Data getDataServiceByID(String id){
		 String url = this.getBaseUrl();
		 url = url + "geodata/json/" + id;
		 String response = HttpHelper.request_get(url, null);
		 JSONObject jResponse = JSONObject.parseObject(response);
		 if(jResponse.getString("result").equals("suc")){
			 JSONObject jData = jResponse.getJSONObject("data");
			 if(jData.toString().equals("")){
				 return null;
			 }
			 Data pData = DataFactory.createDataByJSON(jData, this.getIP(), this.getPort());
			 return pData;
		 }else{
			 return null;
		 }
	 }
	 
	 public Data uploadDataByFile(String filepath, String tag){
		 String url = this.getBaseUrl();
		 url = url + "geodata?type=file";
		 Map<String,String> textMap = new HashMap<String,String>();
		 Map<String,String> fileMap = new HashMap<String,String>();
		 
		 textMap.put("gd_tag", tag);
		 fileMap.put("myfile", filepath);
		 
		 String response = HttpHelper.request_post_file(url, fileMap, textMap);
		 JSONObject jResponse = JSONObject.parseObject(response);
		 if(jResponse.getString("result").equals("suc")){
			 String dataid = jResponse.getString("data");
			 String geturl = this.getBaseUrl();
			 geturl += "geodata/json/" + dataid;
			 String result = HttpHelper.request_get(geturl, null);
			 JSONObject jGeoData = JSONObject.parseObject(result);
			 if(jGeoData.getString("result").equals("suc")){
				 JSONObject jdata = jGeoData.getJSONObject("data");
				 Data pData = DataFactory.createDataByJSON(jdata, this.getIP(), this.getPort());
				 return pData;
			 }
		 }
		 return null;
	 }
	 
	 
	 public DataConfiguration createDataConfig(){
		 return new DataConfiguration();
	 }
	 
	 public ModelServiceRecord getModelServiceRecordByID(String msrid){
		 String url = this.getBaseUrl();
		 url += "modelserrun/json/" + msrid;
		 String response = HttpHelper.request_get(url, null);
		 JSONObject jResponse = JSONObject.parseObject(response);
		 if(jResponse.getString("result").equals("suc")){
			 JSONObject jMsr = jResponse.getJSONObject("data");
			 return ModelServiceRecordFactory.createModelServiceRecordByJSON(jMsr, this.getIP(), this.getPort());
		 }else{
			 return null;
		 }
	 }
	 
	 public ModelServiceInstance getModelServiceInstanceByGUID(String guid){
		 String url = this.getBaseUrl();
		 url += "modelins/json/" + guid;
		 String response = HttpHelper.request_get(url, null);
		 JSONObject jResponse = JSONObject.parseObject(response);
		 if(jResponse.getString("result").equals("suc")){
			 JSONObject jMsr = jResponse.getJSONObject("data");
			 return ModelServiceInstanceFactory.createModelServiceInstance(jMsr, this.getIP(), this.getPort());
		 }else{
			 return null;
		 }
	 }
}
