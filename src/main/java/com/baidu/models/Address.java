package com.baidu.models;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.Writable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 地址抽象类
 * 
 * @author tangchunsong
 * 
 */
public class Address {

	private static final Logger LOG = LoggerFactory.getLogger(Address.class);

	/**
	 * 是否是直辖市
	 */
	private boolean isZX = false;
	/**
	 * 省
	 */
	private String prov = "";
	private int provId = -1;
	/**
	 * 市或直辖市
	 */
	private String city = "";
	private int cityId = -1;
	/**
	 * 区县
	 */
	private String districtOrCounty = "";
	private int districtOrCountyId = -1;

	private String address = "";

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb
				.append("prov:" + prov + ", city:" + city + ", county:"
						+ districtOrCounty).append(", isZXcity:").append(isZX)
				.toString();
	}

	public Address() {
	}
	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public boolean isZX() {
		return isZX;
	}

	public void setZX(boolean isZX) {
		this.isZX = isZX;
	}

	public String getDistrictOrCounty() {
		return districtOrCounty;
	}

	public void setDistrictOrCounty(String districtOrCounty) {
		this.districtOrCounty = districtOrCounty;
	}

	public int getProvId() {
		return provId;
	}

	public void setProvId(int provId) {
		this.provId = provId;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public int getDistrictOrCountyId() {
		return districtOrCountyId;
	}

	public void setDistrictOrCountyId(int districtOrCountyId) {
		this.districtOrCountyId = districtOrCountyId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}