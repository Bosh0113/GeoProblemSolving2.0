package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.taskservice;

import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice.Service;
import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.utils.CommonUtils;
import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.utils.HttpHelper;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.DecoderException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class GeoDataExServer extends Service {
	
	public GeoDataExServer(String ip, int port) {
		super(ip,port);
	}
	
	
	public ExData upload(String dataPath, String tag) throws UnsupportedEncodingException, DecoderException{
		String md5 = CommonUtils.getFileMd5(dataPath);
		String path = "data?md5=" + md5;
		String url = this.getBaseUrl() + path;
		String response = HttpHelper.request_get(url, null);
		JSONObject jResponse = JSONObject.parseObject(response);
		if(jResponse.getString("result").equals("suc")){
			int code = jResponse.getInteger("code");
			if(code == 1){
				//说明数据是一个JsonObject
				JSONObject jData = jResponse.getJSONObject("data");
				String pwd = jData.getString("d_pwd");
				pwd = CommonUtils.decryption(CommonUtils.decryption(pwd));
				String id = jData.getString("id");
				return new ExData(id, pwd, this.getIP(), this.getPort());
			}else{
				//说明还未存在该数据，需要进行post请求
				Map<String, String> textMap = new HashMap<String,String>();
				Map<String, String> fileMap = new HashMap<String,String>();
				
				textMap.put("datatag", tag);
				textMap.put("pwd", "true");
				fileMap.put("datafile", dataPath);
				
				String actionURL = this.getBaseUrl() + "data";
				String result = HttpHelper.request_post_file(actionURL, fileMap, textMap);
				JSONObject jsResult= JSONObject.parseObject(result);
				if(jsResult.getString("result").equals("suc")){
					JSONObject jData = jsResult.getJSONObject("data");
					String pwd = jData.getString("d_pwd");
					pwd = CommonUtils.decryption(CommonUtils.decryption(pwd));
					String id = jData.getString("id");
					return new ExData(id, pwd, this.getIP(), this.getPort());
				}
			}
		}
		return null;
	}
	
	
}
