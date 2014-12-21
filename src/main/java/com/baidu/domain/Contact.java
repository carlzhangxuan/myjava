package com.baidu.domain;

import java.io.Serializable;

public class Contact implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9027335367110063876L;
	private String phone_num;
	private String contact_name;
	private String policy_type;  //DECISION_MAKER(1L, "决策人"), OPERATOR(2L, "操作人"), DECISION_MAKER_INFLUENCER(3L, "影响决策人"), UNKNOWN(-1L, "未知");
	public String getPhone_num() {
		return phone_num;
	}
	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}
	public String getContact_name() {
		return contact_name;
	}
	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}
	public String getPolicy_type() {
		return policy_type;
	}
	public void setPolicy_type(String policy_type) {
		this.policy_type = policy_type;
	}
	
}
