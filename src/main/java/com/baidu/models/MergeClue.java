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

public class MergeClue implements Cloneable {

	private static final Logger LOG = LoggerFactory.getLogger(MergeClue.class);

	/**
	 * 包含的判重线索
	 */
	private DupClue dupClue = new DupClue();

	/**
	 * 是否为工商来源
	 */
	private boolean containGS = false;

	/**
	 * 存在在哪几个线上库，如果列表未空，表示可以下发
	 */
	private Set<String> onlineSourceList = new HashSet<String>();

	/**
	 * 整合出当前线索的归一化线索，每个元素是DupClue的key,即source:id
	 */
	private List<String> sourceIdList = new ArrayList<String>();

	/**
	 * 整合时间,秒数
	 */
	private String mergeTime = "";

	

	
	@Override
	public String toString() {
		try {
			String split = "---merge---";
			StringBuilder sb = new StringBuilder();
			Field[] fields = this.getClass().getDeclaredFields();
			for (Field f : fields) {
				f.setAccessible(true);
				if (f.getType().getName().equals(DupClue.class.getName())) {
					sb.append(f.getName()).append(":").append(f.get(this))
							.append(split);
				} else if (f.getType().getName()
						.equals(Address.class.getName())) {
					sb.append(f.getName()).append(":").append(f.get(this))
							.append(split);
				} else if (f.getType().getName().equals(String.class.getName())) {
					sb.append(f.getName()).append(":").append(f.get(this))
							.append(split);
				}
			}
			for (String s : onlineSourceList) {
				sb.append("onlineSource:").append(s).append(split);
			}
			for (String s : sourceIdList) {
				sb.append("source&Id:").append(s).append(split);
			}
			sb.append("containGS:").append(containGS);

			return sb.toString();
		} catch (IllegalAccessException e) {
			LOG.error("toString failed, ", e);
			return "";
		}
	}

	public DupClue getDupClue() {
		return dupClue;
	}

	public void setDupClue(DupClue dupClue) {
		this.dupClue = dupClue;
	}

	public boolean isContainGS() {
		return containGS;
	}

	public void setContainGS(boolean containGS) {
		this.containGS = containGS;
	}

	public Set<String> getOnlineSourceList() {
		return onlineSourceList;
	}

	public void setOnlineSourceList(Set<String> onlineSourceList) {
		this.onlineSourceList = onlineSourceList;
	}

	public List<String> getSourceIdList() {
		return sourceIdList;
	}

	public void setSourceIdList(List<String> sourceIdList) {
		this.sourceIdList = sourceIdList;
	}

	public String getMergeTime() {
		return mergeTime;
	}

	public void setMergeTime(String mergeTime) {
		this.mergeTime = mergeTime;
	}

}
