package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.enumcollection;

public enum ModelServiceStatus {
	STAT_OFFLINE(0,"offline"),STAT_ONLINE(1,"online"),STAT_DELETE(-1,"delete");
	private int code;
	private String name;
	
	ModelServiceStatus(int code, String name){
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
