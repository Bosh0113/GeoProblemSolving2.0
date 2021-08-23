package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice;

/**
 * @author wang ming
 * 仿c++结构体
 *
 */
public class DataConfigItem {
	
	public String state;
	public String event;
	public String data;
	public boolean destoryed = false;
	public boolean requested = false;
	public boolean optional = false;
	
	public DataConfigItem(String state, String event, String data){
		this.state = state;
		this.event = event;
		this.data = data;			
	}

	public DataConfigItem(String state, String event, String data, boolean destoryed, boolean requested,
			boolean optional) {
		this.state = state;
		this.event = event;
		this.data = data;
		this.destoryed = destoryed;
		this.requested = requested;
		this.optional = optional;
	}

	public DataConfigItem(String state, String event, String data, boolean destoryed) {
		this.state = state;
		this.event = event;
		this.data = data;
		this.destoryed = destoryed;
	}
	
	
	

}
