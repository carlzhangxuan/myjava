package com.baidu.models;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 原始线索类
 * 
 * @author tangchunsong
 * 
 */
public class BaseClue  implements Cloneable {

	private static final Logger LOG = LoggerFactory.getLogger(BaseClue.class);

	private String id = "";// 线索id
	private String source = "";// 线索来源
	private String custName = "";// 公司名

	private List<Contact> contacts = new ArrayList<Contact>(); // 联系人信息列表，包含电话
	private String fax = ""; // 传真
	private String email = ""; // 邮箱
	private String zipCode = ""; // 邮编 add
	private String sourceUrl = ""; // 来源url 从什么url抓取

	private String custAddr = "";// 地址
	private String url = "";// 网址
	private String legal = "";// 法人
	private String regAddr = "";// 注册地址
	private String regCap = "";// 注册资本
	private String regOffice = "";// 注册机构
	private String regNo = "";// 注册号
	private String annualCert = "";// 年检年份
	private String certResult = "";// 年检结果
	private String effTime = "";// 工商有效期，营业期限
	private String regStatus = ""; // 企业状态，工商状态
	private String busScope = "";// 经营范围
	private String estTime = ""; // 成立时间
	private String brandName = ""; // 商标
	private String products = "";// 主营产品
	private String mainCusts = ""; // 主要客户
	private String mainMarkets = "";// 主要市场
	private String profile = ""; // 公司简介

	// 下面联系字段为规模信息
	private String empNum = "";// 员工人数
	private String resNum = "";// 研发人员数量
	private String annualTurnover = "";// 年营业额
	private String imports = "";// 年进口额
	private String exports = "";// 年出口额
	private String monYield = "";// 月盈利额
	private String roomArea = "";// 占地面积

	private String trade = "";// 行业
	private String custType = "";// 企业类型，如私营合伙企业，有限责任公司等
	private String busMode = "";// 经营模式，如生产型，服务型
	private String accountBank = "";// 开户银行
	private String accountNo = "";// 银行账号
	private String icpStatus = ""; // icp备案状态,有效和无效,每天抓取都会发送同一批数据的icp状态数据，流式进行更新

	private String source1 = ""; // 一级来源
	private String source2 = ""; // 二级来源
	private Set<String> source3List = new HashSet<String>(); // 多个三级来源标签
	private int importTaskId; // lc导入数据任务id
	private long importTime; // lc导入数据任务时间 秒long
	private long addTime; // 数据入系统时间
	private OnlineStatus onlineStatus = OnlineStatus.NULL;// 线上状态
	private String onlineSource = ""; // 线上库
	private String onlineUpdTime = ""; // 资料更新时间 秒long
	private String busType = ""; // 企业类型，希望统一：类型 0:普通企业 1:特殊企业 2:个人客户 3:广告代理',
	private String inferTrade1 = ""; // 线上导入线索的一级行业
	private String inferTrade2 = ""; // 线上导入线索的二级行业
	private Set<String> inferTrade3List = new HashSet<String>(); // 线上导入线索的三级行业列表
	private String inferProv = ""; // 线上导入线索的省级地域
	private String inferCity = ""; // 线上导入线索的市级地域
	private String inferCounty = ""; // 线上导入线索的区县级地域
	private String pos = ""; // 运营单位

	private String source1Id = "";// 线上导入线索的一级行业id
	private String source2Id = "";// 线上导入线索的二级行业id
	private Set<String> source3IdList = new HashSet<String>();// 多个三级来源标签id
	private String inferTrade1Id = ""; // 线上导入线索的一级行业id
	private String inferTrade2Id = ""; // 线上导入线索的二级行业id
	private Set<String> inferTrade3IdList = new HashSet<String>(); // 线上导入线索的三级行业id列表
	private String inferProvId = ""; // 线上导入线索的省级地域id
	private String inferCityId = ""; // 线上导入线索的市级地域id
	private String inferCountyId = ""; // 线上导入线索的区县级地域id
	private String posId = ""; // 运营单位id

	@Override
	public String toString() {
		try {
			String split = "---base---";
			StringBuilder sb = new StringBuilder();
			Field[] fields = this.getClass().getDeclaredFields();
			for (Field f : fields) {
				f.setAccessible(true);
				if (f.getType().getName().equals(String.class.getName())) {
					sb.append(f.getName()).append(":").append(f.get(this))
							.append(split);
				}
				if (f.getType().isPrimitive()) {
					sb.append(f.getName()).append(":").append(f.get(this))
							.append(split);
				}
			}
			for (Contact c : contacts) {
				sb.append("contact:").append(c).append(split);
			}
			for (String s : source3List) {
				sb.append("source3:").append(s).append(split);
			}
			for (String s : source3IdList) {
				sb.append("source3Id:").append(s).append(split);
			}
			for (String s : inferTrade3List) {
				sb.append("inferTrade3:").append(s).append(split);
			}
			for (String s : inferTrade3IdList) {
				sb.append("inferTrade3Id:").append(s).append(split);
			}
			sb.append("onlineStatus:").append(onlineStatus);
			return sb.toString();
		} catch (IllegalAccessException e) {
			LOG.error("toString failed, ", e);
			return "";
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		BaseClue bc = (BaseClue) super.clone();
		List<Contact> tmpContacts = new ArrayList<Contact>();
		if (contacts != null) {
			for (Contact c : contacts) {
				tmpContacts.add((Contact) c.clone());
			}
		}
		bc.setContacts(tmpContacts);

		// 枚举对象不要克隆

		Set<String> tmpSource3List = new HashSet<String>();
		if (source3List != null) {
			tmpSource3List.addAll(source3List);
		}
		bc.setSource3List(tmpSource3List);

		Set<String> tmpSource3IdList = new HashSet<String>();
		if (source3IdList != null) {
			tmpSource3IdList.addAll(source3IdList);
		}
		bc.setSource3IdList(tmpSource3IdList);

		Set<String> tmpInferTrade3List = new HashSet<String>();
		if (inferTrade3List != null) {
			tmpInferTrade3List.addAll(inferTrade3List);
		}
		bc.setInferTrade3List(tmpInferTrade3List);

		Set<String> tmpInferTrade3IdList = new HashSet<String>();
		if (inferTrade3IdList != null) {
			tmpInferTrade3IdList.addAll(inferTrade3IdList);
		}
		bc.setInferTrade3IdList(tmpInferTrade3IdList);

		return bc;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustAddr() {
		return custAddr;
	}

	public void setCustAddr(String custAddr) {
		this.custAddr = custAddr;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLegal() {
		return legal;
	}

	public void setLegal(String legal) {
		this.legal = legal;
	}

	public String getRegAddr() {
		return regAddr;
	}

	public void setRegAddr(String regAddr) {
		this.regAddr = regAddr;
	}

	public String getRegCap() {
		return regCap;
	}

	public void setRegCap(String regCap) {
		this.regCap = regCap;
	}

	public String getRegStatus() {
		return regStatus;
	}

	public void setRegStatus(String regStatus) {
		this.regStatus = regStatus;
	}

	public String getRegOffice() {
		return regOffice;
	}

	public void setRegOffice(String regOffice) {
		this.regOffice = regOffice;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public String getAnnualCert() {
		return annualCert;
	}

	public void setAnnualCert(String annualCert) {
		this.annualCert = annualCert;
	}

	public String getCertResult() {
		return certResult;
	}

	public void setCertResult(String certResult) {
		this.certResult = certResult;
	}

	public String getEffTime() {
		return effTime;
	}

	public void setEffTime(String effTime) {
		this.effTime = effTime;
	}

	public String getBusScope() {
		return busScope;
	}

	public void setBusScope(String busScope) {
		this.busScope = busScope;
	}

	public String getEstTime() {
		return estTime;
	}

	public void setEstTime(String estTime) {
		this.estTime = estTime;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}

	public String getMainCusts() {
		return mainCusts;
	}

	public void setMainCusts(String mainCusts) {
		this.mainCusts = mainCusts;
	}

	public String getMainMarkets() {
		return mainMarkets;
	}

	public void setMainMarkets(String mainMarkets) {
		this.mainMarkets = mainMarkets;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTrade() {
		return trade;
	}

	public void setTrade(String trade) {
		this.trade = trade;
	}

	public String getCustType() {
		return custType;
	}

	public void setCustType(String custType) {
		this.custType = custType;
	}

	public String getBusMode() {
		return busMode;
	}

	public void setBusMode(String busMode) {
		this.busMode = busMode;
	}

	public String getIcpStatus() {
		return icpStatus;
	}

	public void setIcpStatus(String icpStatus) {
		this.icpStatus = icpStatus;
	}

	public String getAccountBank() {
		return accountBank;
	}

	public void setAccountBank(String accountBank) {
		this.accountBank = accountBank;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getFax() {
		return fax;
	}

	public String getOnlineUpdTime() {
		return onlineUpdTime;
	}

	public void setOnlineUpdTime(String onlineUpdTime) {
		this.onlineUpdTime = onlineUpdTime;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmpNum() {
		return empNum;
	}

	public void setEmpNum(String empNum) {
		this.empNum = empNum;
	}

	public String getResNum() {
		return resNum;
	}

	public void setResNum(String resNum) {
		this.resNum = resNum;
	}

	public String getAnnualTurnover() {
		return annualTurnover;
	}

	public void setAnnualTurnover(String annualTurnover) {
		this.annualTurnover = annualTurnover;
	}

	public String getImports() {
		return imports;
	}

	public void setImports(String imports) {
		this.imports = imports;
	}

	public String getExports() {
		return exports;
	}

	public void setExports(String exports) {
		this.exports = exports;
	}

	public String getMonYield() {
		return monYield;
	}

	public void setMonYield(String monYield) {
		this.monYield = monYield;
	}

	public String getRoomArea() {
		return roomArea;
	}

	public void setRoomArea(String roomArea) {
		this.roomArea = roomArea;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public String getSource1() {
		return source1;
	}

	public void setSource1(String source1) {
		this.source1 = source1;
	}

	public String getSource2() {
		return source2;
	}

	public void setSource2(String source2) {
		this.source2 = source2;
	}

	public int getImportTaskId() {
		return importTaskId;
	}

	public void setImportTaskId(int importTaskId) {
		this.importTaskId = importTaskId;
	}

	public long getImportTime() {
		return importTime;
	}

	public void setImportTime(long importTime) {
		this.importTime = importTime;
	}

	public OnlineStatus getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(OnlineStatus onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public String getOnlineSource() {
		return onlineSource;
	}

	public void setOnlineSource(String onlineSource) {
		this.onlineSource = onlineSource;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getBusType() {
		return busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}

	public String getInferTrade1() {
		return inferTrade1;
	}

	public void setInferTrade1(String inferTrade1) {
		this.inferTrade1 = inferTrade1;
	}

	public String getInferTrade2() {
		return inferTrade2;
	}

	public void setInferTrade2(String inferTrade2) {
		this.inferTrade2 = inferTrade2;
	}

	public String getInferProv() {
		return inferProv;
	}

	public void setInferProv(String inferProv) {
		this.inferProv = inferProv;
	}

	public String getInferCity() {
		return inferCity;
	}

	public void setInferCity(String inferCity) {
		this.inferCity = inferCity;
	}

	public String getInferCounty() {
		return inferCounty;
	}

	public void setInferCounty(String inferCounty) {
		this.inferCounty = inferCounty;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public Set<String> getSource3List() {
		return source3List;
	}

	public void setSource3List(Set<String> source3List) {
		this.source3List = source3List;
	}

	public long getAddTime() {
		return addTime;
	}

	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}

	public String getSource1Id() {
		return source1Id;
	}

	public void setSource1Id(String source1Id) {
		this.source1Id = source1Id;
	}

	public String getSource2Id() {
		return source2Id;
	}

	public void setSource2Id(String source2Id) {
		this.source2Id = source2Id;
	}

	public Set<String> getSource3IdList() {
		return source3IdList;
	}

	public void setSource3IdList(Set<String> source3IdList) {
		this.source3IdList = source3IdList;
	}

	public String getInferTrade1Id() {
		return inferTrade1Id;
	}

	public void setInferTrade1Id(String inferTrade1Id) {
		this.inferTrade1Id = inferTrade1Id;
	}

	public String getInferTrade2Id() {
		return inferTrade2Id;
	}

	public void setInferTrade2Id(String inferTrade2Id) {
		this.inferTrade2Id = inferTrade2Id;
	}

	public String getInferProvId() {
		return inferProvId;
	}

	public void setInferProvId(String inferProvId) {
		this.inferProvId = inferProvId;
	}

	public String getInferCityId() {
		return inferCityId;
	}

	public void setInferCityId(String inferCityId) {
		this.inferCityId = inferCityId;
	}

	public String getInferCountyId() {
		return inferCountyId;
	}

	public void setInferCountyId(String inferCountyId) {
		this.inferCountyId = inferCountyId;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public Set<String> getInferTrade3List() {
		return inferTrade3List;
	}

	public void setInferTrade3List(Set<String> inferTrade3List) {
		this.inferTrade3List = inferTrade3List;
	}

	public Set<String> getInferTrade3IdList() {
		return inferTrade3IdList;
	}

	public void setInferTrade3IdList(Set<String> inferTrade3IdList) {
		this.inferTrade3IdList = inferTrade3IdList;
	}

	public static void main(String[] args) throws Exception {
		BaseClue a = new BaseClue();
		a.setOnlineStatus(OnlineStatus.DEL);
		BaseClue b = (BaseClue) a.clone();
		b.setOnlineStatus(OnlineStatus.MODIFY);
		System.out.println(a.getOnlineStatus());
	}
}
