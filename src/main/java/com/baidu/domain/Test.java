package com.baidu.domain;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8870298576933665132L;

	private static final Logger LOG = LoggerFactory.getLogger(Test.class);
	
	private String id;  //pg_cust|tb_cust|cust_id
	private String hint_id; //  pg_cust|tb_cust|hint_id
	private String hint_source_1;  //  pg_cust|tb_cust|hint_source_1
	private String hint_source_2;  //  pg_cust|tb_cust|hint_source_2
	private String type;           //  pg_cust|tb_cust|type（客户类型）
	private String department;     //  pg_cust|tb_cust|department（部门）
	private String full_name;	   //  pg_cust|tb_cust|full_name（公司全名）
	private String enterprise_scale;  //   pg_cust|tb_cust|enterprise_scale（公司规模）
	private String major_product;     //   pg_cust|tb_cust|major_product（主营产品）
	private String trade_1;           //   pg_cust|tb_cust|trade_1
	private String trade_2;           //   pg_cust|tb_cust|trade_2
	private String belong_province_id; // 客户所在省id
	private String belong_city_id;// 客户所在市id
	private String belong_county_id;// 客户所在区id
	private String company_address; //pg_cust|tb_cust|company_address 客户公司地址
	private String zipcode;     //pg_cust|tb_cust|zipcode公司所在地邮编
	private String site_type;   //pg_cust|tb_cust|site_type 建站情况
	private String no_site_desc;    //pg_cust|tb_cust|no_site_desc暂无网站说明
	private String status;        //pg_cust|tb_cust|status 客户端状态
	private String auto_audit_type;  //pg_cust|tb_cust|auto_audit_type 自动审核结果
	private String priority;    //pg_cust|tb_cust|priority优先级别
	private String add_time;  //pg_cust|tb_cust|add_time添加时间
	private String add_ucid;  //pg_cust|tb_cust|add_ucid添加人uid
	private String upd_time;  //pg_cust|tb_cust|upd_time更新时间
	private String upd_ucid;   //pg_cust|tb_cust|upd_ucid最近一次更改人ucid
	private String no_site_type;  //pg_cust|tb_cust|no_site_type暂无网站类型
	private String remark;   //pg_cust|tb_cust|remark 客户备注
	private String add_posid;  //pg_cust|tb_cust|add_posid 添加人posid
	private String upd_posid;  //pg_cust|tb_cust|upd_posid 更新人posid
	private String[] trade_3;  //pg_cust|tb_cust_trade3|trade_3   3级行业id
	private String[] site_url; //  pg_cust|tb_cust_site|site_url  网站url
	private String[] contact_detail;  //pg_cust|tb_contact_detail|full_info，联系方式
	private String registered_address;  //pg_cust|tb_cust_optional|registered_address 注册地址
	private String registered_fund;   //pg_cust|tb_cust_optional|registered_fund  注册资金
	private String factory_area;      //pg_cust|tb_cust_optional|factory_area  厂房面积
	private String operate_model;     //pg_cust|tb_cust_optional|operate_model 经营模式
	private String operate_scope;     //pg_cust|tb_cust_optional|operate_scope 经营范围
	private String reseach_scope;    //pg_cust|tb_cust_optional|reseach_scope  研发人员人数
	private String annual_turnover;  //pg_cust|tb_cust_optional|annual_turnover 年营业额
	private String main_customer;   //pg_cust|tb_cust_optional|main_customer 主要客户
	private String main_market;     //pg_cust|tb_cust_optional|main_market  主要市场
	private String company_brief;  //pg_cust|tb_cust_optional|company_brief 公司介绍
	private String found_time;   //pg_cust|tb_cust_optional|found_time 成立时间
	
	private List<CustCscInfo> csc_info; //csc信息
	
	private List<KbInfo> kb_info; //客保信息
	
	private String enterprise_type;//企业类型
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHint_id() {
		return hint_id;
	}
	public void setHint_id(String hint_id) {
		this.hint_id = hint_id;
	}
	public String getHint_source_1() {
		return hint_source_1;
	}
	public void setHint_source_1(String hint_source_1) {
		this.hint_source_1 = hint_source_1;
	}
	public String getHint_source_2() {
		return hint_source_2;
	}
	public void setHint_source_2(String hint_source_2) {
		this.hint_source_2 = hint_source_2;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getFull_name() {
		return full_name;
	}
	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}
	public String getEnterprise_scale() {
		return enterprise_scale;
	}
	public void setEnterprise_scale(String enterprise_scale) {
		this.enterprise_scale = enterprise_scale;
	}
	public String getMajor_product() {
		return major_product;
	}
	public void setMajor_product(String major_product) {
		this.major_product = major_product;
	}
	public String getTrade_1() {
		return trade_1;
	}
	public void setTrade_1(String trade_1) {
		this.trade_1 = trade_1;
	}
	public String getTrade_2() {
		return trade_2;
	}
	public void setTrade_2(String trade_2) {
		this.trade_2 = trade_2;
	}
	public String getBelong_province_id() {
		return belong_province_id;
	}
	public void setBelong_province_id(String belong_province_id) {
		this.belong_province_id = belong_province_id;
	}
	public String getBelong_city_id() {
		return belong_city_id;
	}
	public void setBelong_city_id(String belong_city_id) {
		this.belong_city_id = belong_city_id;
	}
	public String getBelong_county_id() {
		return belong_county_id;
	}
	public void setBelong_county_id(String belong_county_id) {
		this.belong_county_id = belong_county_id;
	}
	public String getCompany_address() {
		return company_address;
	}
	public void setCompany_address(String company_address) {
		this.company_address = company_address;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getSite_type() {
		return site_type;
	}
	public void setSite_type(String site_type) {
		this.site_type = site_type;
	}
	public String getNo_site_desc() {
		return no_site_desc;
	}
	public void setNo_site_desc(String no_site_desc) {
		this.no_site_desc = no_site_desc;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAuto_audit_type() {
		return auto_audit_type;
	}
	public void setAuto_audit_type(String auto_audit_type) {
		this.auto_audit_type = auto_audit_type;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
	public String getAdd_ucid() {
		return add_ucid;
	}
	public void setAdd_ucid(String add_ucid) {
		this.add_ucid = add_ucid;
	}
	public String getUpd_time() {
		return upd_time;
	}
	public void setUpd_time(String upd_time) {
		this.upd_time = upd_time;
	}
	public String getUpd_ucid() {
		return upd_ucid;
	}
	public void setUpd_ucid(String upd_ucid) {
		this.upd_ucid = upd_ucid;
	}
	public String getNo_site_type() {
		return no_site_type;
	}
	public void setNo_site_type(String no_site_type) {
		this.no_site_type = no_site_type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAdd_posid() {
		return add_posid;
	}
	public void setAdd_posid(String add_posid) {
		this.add_posid = add_posid;
	}
	public String getUpd_posid() {
		return upd_posid;
	}
	public void setUpd_posid(String upd_posid) {
		this.upd_posid = upd_posid;
	}
	public String[] getTrade_3() {
		return trade_3;
	}
	public void setTrade_3(String trade_3) {
		this.trade_3 = trade_3.split(";;");
	}
	public void setTrade_3(String[] trade_3) {
		this.trade_3 = trade_3;
	}
	public String[] getSite_url() {
		return site_url;
	}
	public void setSite_url(String site_url) {
		this.site_url = site_url.split(";;");
	}
	public void setSite_url(String[] urls) {
		this.site_url = urls;
	}
	public String[] getContact_detail() {
		return contact_detail;
	}
	public void setContact_detail(String contact_detail) {
		this.contact_detail = contact_detail.split(";;");
	}
	
	public void setContact_detail(String[] details) {
		this.contact_detail = details;
	}
	
	public String getRegistered_address() {
		return registered_address;
	}
	public void setRegistered_address(String registered_address) {
		this.registered_address = registered_address;
	}
	public String getRegistered_fund() {
		return registered_fund;
	}
	public void setRegistered_fund(String registered_fund) {
		this.registered_fund = registered_fund;
	}
	public String getFactory_area() {
		return factory_area;
	}
	public void setFactory_area(String factory_area) {
		this.factory_area = factory_area;
	}
	public String getOperate_model() {
		return operate_model;
	}
	public void setOperate_model(String operate_model) {
		this.operate_model = operate_model;
	}
	public String getOperate_scope() {
		return operate_scope;
	}
	public void setOperate_scope(String operate_scope) {
		this.operate_scope = operate_scope;
	}
	public String getReseach_scope() {
		return reseach_scope;
	}
	public void setReseach_scope(String reseach_scope) {
		this.reseach_scope = reseach_scope;
	}
	public String getAnnual_turnover() {
		return annual_turnover;
	}
	public void setAnnual_turnover(String annual_turnover) {
		this.annual_turnover = annual_turnover;
	}
	public String getMain_customer() {
		return main_customer;
	}
	public void setMain_customer(String main_customer) {
		this.main_customer = main_customer;
	}
	public String getMain_market() {
		return main_market;
	}
	public void setMain_market(String main_market) {
		this.main_market = main_market;
	}
	public String getCompany_brief() {
		return company_brief;
	}
	public void setCompany_brief(String company_brief) {
		this.company_brief = company_brief;
	}
	public String getFound_time() {
		return found_time;
	}
	public void setFound_time(String found_time) {
		this.found_time = found_time;
	}
	public List<CustCscInfo> getCsc_info() {
		return csc_info;
	}
	public void setCsc_info(List<CustCscInfo> csc_info) {
		this.csc_info = csc_info;
	}
	public String getEnterprise_type() {
		return enterprise_type;
	}
	public void setEnterprise_type(String enterprise_type) {
		this.enterprise_type = enterprise_type;
	}
	public List<KbInfo> getKb_info() {
		return kb_info;
	}
	public void setKb_info(List<KbInfo> kb_info) {
		this.kb_info = kb_info;
	}
	
	@Override
	public String toString() {
		try {
			StringBuilder sb = new StringBuilder();
			Field[] fields = this.getClass().getDeclaredFields();
			for (Field f : fields) {
				f.setAccessible(true);
				if (f.getType().getName().equals(String.class.getName())) {
					sb.append(f.getName()).append("-").append(f.get(this)).append("\n");
				} else if (f.getType().getName().equals(this.getClass().getName())) {
					sb.append(f.getName()).append("-").append(f.get(this)).append("\n");
				}
			}
			return sb.toString();
		} catch (IllegalAccessException e) {
			LOG.error("toString failed, ", e);
			return "";
		}
	}
	
}
