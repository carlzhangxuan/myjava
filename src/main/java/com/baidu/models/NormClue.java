package com.baidu.models;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 归一化线索类
 * 
 * @author tangchunsong
 * 
 */
public class NormClue  {

	private static final Logger LOG = LoggerFactory.getLogger(NormClue.class);

	private BaseClue baseClue = new BaseClue();

	private Address custAddr = new Address(); // 地址

	private Address regAddr = new Address(); // 注册地址



	@Override
	public String toString() {
		try {
			String split = "---norm---";
			StringBuilder sb = new StringBuilder();
			Field[] fields = this.getClass().getDeclaredFields();
			for (Field f : fields) {
				f.setAccessible(true);
				if (!f.getType().isPrimitive()) {
					sb.append(f.getName()).append(":").append(f.get(this))
							.append(split);
				}
			}
			return sb.toString();
		} catch (IllegalAccessException e) {
			LOG.error("toString failed, ", e);
			return "";
		}
	}
	

	public BaseClue getBaseClue() {
		return baseClue;
	}

	public void setBaseClue(BaseClue baseClue) {
		this.baseClue = baseClue;
	}

	public Address getCustAddr() {
		return custAddr;
	}

	public void setCustAddr(Address custAddr) {
		this.custAddr = custAddr;
	}

	public Address getRegAddr() {
		return regAddr;
	}

	public void setRegAddr(Address regAddr) {
		this.regAddr = regAddr;
	}

}
