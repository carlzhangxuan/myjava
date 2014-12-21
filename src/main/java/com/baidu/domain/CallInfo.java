package com.baidu.domain;

public class CallInfo {
	private String id;
	private String create_time;
	private String update_time;
	private String add_ucid;
	private String update_ucid;
	private String related_id;
	private String unit_pos_id;
	private String agent_type;
	private String call_type;
	private String start_Time;
	private String connection_time;
	private String answer_time;
	private String stop_time;
	private String call_duration;
	private String call_interval;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getAdd_ucid() {
		return add_ucid;
	}
	public void setAdd_ucid(String add_ucid) {
		this.add_ucid = add_ucid;
	}
	public String getUpdate_ucid() {
		return update_ucid;
	}
	public void setUpdate_ucid(String update_ucid) {
		this.update_ucid = update_ucid;
	}
	public String getRelated_id() {
		return related_id;
	}
	public void setRelated_id(String related_id) {
		this.related_id = related_id;
	}
	public String getUnit_pos_id() {
		return unit_pos_id;
	}
	public void setUnit_pos_id(String unit_pos_id) {
		this.unit_pos_id = unit_pos_id;
	}
	public String getAgent_type() {
		return agent_type;
	}
	public void setAgent_type(String agent_type) {
		this.agent_type = agent_type;
	}
	public String getCall_type() {
		return call_type;
	}
	public void setCall_type(String call_type) {
		this.call_type = call_type;
	}
	public String getStart_Time() {
		return start_Time;
	}
	public void setStart_Time(String start_Time) {
		this.start_Time = start_Time;
	}
	public String getConnection_time() {
		return connection_time;
	}
	public void setConnection_time(String connection_time) {
		this.connection_time = connection_time;
	}
	public String getAnswer_time() {
		return answer_time;
	}
	public void setAnswer_time(String answer_time) {
		this.answer_time = answer_time;
	}
	public String getStop_time() {
		return stop_time;
	}
	public void setStop_time(String stop_time) {
		this.stop_time = stop_time;
	}
	public String getCall_duration() {
		return call_duration;
	}
	public void setCall_duration(String call_duration) {
		this.call_duration = call_duration;
	}
	public String getCall_interval() {
		return call_interval;
	}
	public void setCall_interval(String call_interval) {
		this.call_interval = call_interval;
	}
	@Override
	public String toString() {
		return "CallInfo [id=" + id + ", related_id=" + related_id
				+ ", start_Time=" + start_Time + ", call_duration="
				+ call_duration + "]";
	}
	
	
}
