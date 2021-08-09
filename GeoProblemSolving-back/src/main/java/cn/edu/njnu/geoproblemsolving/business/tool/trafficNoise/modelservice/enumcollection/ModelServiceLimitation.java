package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.enumcollection;

public enum ModelServiceLimitation {

	LMT_PRIVATE(0,"private"),LMT_PUBLIC(1,"public");
	private int code;
	private String name;
	
	ModelServiceLimitation(int code, String name){
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
