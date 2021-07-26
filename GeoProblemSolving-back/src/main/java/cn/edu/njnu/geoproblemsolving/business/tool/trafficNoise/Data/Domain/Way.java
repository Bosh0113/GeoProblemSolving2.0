package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Domain;

import java.util.ArrayList;
import java.util.List;

public class Way {
	private String wayID;
	private List<String> NodeID;
	
	public Way() {
		this.NodeID = new ArrayList<String>();
	};
	
	public void addNode(String nodeID) {
		this.NodeID.add(nodeID);
	}
	
	public List<String> getNodeList() {
		return this.NodeID;
	}
}
