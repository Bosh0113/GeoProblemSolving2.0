package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice;

public class Server extends Service {

	public Server(String ip, int port){
		super(ip,port);
	}
	
	public int setIPAndPort(String ip, int port){
		this.setIP(ip);
		
		if(port > 65535 || port < 0){
			return -1;
		}
		
		this.setPort(port);
		return 1;
	}
	
	public ServiceAccess getServiceAccess(){
		return new ServiceAccess(this.getIP(), this.getPort());
	}
	
	
}
