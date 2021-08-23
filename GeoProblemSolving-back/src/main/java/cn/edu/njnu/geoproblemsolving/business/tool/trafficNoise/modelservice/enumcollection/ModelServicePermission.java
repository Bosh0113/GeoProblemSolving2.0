package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.enumcollection;

public enum ModelServicePermission {
	
	PMS_OPEN(0,"open"),PMS_PERMISSION(1,"permission");
	
	private int code;
	private String name;
	
	ModelServicePermission(int code, String name){
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
