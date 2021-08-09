package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.enumcollection;

public enum DataType {
	DAT_UNKNOWN(0,"unknown"),DAT_RAW(1,"raw"),DAT_XML(2,"xml"),DAT_ZIP(3,"zip");
	
	private int code;
	private String name;
	
	DataType(int code, String name){
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
