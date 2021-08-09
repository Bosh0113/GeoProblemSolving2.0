package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice;

import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.taskservice.GeoDataExServer;
import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.taskservice.GeoTaskServer;

public class OGMSService_DEBUG {
	
	public static Server CreateServer(String ip, int port) {
		return new Server(ip, port);
	}
	
	public static GeoDataExServer CreateDataExchangeServer(String ip, int port){
		GeoDataExServer dataExServer = new GeoDataExServer(ip, port);
		if(dataExServer.connect() == 1){
			return dataExServer;
		}else{
			return null;
		}
	}
	
	public static GeoTaskServer CreateTaskServer(String ip, int port){
		GeoTaskServer taskServer = new GeoTaskServer(ip, port);
		if(taskServer.connect() == 1){
			return taskServer;
		}else{
			return null;
		}
	}

}
