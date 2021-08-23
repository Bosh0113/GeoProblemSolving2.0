package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice;

import java.util.List;

import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.enumcollection.RecordStatus;
import com.alibaba.fastjson.JSONObject;

public class ModelServiceRecordFactory {
	
	public static ModelServiceRecord createModelServiceRecordByJSON(JSONObject jMsr,String ip, int port){
		String oid = jMsr.getString("_id");
		String ms_id = jMsr.getString("ms_id");
		String datetime = jMsr.getString("msr_datetime");
		double span = jMsr.getDouble("msr_span");
		String guid = jMsr.getString("msr_guid");
		DataConfiguration pInput = DataFactory.createDataConfigByJSON(jMsr.getJSONArray("msr_input"));
		DataConfiguration pOutput = DataFactory.createDataConfigByJSON(jMsr.getJSONArray("msr_output"));
		RecordStatus restatus;
		if(jMsr.getInteger("msr_status") == 1){
			restatus = RecordStatus.MSRS_FINISHED;
		}else if(jMsr.getInteger("msr_status") == 0){
			restatus = RecordStatus.MSRS_UNFINISHED;
		}else{
			restatus = RecordStatus.MSRS_ERROR;
		}
		String stdout = jMsr.getJSONObject("msr_runninginfo").getString("StdOut");
		String stderr = jMsr.getJSONObject("msr_runninginfo").getString("StdErr");
		String invokerr = jMsr.getJSONObject("msr_runninginfo").getString("InvokeErr");
		
		List<RunningLog> list_log = RunningLogFactory.convertJson2Logs(jMsr.getJSONArray("msr_logs"));
		
		ModelServiceRecord pMsr = new ModelServiceRecord(oid,ms_id,datetime,span,guid,pInput,pOutput,
				restatus,stdout,stderr,invokerr,list_log,ip,port) ;
		return pMsr;
	}

}
