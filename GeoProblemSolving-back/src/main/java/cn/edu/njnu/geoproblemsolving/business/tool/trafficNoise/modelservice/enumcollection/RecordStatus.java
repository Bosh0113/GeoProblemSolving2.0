package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.enumcollection;

public enum RecordStatus {
	MSRS_UNFINISHED(0,"unfinished"),MSRS_FINISHED(1,"finished"),MSRS_ERROR(2,"error");
	private int code;
	private String name;
	
	RecordStatus(int code, String name){
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
