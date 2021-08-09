package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.taskservice;

import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice.Service;
import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.utils.HttpHelper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.DecoderException;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class Task extends Service {
	
	private String pid;
	
	private GeoDataExServer dxServer;
	
	private ExDataConfiguration inputData;
	
	private ExDataConfiguration outputData;
	
	private String username;
	
	private String tid;
	
	private TaskStatus status;
	
	

	public Task(String pid, GeoDataExServer dxServer, String username, String ip, int port) {
		super(ip, port);
		this.pid = pid;
		this.dxServer = dxServer;
		this.inputData = new ExDataConfiguration();
		this.outputData = new ExDataConfiguration();
		this.username = username;
		this.tid = "";
		this.status = null;
	}
	
	public Task(String pid, GeoDataExServer dxServer, ExDataConfiguration inputData,
			ExDataConfiguration outputData, String username, String tid, TaskStatus status, String ip, int port) {
		super(ip, port);
		this.pid = pid;
		this.dxServer = dxServer;
		this.inputData = inputData;
		this.outputData = outputData;
		this.username = username;
		this.tid = tid;
		this.status = status;
	}


	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public GeoDataExServer getDxServer() {
		return dxServer;
	}

	public void setDxServer(GeoDataExServer dxServer) {
		this.dxServer = dxServer;
	}

	public ExDataConfiguration getInputData() {
		return inputData;
	}

	public void setInputData(ExDataConfiguration inputData) {
		this.inputData = inputData;
	}

	public ExDataConfiguration getOutputData() {
		return outputData;
	}

	public void setOutputData(ExDataConfiguration outputData) {
		this.outputData = outputData;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	
	public int configInputData(String statename, String event, String dataPath, String tag){
		
		try {
			ExData exData = this.dxServer.upload(dataPath, tag);
			String url = exData.getURL();
			this.inputData.insertData(statename, event, url, tag);
			return 1;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return -1;
		} catch (DecoderException e) {
			e.printStackTrace();
			return -1;
		} 
	}
	
	public int refresh() {
		String url_origin = this.getBaseUrl() + "/task/" + this.tid;
		String response = HttpHelper.request_get(url_origin, null);
		JSONObject jResponse = JSONObject.parseObject(response);
		if(jResponse.getString("result").equals("suc")){
			JSONObject jData = jResponse.getJSONObject("data");
			TaskStatus taskStatus;
			String status = jData.getString("t_status");
			if(status.equals("Inited")){
				taskStatus = TaskStatus.TASK_INITED;
			}else if(status.equals("Started")){
				taskStatus = TaskStatus.TASK_STARTED;
			}else if (status.equals("Finished")) {
				taskStatus = TaskStatus.TASK_FINISHED;
			}else {
				taskStatus = TaskStatus.TASK_ERROR;
			}
			this.status = taskStatus;
			JSONArray jOutputs = jData.getJSONArray("t_outputs");
			for(int i = 0; i < jOutputs.size(); i++){
				JSONObject jOutput = jOutputs.getJSONObject(i);
				String stateName = jOutput.getString("StateName");
				String event = jOutput.getString("Event");
				String url = jOutput.getString("Url");
				String tag = jOutput.getString("Tag");
				this.outputData.insertData(stateName, event, url, tag);
			}
		}else{
			return -1;
		}
		return 1;
	}
	
	//status: "Inited", "Started", "Finished", "Error"
	public int wait4Status(String status_w, int timeout) {
		long startTime = System.currentTimeMillis();
		this.refresh();
		String status = this.getStatus().getName();
		long endTime = System.currentTimeMillis();
		while(!status.equals(status_w) && (endTime - startTime) < timeout){
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.refresh();
			status = this.getStatus().getName();
			endTime = System.currentTimeMillis();	
		}
		
		if((endTime - startTime) > timeout){
			//TODO update more detail
			return -1;
		}	
		return 1;
	}
	
	//refactor the wait4Status function, wait4Finished(), 1: Started, 2:Finished, -1:Error, 0:Inited
	public int wait4Finished(){
		int status = this.getStatus().getCode();
		while(status == 1 || status == 0){
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.refresh();
			String task_status = this.getStatus().getName();
			status = this.getStatus().getCode();
			System.out.println("Task running status is " + task_status);
		}
		
		if(status == 2){
			return 1; //task finish
		}else{
			return -1; //task error
		}
	}
	
	public int downloadResultByStateEvent(String stateName, String event, String path) throws Exception{
		ExDataConfiguration temp_output = this.getOutputData();
		String url = temp_output.getDataUrl(stateName, event);
		if(url != null){
			File file = HttpHelper.downloadFile(url, path, "GET");
			if(file.exists()){
				 return 1;
			}else{
				 return 0;
			}
		}
		return -1;
	}
	
	
	
	public int bind(String tid, String status) {
		this.tid = tid;
		TaskStatus taskStatus;
		if(status.equals("Inited")){
			taskStatus = TaskStatus.TASK_INITED;
		}else if(status.equals("Started")){
			taskStatus = TaskStatus.TASK_STARTED;
		}else if (status.equals("Finished")) {
			taskStatus = TaskStatus.TASK_FINISHED;
		}else {
			taskStatus = TaskStatus.TASK_ERROR;
		}
		this.status = taskStatus;
		return 1;
	}
	
	
	
	
	
	
	

}
