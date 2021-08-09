package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.enumcollection;

public enum Platform {

	PLF_UNKNOWN(0,"unknown"),PLF_WINDOWS(1,"windows"),PLF_LINUX(2,"linux"),PLF_MACOS(3,"macos");
	
	private int code;
	private String name;
	
	Platform(int code, String name){
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
