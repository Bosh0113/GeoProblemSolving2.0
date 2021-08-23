package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice;

import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.enumcollection.DataType;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class DataFactory {
	
	public static Data createDataByJSON(JSONObject jData, String ip, int port){
		String gd_id = jData.getString("gd_id");
		String gd_tag = jData.getString("gd_tag");
		String gd_datetime = jData.getString("gd_datetime");
		String gd_type = jData.getString("gd_type");
		DataType type = DataFactory.convertString2DataType(gd_type);
		int gd_size = jData.getInteger("gd_size");
		String gd_value = jData.getString("gd_value");
		Data pData = new Data(gd_id,gd_tag,gd_size,gd_datetime,type,gd_value,ip,port);
		return pData;
	}
	
	public static DataType convertString2DataType(String ctype){
		DataType type;
		if(ctype.equals("XML")){
			type = DataType.DAT_XML;
		}else if(ctype.equals("ZIP")){
			type = DataType.DAT_ZIP;
		}else if(ctype.equals("RAW")){
			type = DataType.DAT_RAW;
		}else{
			type = DataType.DAT_UNKNOWN;
		}
		
		return type;
	}
	
	public static DataConfiguration createDataConfigByJSON(JSONArray jConfig){
		DataConfiguration pDataConfig = new DataConfiguration();
		for(int i = 0; i < jConfig.size(); i++){
			JSONObject jdata = jConfig.getJSONObject(i);
			String stateid = jdata.getString("StateId");
			String eventname = jdata.getString("Event");
			String dataid = jdata.getString("DataId");
			boolean destoryed = Boolean.getBoolean(jdata.get("Destroyed").toString());
			pDataConfig.insertData(stateid, eventname, dataid, destoryed);
		}
		return pDataConfig;
	}

}
