package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class RunningLogFactory {

	  public static RunningLog createRunningLogByJSON(JSONObject jLog){
		  String type = jLog.getString("Type");
		  String state = jLog.getString("State");
		  String event = jLog.getString("Event");
		  int flag = jLog.getInteger("Flag");
		  String message = jLog.getString("Message");
		  String datetime = jLog.getString("DateTime");
		  
		  RunningLog  pRl = new RunningLog(type,state,event,flag,message,datetime);
		  
		  return pRl;
	  }
	  
	  public static List<RunningLog> convertJson2Logs(JSONArray jsLogs){
		  List<RunningLog> logs = new ArrayList<RunningLog>();
		  
		  for(int i = 0; i < jsLogs.size(); i++){
			  JSONObject jLog = jsLogs.getJSONObject(i);
			  RunningLog log = RunningLogFactory.createRunningLogByJSON(jLog);
			  logs.add(log);
		  }
		  
		  return logs;
	  }
}
