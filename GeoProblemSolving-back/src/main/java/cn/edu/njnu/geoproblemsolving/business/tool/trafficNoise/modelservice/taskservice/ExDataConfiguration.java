package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.taskservice;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ExDataConfiguration {
	
	private List<ExDataConfigItem> items;
	
	public ExDataConfiguration() {
		this.items = new ArrayList<ExDataConfigItem>();
	}
	
	public int getCount(){
		return this.items.size();
	}
	
	public int insertData(String statename, String event, String url, String tag){
		for(int i = 0; i < this.items.size(); i++){
			ExDataConfigItem item = this.items.get(i);
			if(item.getStatename().equals(statename) && item.getEvent().equals(event)){
				item.setUrl(url);
				item.setTag(tag);
				return 2;
			}
		}
		
		ExDataConfigItem item_example = new ExDataConfigItem(statename, event, url, tag);
		this.items.add(item_example);
		return 1;
	}

	public List<ExDataConfigItem> getItems() {
		return items;
	}

	
	public String getDataUrl(String statename, String event){
		for(int i = 0; i < this.items.size(); i++){
			ExDataConfigItem item = this.items.get(i);
			if(item.getStatename().equals(statename) && item.getEvent().equals(event)){
				return item.getUrl();
			}
		}
		return null;
	}
	
	public JSONArray convertItems2JSON(){
		JSONArray resultJson = new JSONArray();
		List<ExDataConfigItem> tempItems = this.getItems();
		for(int i = 0; i < tempItems.size(); i++){
			JSONObject temp = new JSONObject();
			ExDataConfigItem item = tempItems.get(i);
			temp.put("StateName", item.getStatename());
			temp.put("Event", item.getEvent());
			temp.put("Url", item.getUrl());
			temp.put("Tag", item.getTag());
			resultJson.add(temp);
		}
		return resultJson;
	}
	
	

}
