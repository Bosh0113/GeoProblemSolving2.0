package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice;

import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.utils.HttpHelper;
import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.utils.StringUtils;

public class Service {
	
	private String ip;
	private int port;
	
	public Service(String ip, int port){
		this.ip = ip;
		this.port = port;
	}
	
	public Service(){
		this.ip = "127.0.0.1";
		this.port = 8060;
	}
	
	public String getIP(){
		return this.ip;
	} 
	
	public int getPort(){
		return this.port;
	}
	
	protected int setIP(String ip){
		this.ip = ip;
		return 1;
	}
	
	protected int setPort(int port){
		this.port = port;
		return 1;
	}
	
	protected String getBaseUrl(){
		StringBuilder stringbuilder = new StringBuilder("");
		stringbuilder.append("http://").append(this.ip).append(":").append(this.port).append("/");
		return stringbuilder.toString();
	}
	
	public int connect(){
		String url = this.getBaseUrl();
		url = url + "ping";
		String body = HttpHelper.request_get(url, null);
		String content = StringUtils.replaceBlank(body);
		// JSON Object
		if(content.equals("OK")){
			return 1;
		}
		return 0;
	}
	

}
