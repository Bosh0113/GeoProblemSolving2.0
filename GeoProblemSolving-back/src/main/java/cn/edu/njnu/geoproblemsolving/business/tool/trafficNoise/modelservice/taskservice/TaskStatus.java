package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.taskservice;

public enum TaskStatus {
	
	TASK_INITED(0, "Inited"),TASK_STARTED(1, "Started"),TASK_FINISHED(2, "Finished"),TASK_ERROR(-1, "Error");
	
	private int code;
	private String name;
	
	TaskStatus(int code, String name){
		this.code = code;
		this.name= name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getCode(){
		return code;
	}
	
	public void setCode(int code){
		this.code = code;
	}
}
