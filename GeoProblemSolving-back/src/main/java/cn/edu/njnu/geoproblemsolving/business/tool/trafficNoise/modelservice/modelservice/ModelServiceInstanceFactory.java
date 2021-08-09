package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice;

import java.util.List;

import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.enumcollection.ModelInstanceStatus;
import com.alibaba.fastjson.JSONObject;

public class ModelServiceInstanceFactory {
	
	public static ModelServiceInstance createModelServiceInstance(JSONObject jsData,String ip, int port){
		String state = jsData.getString("state");
		String eventname = jsData.getString("event");
		String guid = jsData.getString("guid");
		String startDT = jsData.getString("start");
		String serviceID = jsData.getJSONObject("ms").getString("_id");
		List<RunningLog> logs = RunningLogFactory.convertJson2Logs(jsData.getJSONArray("log"));
		ModelInstanceStatus status = ModelServiceInstanceFactory.convertString2Status(jsData.getString("status"));
		String statusDes = jsData.getString("statusDes");
		
		return new ModelServiceInstance(state,eventname,guid,startDT,serviceID,logs,status,statusDes,ip,port);
	}
	
	public static ModelInstanceStatus convertString2Status(String cstatus){
		ModelInstanceStatus status;
		if(cstatus.equals("RUNNING")){
			status = ModelInstanceStatus.INSTA_RUNNING;
		}else if(cstatus.equals("REQUESTING")){
			status = ModelInstanceStatus.INSTA_REQUESTING;
		}else if(cstatus.equals("HANGING")){
			status = ModelInstanceStatus.INSTA_HANGING;
		}else{
			status = ModelInstanceStatus.INSTA_UNKNOWN;
		}
		return status;
	}

}
