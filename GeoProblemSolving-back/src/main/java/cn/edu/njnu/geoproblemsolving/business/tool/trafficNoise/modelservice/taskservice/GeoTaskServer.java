package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.taskservice;

import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.utils.HttpHelper;

public class GeoTaskServer extends Service {
	
	public GeoTaskServer(String ip, int port) {
		super(ip, port);
	}
	
	
	public Task createTask(String pid, GeoDataExServer dxserver, String username){
		String url = this.getBaseUrl() + "server?pid=" + pid;
		String resJson = HttpHelper.request_get(url, null);
		JSONObject jResponse = JSONObject.parseObject(resJson);
		if(!jResponse.getString("result").equals("suc") || jResponse.getInteger("code") != 1){
			return null;
		}else{
			if (dxserver == null) {
				String result = HttpHelper.request_get(this.getBaseUrl() + "dxserver?ac=recommend", null);
				JSONObject jResult = JSONObject.parseObject(result);
				if(jResult.getString("result").equals("suc") &&jResult.getInteger("code") == 1){
					JSONObject data = jResult.getJSONObject("data");
					String ds_ip = data.getString("ds_ip");
					int ds_port = data.getInteger("ds_port");
					dxserver = new GeoDataExServer(ds_ip, ds_port);
				}else{
					return null;
				}
			}
		}
		return new Task(pid, dxserver, username, this.getIP(), this.getPort());
	}
	
	public int subscribeTask(Task task) {
		JSONObject params = new JSONObject();
		JSONArray inputsArray = task.getInputData().convertItems2JSON();
		params.put("inputs", inputsArray.toString());
		params.put("username", task.getUsername());
		params.put("pid", task.getPid());
		
		String actionURL = this.getBaseUrl() + "task";
		
		String resJson = HttpHelper.request_post_json(actionURL, params);
		JSONObject jResponse = JSONObject.parseObject(resJson);
		if(jResponse.getString("result").equals("suc")){
			String tid = jResponse.getString("data");
			task.bind(tid, "Inited");
			return 1;
		}else{
			return -1;
		}
		
	}

}
