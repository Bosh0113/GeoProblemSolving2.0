package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wang ming
 *
 */
public class DataConfiguration {
	
	private List<DataConfigItem> items;
	
	
	
	public DataConfiguration() {
		//具体实现类
		this.items = new ArrayList<DataConfigItem> ();
	}

	public int getCount(){
		return this.items.size();
	} 
	
	public int insertData(String state, String event, String dataid, boolean destoryed){
		for(int i = 0; i < this.items.size(); i++){
			DataConfigItem item = this.items.get(i);
			if(item.state.equals(state) && item.event.equals(event)){
				item.data = dataid;
				item.destoryed = destoryed;
				return 2;
			}
		}
		DataConfigItem item_example = new DataConfigItem(state,event,dataid,destoryed);
		this.items.add(item_example);
		return 1;
	}
	
	public List<DataConfigItem> getItem(){
		return this.items;
	}
	
	public String getDataID(String state, String event){
		for(int i = 0; i < this.items.size(); i++){
			DataConfigItem item = this.items.get(i);
			if(item.state.equals(state) && item.event.equals("event")){
				return this.items.get(i).data;
			}
			
		}
		return null;
	}

}
