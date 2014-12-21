package com.baidu.domain;

import java.io.Serializable;

public class TurnOutInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7481159586187135550L;
	private String in_posid;
	private String out_posid;
	private String add_time;
	private String upd_time;
	private String assign_time;
	private String turn_out_time;
	private String close_time;
	private String renewal_times;
	public String getIn_posid() {
		return in_posid;
	}
	public void setIn_posid(String in_posid) {
		this.in_posid = in_posid;
	}
	public String getOut_posid() {
		return out_posid;
	}
	public void setOut_posid(String out_posid) {
		this.out_posid = out_posid;
	}
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
	public String getUpd_time() {
		return upd_time;
	}
	public void setUpd_time(String upd_time) {
		this.upd_time = upd_time;
	}
	public String getAssign_time() {
		return assign_time;
	}
	public void setAssign_time(String assign_time) {
		this.assign_time = assign_time;
	}
	public String getTurn_out_time() {
		return turn_out_time;
	}
	public void setTurn_out_time(String turn_out_time) {
		this.turn_out_time = turn_out_time;
	}
	public String getClose_time() {
		return close_time;
	}
	public void setClose_time(String close_time) {
		this.close_time = close_time;
	}
	public String getRenewal_times() {
		return renewal_times;
	}
	public void setRenewal_times(String renewal_times) {
		this.renewal_times = renewal_times;
	}
	
	
}
