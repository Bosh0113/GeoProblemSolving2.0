package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice;

public class RunningLog {

	private String _type;
	private String _state;
	private String _event;
	private int _flag;
	private String _message;
	private String _datetime;
	private boolean _mark;
	public RunningLog(String _type, String _state, String _event, int _flag, String _message, String _datetime) {
		this._type = _type;
		this._state = _state;
		this._event = _event;
		this._flag = _flag;
		this._message = _message;
		this._datetime = _datetime;
		this._mark = false;
	}
	
	public String getType(){
		return this._type;
	}
	
	public String getState(){
		return this._state;
	}
	
	public String getEvent(){
		return this._event;
	}
	
	public int getFlag(){
		return this._flag;
	}
	
	public String getMessage(){
		return this._message;
	}
	
	public String getDataTime(){
		return this._datetime;
	}
	
	public boolean getMark(){
		return this._mark;
	}
	
	public void setMark(boolean mark){
		this._mark = mark;
	}
	
	public void print(){
		System.out.println(this._type + " - " + this._state + " - " + this._event + " - " + this._message);
	}
	
}
