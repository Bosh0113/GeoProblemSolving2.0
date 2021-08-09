package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;

public class Test {

	public static void main(String[] args) throws IOException {
		
		System.out.println("test success");
		JSONObject my_json = new JSONObject();
		my_json.put("class", "two");
		my_json.put("total", 2);
		
		System.out.println(my_json.toString());
//		Server server = ServerFactory.createServer("127.0.0.1", 8060);
//		if(server.connect() == 1){
//			System.out.println("Hello my sdk!");
//			ServiceAccess pServiceAccess = server.getServiceAccess();
//			List<ModelService> list_ms = pServiceAccess.getModelServicesList();
//			
//			for(int i = 0; i < list_ms.size(); i++){
//				ModelService ms = list_ms.get(i);
//				if(ms == null) continue;
//				System.out.println(ms.getServiceOID() + " - " + ms.getServiceName() + " - " + ms.getServiceType());
//			}
//			System.out.println("test success!");
//			
//			//上传数据
//			Data pData = pServiceAccess.uploadDataByFile("E:\\NativeTest\\TestData\\TouchAir\\input.xml", "input.xml");
//			if(pData != null){
//				System.out.println(pData.getID() + " - " + pData.getSize() + " - " + pData.getGenerationDateTime() + " - " + pData.getTag());
//				DataConfiguration pDataconfig = pServiceAccess.createDataConfig();
//				pDataconfig.insertData("aa00cced-60e7-48a5-90d2-f91ac08b624d", "InputData", pData.getID(), false);
//				ModelService pMservice = pServiceAccess.getModelServiceByOID("5b19534b7fe81768bcc191b2");
//				String recordid = pMservice.invoke(pDataconfig);
//				System.out.println(recordid);
//				ModelServiceRecord pRecord = pServiceAccess.getModelServiceRecordByID(recordid);
////				ModelServiceInstance pinstance = pServiceAccess.getModelServiceInstanceByGUID(pRecord.getGUID());
////				pinstance.wait4Status(ModelInstanceStatus.INSTA_FINISHED, 1000, true);
//				//System.out.println("TouchAir model has been finished!");
//				//pRecord.refresh();
////				for(int j = 0; j < pRecord.getOutputData().getCount(); j++){
////					String dataid = pRecord.getOutputData().getItem().get(j).data;
////					String eventname = pRecord.getOutputData().getItem().get(j).event;
////					Data outputdata = pServiceAccess.getDataServiceByID(dataid);
////					outputdata.saveAs("E:\\NativeTest\\TestData\\TouchAir\\" + )
////				}
//				//System.out.println("fuck no bug!!!");
//				int msrstatus = pRecord.getStatus().getCode();
//				System.out.println("model status is " + msrstatus);
//				while(msrstatus == 0){
//					try{
//						Thread.sleep(2000);
//					}catch (InterruptedException e){
//						e.printStackTrace();
//					}
//					pRecord.refresh();
//					//打印log信息
//					List<RunningLog> list_logs = pRecord.getLogs();
//					for(int j = 0; j < list_logs.size(); j++){
//						RunningLog log = list_logs.get(j);
//						System.out.println(log.getType() + " - " + log.getState() + " - " + log.getEvent() + " - " + log.getMessage());
//					}
//					msrstatus = pRecord.getStatus().getCode();
//					System.out.println("Model Run Status is " + msrstatus);
//					
//				}
//				System.out.println("TouchAir model has been finished!");
//				
//				//测试保存文件
//				for(int t = 0; t < pRecord.getOutputData().getCount();t++){
//					DataConfigItem item = pRecord.getOutputData().getItem().get(t);
//					String dataid = item.data;
//					Data outputdata = pServiceAccess.getDataServiceByID(dataid);
//					String datavalue = outputdata.getValue();
//					//根据value值获取文件后缀
//					String ext = datavalue.substring(datavalue.lastIndexOf(".") + 1);
//					System.out.println(ext);
//					outputdata.saveAs("E:\\NativeTest\\TestData\\TouchAir\\" + item.event + "." + ext);
//				}
//			}
//		}
	}
}
