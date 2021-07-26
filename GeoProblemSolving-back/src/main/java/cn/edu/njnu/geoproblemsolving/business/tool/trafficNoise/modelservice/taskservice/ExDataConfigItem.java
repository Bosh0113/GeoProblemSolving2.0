package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.taskservice;


/**
 * 数据交换服务器配置Item
 * @author wang ming
 *
 */
public class ExDataConfigItem {

	private String statename;
	private String event;
	private String url;
	private String tag;
	public ExDataConfigItem(String statename, String event, String url, String tag) {
		super();
		this.statename = statename;
		this.event = event;
		this.url = url;
		this.tag = tag;
	}
	public String getStatename() {
		return statename;
	}
	public void setStatename(String statename) {
		this.statename = statename;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	
	
}
