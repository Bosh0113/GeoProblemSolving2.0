package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice;

import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.enumcollection.RecordStatus;
import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.utils.HttpHelper;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class ModelServiceRecord extends Service {
    private String _oid;
    private String _msid;
    private String _datetime;
    private double _span;
    private String _guid;
    private DataConfiguration _input;
    private DataConfiguration _output;
    private RecordStatus _status;
    private String _stdout;
    private String _stderr;
    private String _invkerr;
    private List<RunningLog> _list_log;
	public ModelServiceRecord(String _oid, String _msid, String _datetime, double _span,
			String _guid, DataConfiguration _input, DataConfiguration _output, RecordStatus _status, String _stdout,
			String _stderr, String _invkerr,List<RunningLog> _list_log, String ip, int port) {
		super(ip, port);
		this._oid = _oid;
		this._msid = _msid;
		this._datetime = _datetime;
		this._span = _span;
		this._guid = _guid;
		this._input = _input;
		this._output = _output;
		this._status = _status;
		this._stdout = _stdout;
		this._stderr = _stderr;
		this._invkerr = _invkerr;
		this._list_log = _list_log;
	}
	
	public String getID(){
		return this._oid;
	}
	
	public String getModelServiceID(){
		return this._msid;
	}
	
	public String getStartDatetime(){
		return this._datetime;
	}

	public double getTimeSpan(){
		return this._span;
	}
	
	public DataConfiguration getInputData(){
		return this._input;
	}
	
	public DataConfiguration getOutputData(){
		return this._output;
	}
	
	public RecordStatus getStatus(){
		return this._status;
	}
	
	public String getRunningInfo_Standout(){
		return this._stdout;
	}
	
	public String getRunningInfo_Standerr(){
		return this._stderr;
	}
	
	public String getRunningInfo_Invokeerr(){
		return this._invkerr;
	}
	
	public List<RunningLog> getLogs(){
		return this._list_log;
	}
    
	public String getGUID(){
		return this._guid;
	}
	
	public int refresh(){
		String url = this.getBaseUrl();
		url += "modelserrun/json/" + this._oid;
		String response = HttpHelper.request_get(url, null);
		JSONObject jResponse = JSONObject.parseObject(response);
		if(jResponse.getString("result").equals("suc")){
			JSONObject jMsr = jResponse.getJSONObject("data");
			DataConfiguration pInput = DataFactory.createDataConfigByJSON(jMsr.getJSONArray("msr_input"));
			DataConfiguration pOutput = DataFactory.createDataConfigByJSON(jMsr.getJSONArray("msr_output"));
			
			//update message
			this._input = pInput;
			this._output = pOutput;
			this._span = jMsr.getDouble("msr_span");
			RecordStatus restatus;
			int status = jMsr.getInteger("msr_status");
			if(status == 1){
				restatus = RecordStatus.MSRS_FINISHED;
			}else if(status == 0){
				restatus = RecordStatus.MSRS_UNFINISHED;
			}else{
				restatus = RecordStatus.MSRS_ERROR;
			}
			this._status = restatus;
			this._invkerr = jMsr.getJSONObject("msr_runninginfo").getString("InvokeErr");
			this._stdout = jMsr.getJSONObject("msr_runninginfo").getString("StdOut");
			this._stderr = jMsr.getJSONObject("msr_runninginfo").getString("StdErr");
			
			//update log information
			this._list_log = RunningLogFactory.convertJson2Logs(jMsr.getJSONArray("msr_logs"));
		}else{
			return -1;
		}
		return 1;
	}
	
	public int wait4Finished(){
		int msrstatus = this.getStatus().getCode();
		while(msrstatus == 0){
			try{
				Thread.sleep(2000);
			}catch (InterruptedException e){
				e.printStackTrace();
			}
			this.refresh();
			//打印log信息
			List<RunningLog> list_logs = this.getLogs();
			for(int j = 0; j < list_logs.size(); j++){
				RunningLog log = list_logs.get(j);
				System.out.println(log.getType() + " - " + log.getState() + " - " + log.getEvent() + " - " + log.getMessage());
			}
			msrstatus = this.getStatus().getCode();
			System.out.println("Model Run Status is " + msrstatus);
		}
		if(msrstatus == 1){
			return 1;
		}else{
			return -1;
		}
		
	}
    
}
