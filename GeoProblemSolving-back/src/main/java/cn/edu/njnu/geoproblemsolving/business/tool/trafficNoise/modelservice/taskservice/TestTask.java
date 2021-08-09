package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.taskservice;

import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice.OGMSService_DEBUG;

public class TestTask {
	
	public static void main(String[] args) {
		GeoTaskServer taskServer = OGMSService_DEBUG.CreateTaskServer("172.21.212.119", 8061);
		GeoDataExServer dataExServer = OGMSService_DEBUG.CreateDataExchangeServer("172.21.212.155", 8062);
		Task task = taskServer.createTask("faa3fa6554e822154862800961a99e51", dataExServer, "wangming");
		
		if(task != null){
			int status = task.configInputData("RUNSTATE", "LOADDATASET", "E:\\DemoData\\FDS\\data11.fds", "");
			if(status == 1){
				System.out.println("config data success");
				//submit task
				int status_task = taskServer.subscribeTask(task);
				if(status_task == 1){
					System.out.println("subscibe task finished");
				}
				status_task = task.wait4Finished();
				
			}else{
				System.out.println("config data error");
			}
			System.out.println("finish");
		}else{
			System.out.println("error");
		}
	}

}
