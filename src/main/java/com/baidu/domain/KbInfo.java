package com.baidu.domain;

import java.util.List;

public class KbInfo {
	private String id;
	private String opp_id;
	private String cust_id;
	private String unit_pos_id;
	private String product_line_id;
	private String stat;  //商机资源状态
	private String contract_flag;
	private String add_time;
	private String apply_type;
	private String close_time;
	private String upd_time;
	private String follow_cycle;
	private String in_posid;
	private String out_posid;
	private String kb_order_flag;  //单次客保记录是否成单
	private String turn_out_status;
	private List<CallInfo> callInfos;
	
	private List<TurnOutInfo> turn_out_infos;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCust_id() {
		return cust_id;
	}
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	public String getUnit_pos_id() {
		return unit_pos_id;
	}
	public void setUnit_pos_id(String unit_pos_id) {
		this.unit_pos_id = unit_pos_id;
	}
	public String getProduct_line_id() {
		return product_line_id;
	}
	public void setProduct_line_id(String product_line_id) {
		this.product_line_id = product_line_id;
	}
	public String getStat() {
		return stat;
	}
	public void setStat(String stat) {
		this.stat = stat;
	}
	public String getContract_flag() {
		return contract_flag;
	}
	public void setContract_flag(String contract_flag) {
		this.contract_flag = contract_flag;
	}
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
	public String getApply_type() {
		return apply_type;
	}
	public void setApply_type(String apply_type) {
		this.apply_type = apply_type;
	}
	public String getClose_time() {
		return close_time;
	}
	public void setClose_time(String close_time) {
		this.close_time = close_time;
	}
	public String getUpd_time() {
		return upd_time;
	}
	public void setUpd_time(String upd_time) {
		this.upd_time = upd_time;
	}
	public String getFollow_cycle() {
		return follow_cycle;
	}
	public void setFollow_cycle(String follow_cycle) {
		this.follow_cycle = follow_cycle;
	}
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
	public String getTurn_out_status() {
		return turn_out_status;
	}
	public void setTurn_out_status(String turn_out_status) {
		this.turn_out_status = turn_out_status;
	}
	public List<TurnOutInfo> getTurn_out_infos() {
		return turn_out_infos;
	}
	public void setTurn_out_infos(List<TurnOutInfo> turn_out_infos) {
		this.turn_out_infos = turn_out_infos;
	}
	public String getKb_order_flag() {
		return kb_order_flag;
	}
	public void setKb_order_flag(String kb_order_flag) {
		this.kb_order_flag = kb_order_flag;
	}
	public String getOpp_id() {
		return opp_id;
	}
	public void setOpp_id(String opp_id) {
		this.opp_id = opp_id;
	}
	public List<CallInfo> getCallInfos() {
		return callInfos;
	}
	public void setCallInfos(List<CallInfo> callInfos) {
		this.callInfos = callInfos;
	}
	public static void main(String[] args) {
		String ss = "mysql -hdb-dba-dbbk-007.db01 -P5100 -uzhushiliang -pee94ZFEU6A ipangu_callplatform_test -e \"select busi_id2, related_id from busirelation_41351_01 where busi_type2=4\" 1> 41351_01";
		String[] array={"41351","46103","51815","56665"};
		for(String ar:array){
			for(int i=1;i<=12;i++){
				String str = "";
				if(i<10){
					str+="0"+i;
				}else{
					str+=i;
				}
				String finalStr = ar+"_"+str;
				System.out.println(ss.replaceAll("41351_01", finalStr));
			}
		}
		
	}
}
