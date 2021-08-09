package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice;

import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.enumcollection.ModelServiceLimitation;
import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.enumcollection.ModelServicePermission;
import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.enumcollection.ModelServiceStatus;
import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.enumcollection.Platform;
import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.utils.HttpHelper;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author wang ming
 *
 */
public class ModelService extends Service {

	private String _oid;
	private String _name;
	private String _type;
	private String _url;
	private String _pid;
	private String _mid;
	private boolean _registered;
	private String _description;
	private String _version;
	private Platform _platform;
	private String _deploymentTime;
	private String _img;
	private String _deployorName;
	private String _deployorEmail;
	private ModelServiceStatus _status;
	private ModelServiceLimitation _limit;
	private ModelServicePermission _permission;
	public ModelService(String _oid, String _name, String _type, String _url, String _pid,
			String _mid, boolean _registered, String _description, String _version, Platform _platform,
			String _deploymentTime, String _img, String _deployorName, String _deployorEmail,
			ModelServiceStatus _status, ModelServiceLimitation _limit, ModelServicePermission _permission,String ip, int port) {
		super(ip, port);
		this._oid = _oid;
		this._name = _name;
		this._type = _type;
		this._url = _url;
		this._pid = _pid;
		this._mid = _mid;
		this._registered = _registered;
		this._description = _description;
		this._version = _version;
		this._platform = _platform;
		this._deploymentTime = _deploymentTime;
		this._img = _img;
		this._deployorName = _deployorName;
		this._deployorEmail = _deployorEmail;
		this._status = _status;
		this._limit = _limit;
		this._permission = _permission;
	}
	
	public String getServiceOID(){
		return this._oid;
	}
	
	public String getServiceName(){
		return this._name;
	}
	
	public String getServiceType(){
		return this._type;
	}
	
	public String getServiceDetailURL(){
		return this._url;
	}
	
	public String getServicePid(){
		return this._pid;
	}
	
	public String getServiceMid(){
		return this._mid;
	}
	
	public boolean getServiceRegister(){
		return this._registered;
	}
	
	public String getServiceDescription(){
		return this._description;
	}
	
	public String getServiceVersion(){
		return this._version;
	}
	
	public Platform getServicePlatform(){
		return this._platform;
	}
	
	public String getDeploymentTime(){
		return this._deploymentTime;
	}
	
	public String getImage(){
		return this._img;
	}
	
	public String getServiceDeployorName(){
		return this._deployorName;
	}
	
	public String getServiceDeployorEmail(){
		return this._deployorEmail;
	}
	
	public ModelServiceStatus getServiceStatus(){
		return this._status;
	}
	
	public ModelServiceLimitation getServiceLimitation(){
		return this._limit;
	}
	
	public ModelServicePermission getServicePermission(){
		return this._permission;
	}
	
	/**
	 * @param config
	 * @return recordid 模型服务记录id
	 */
	public String invoke(DataConfiguration config){
		String recordid = "";
		String url = this.getBaseUrl();
		url += "modelser/" + this.getServiceOID() + "?ac=run&inputdata=[";
		List<DataConfigItem> items = config.getItem();
		for(int i = 0; i < items.size(); i++){
			DataConfigItem item = items.get(i);
			if(i == items.size() - 1){
				url = url + "{\"StateId\":\"" + item.state + "\",\"Event\":\"" + item.event + "\",\"DataId\":\"" + item.data + "\",\"Destroyed\":\"false\"}";
			}else{
				url = url + "{\"StateId\":\"" + item.state + "\",\"Event\":\"" + item.event + "\",\"DataId\":\"" + item.data + "\",\"Destroyed\":\"false\"},";
			}
		}
		url = url + "]&auth=";
		String response = HttpHelper.request_get(url, null);
		JSONObject jResponse = JSONObject.parseObject(response);
		if(jResponse.getString("result").equals("suc")){
			recordid = jResponse.getString("data");
		}
		return recordid;
	}
	
	public int refresh(){
		String url = this.getBaseUrl();
		url += "modelser/json/" + this.getServiceOID();
		String response = HttpHelper.request_get(url, null);
		JSONObject jResponse = JSONObject.parseObject(response);
		if(jResponse.getString("result").equals("suc")){
			JSONObject jMs = jResponse.getJSONObject("data");
			int msstatus = jMs.getInteger("ms_status");
			int mslimit = jMs.getInteger("ms_limited");
			int mspermission = jMs.getInteger("ms_permission");
			ModelServiceStatus status;
			if(msstatus == 0){
				status = ModelServiceStatus.STAT_OFFLINE;
			}else if(msstatus == 1){
				status = ModelServiceStatus.STAT_ONLINE;
			}else{
				status = ModelServiceStatus.STAT_DELETE;
			}
			ModelServiceLimitation limit;
			if(mslimit == 0){
				limit = ModelServiceLimitation.LMT_PRIVATE;
			}else{
				limit = ModelServiceLimitation.LMT_PUBLIC;
			}
			ModelServicePermission permission;
			if(mspermission == 0){
				permission = ModelServicePermission.PMS_OPEN;
			}else{
				permission = ModelServicePermission.PMS_PERMISSION;
			}
			this._status = status;
			this._limit = limit;
			this._permission = permission;
		}else{
			return -1;
		}
		return 1;
	}
	
	
}
