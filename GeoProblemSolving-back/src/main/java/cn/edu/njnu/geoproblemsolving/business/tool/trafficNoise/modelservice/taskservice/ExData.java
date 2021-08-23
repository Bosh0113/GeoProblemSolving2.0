package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.taskservice;

import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice.Service;
import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.utils.CommonUtils;
import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.utils.HttpHelper;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author wang ming
 *
 */
public class ExData extends Service {

	private String id;
	private String pwd;
	
	public ExData(String id, String pwd, String ip, int port) {
		super(ip, port);
		this.id = id;
		this.pwd = pwd;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	
	
	/**
	 * 返回数据URL
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getURL() throws UnsupportedEncodingException{
		String dataId = this.getId();
		String pwd_c = "";
		if(!this.getPwd().equals("")){
			//encryption
			pwd_c = CommonUtils.encryption(this.getPwd());
		}
		String url = this.getBaseUrl() + "data/" + dataId + "?pwd=" + pwd_c;
		return url;
	}
	
	public int download(String filePath) throws IOException {
		String url = this.getURL();
		File file = HttpHelper.downloadFile(url, filePath, "GET");
		if(file.exists()){
			 return 1;
		}else{
			 return 0;
		}
	}
	
}
