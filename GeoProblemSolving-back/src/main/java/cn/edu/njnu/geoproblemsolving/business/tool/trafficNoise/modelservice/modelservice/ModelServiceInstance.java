package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice;

import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.enumcollection.ModelInstanceStatus;
import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.utils.HttpHelper;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModelServiceInstance extends Service {

	 private String _state;
	 private String _event;
	 private String _guid;
	 private String _startDT;
	 private String _serviceID;
	 private List<RunningLog> _logs;
	 private ModelInstanceStatus _status;
	 private String _statusDes;
	 
	 public ModelServiceInstance(String _state, String _event, String _guid, String _startDT,
			String _serviceID, List<RunningLog> _logs, ModelInstanceStatus _status, String _statusDes,String ip, int port) {
		super(ip, port);
		this._state = _state;
		this._event = _event;
		this._guid = _guid;
		this._startDT = _startDT;
		this._serviceID = _serviceID;
		this._logs = _logs;
		this._status = _status;
		this._statusDes = _statusDes;
	}
	 
	public String getCurrentState(){
		return this._state;
	}
	
	public String getCurrentEvent(){
		return this._event;
	}
	
	public String getGUID(){
		return this._guid;
	}
	
	public String getStartTime(){
		return this._startDT;
	}
	
	public String getModelServiceID(){
		return this._serviceID;
	}
	
	public List<RunningLog> getLogs(){
		return this._logs;
	}
	public String getStatusDes(){
		return this._statusDes;
	}
	
	public List<RunningLog> getNewLogs(){
		List<RunningLog> nlog = new ArrayList<RunningLog>();
		for(int i = 0 ; i < this._logs.size(); i++){
			boolean mark = this._logs.get(i).getMark();
			if(!mark){
				this._logs.get(i).setMark(true);
				nlog.add(this._logs.get(i));
			}
		}
		return nlog;
	}
	
	public ModelInstanceStatus getStatus(){
		return this._status;
	}
	
	public int kill() throws JSONException, IOException{
		String url = this.getBaseUrl();
		url += "modelins/" + this.getGUID() + "?ac=kill";
		String response = HttpHelper.request_put(url,null,null);
		JSONObject jResponse = JSONObject.parseObject(response);
		if(jResponse.getString("result").equals("suc")){
			return 1;
		}else if(jResponse.getString("result").equals("fail")){
			return -2;
		}
		return -1;
	}
	
	public int pause()throws JSONException, IOException{
		String url = this.getBaseUrl();
		url += "modelins/" + this.getGUID() + "?ac=pause";
		String response = HttpHelper.request_put(url,null,null);
		JSONObject jResponse = JSONObject.parseObject(response);
		if(jResponse.getString("result").equals("suc")){
			return 1;
		}else if(jResponse.getString("result").equals("fail")){
			return -2;
		}
		return -1;
	}
	public int restart()throws JSONException, IOException{
		String url = this.getBaseUrl();
		url += "modelins/" + this.getGUID() + "?ac=restart";
		String response = HttpHelper.request_put(url,null,null);
		JSONObject jResponse = JSONObject.parseObject(response);
		if(jResponse.getString("result").equals("suc")){
			return 1;
		}else if(jResponse.getString("result").equals("fail")){
			return -2;
		}
		return -1;
	}
	
	
	public void refresh(){
		String url = this.getBaseUrl();
		url += "modelins/json/" + this._guid;
		String response = HttpHelper.request_get(url, null);
		JSONObject jResponse = JSONObject.parseObject(response);
		if(jResponse.getString("result").equals("suc")){
			JSONObject mis = jResponse.getJSONObject("data");
			if(mis.toString().equals("null")){
				this._status = ModelInstanceStatus.INSTA_FINISHED;
			}else{
				this._event = mis.getString("event");
				this._state = mis.getString("state");
				this._status = ModelServiceInstanceFactory.convertString2Status(mis.getString("status"));
				this._logs = RunningLogFactory.convertJson2Logs(mis.getJSONArray("log"));
				this._statusDes = mis.getString("statusDes");
			}
		}
	}
	
	public int wait4Status(ModelInstanceStatus status, int timeout, boolean log){
		int time = 0;
		while(!this._status.getName().equals(status.getName()) && time < timeout){
			time++;
			try{
				Thread.sleep(2000);
			}catch (InterruptedException e){
				e.printStackTrace();
			}
			this.refresh();
			if(log){
				List<RunningLog> list_logs = this.getNewLogs();
				int count = list_logs.size();
				for(int i = 0; i < count; i++){
					list_logs.get(i).print();
				}
			}
		}
		if(this._status.getName().equals(status.getName())){
			return 1;
		}else{
			return -1;
		}
	}
	 
	 
	 
}
