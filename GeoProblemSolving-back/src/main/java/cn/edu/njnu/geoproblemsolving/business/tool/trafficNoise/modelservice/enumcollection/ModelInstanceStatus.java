package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.enumcollection;

public enum ModelInstanceStatus {
	INSTA_UNKNOWN("UNKNOWN","unknown"),INSTA_RUNNING("RUNNING","running"),INSTA_REQUESTING("REQUESTING","requesting"),INSTA_HANGING("HANGING","hanging"),
	INSTA_FINISHED("FINISHED","finished");
	
	private String code;
	private String name;
	
	ModelInstanceStatus(String code, String name){
		this.code = code;
		this.name= name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getCode(){
		return code;
	}
	
	public void setCode(String code){
		this.code = code;
	}

}
