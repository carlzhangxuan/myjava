package com.baidu.models;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.hadoop.io.Writable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 公司规模信息类
 * 
 * @author tangchunsong
 * 
 */
public class Scale implements Cloneable, Writable {

	private static final Logger LOG = LoggerFactory.getLogger(Scale.class);

	private String empNum = "";// 员工人数
	private String resNum = "";// 研发人员数量
	private String annualTurnover = "";// 年营业额
	private String imports = "";// 年进口额
	private String exports = "";// 年出口额
	private String monYield = "";// 月盈利额
	private String roomArea = "";// 占地面积

	@Override
	public Object clone() throws CloneNotSupportedException {
		return (Scale) super.clone();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(empNum);
		out.writeUTF(resNum);
		out.writeUTF(annualTurnover);
		out.writeUTF(imports);
		out.writeUTF(exports);
		out.writeUTF(monYield);
		out.writeUTF(roomArea);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		empNum = in.readUTF();
		resNum = in.readUTF();
		annualTurnover = in.readUTF();
		imports = in.readUTF();
		exports = in.readUTF();
		monYield = in.readUTF();
		roomArea = in.readUTF();
	}

	@Override
	public String toString() {
		try {
			StringBuilder sb = new StringBuilder();
			Field[] fields = this.getClass().getDeclaredFields();
			for (Field f : fields) {
				f.setAccessible(true);
				if (f.getType().getName().equals(String.class.getName())) {
					sb.append(f.getName()).append("-").append(f.get(this))
							.append("\n");
				}
			}
			return sb.toString();
		} catch (IllegalAccessException e) {
			LOG.error("toString failed, ", e);
			return "";
		}
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

}