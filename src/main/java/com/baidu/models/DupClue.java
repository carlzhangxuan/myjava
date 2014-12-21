package com.baidu.models;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 带重复信息的线索类，包括：判重后线索和整合后线索
 * 
 * @author tangchunsong
 * 
 */
public class DupClue {

	private static final Logger LOG = LoggerFactory.getLogger(DupClue.class);

	/**
	 * 基础线索 之所以采用关联关系，而非继承关系，是因为继承的话，输入输出所有的字段都需要每个字段每个字段来考，而关联只需要clone
	 */
	private NormClue normClue = new NormClue();

	/**
	 * 线索重复Id,根据该id判断两个线索是否重复
	 */
	private String dupId;

	
	@Override
	public String toString() {
		try {
			String split = "---dup---";
			StringBuilder sb = new StringBuilder();
			Field[] fields = this.getClass().getDeclaredFields();
			for (Field f : fields) {
				f.setAccessible(true);
				if (f.getType().getName().equals(String.class.getName())) {
					sb.append(f.getName()).append(":").append(f.get(this))
							.append(split);
				} else if (f.getType().getName()
						.equals(NormClue.class.getName())) {
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


	public String getDupId() {
		return dupId;
	}

	public void setDupId(String dupId) {
		this.dupId = dupId;
	}

	public NormClue getNormClue() {
		return normClue;
	}

	public void setNormClue(NormClue normClue) {
		this.normClue = normClue;
	}

}
