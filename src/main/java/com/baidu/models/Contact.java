package com.baidu.models;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.io.Writable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 联系人信息类
 * 
 * @author tangchunsong
 * 
 */
public class Contact implements Cloneable, Writable {

	private static final Logger LOG = LoggerFactory.getLogger(Contact.class);

	private String name = "";// 姓名
	private String phone = "";// 总电话串，解析后逗号分隔
	private String duty = "";// 职务
	private String gender = ""; // 性别
	private String email = ""; // 邮箱，若干,去重，逗号连接
	private String fax = ""; // 传真，若干,去重，逗号连接
	private Set<String> phoneUrls = new HashSet<String>(); // 电话图片url
	private Set<String> names = new HashSet<String>();// 多个名称
	private Set<String> mobs = new HashSet<String>();// 手机
	private Set<String> tels = new HashSet<String>();// 座机
	private Set<String> hots = new HashSet<String>();// 热线
	private Set<String> phones = new HashSet<String>();// 所有电话

	@Override
	public boolean equals(Object other) {
		if (this == other) { // 自反性
			return true;
		}
		if (other == null) {
			return false;
		}
		if (!(other instanceof Contact)) {
			return false;
		}

		final Contact contact = (Contact) other;
		if (names.equals(contact.getNames())
				&& phones.equals(contact.getPhones())
				&& duty.equals(contact.getDuty())
				&& gender.equals(contact.getGender())
				&& email.equals(contact.getEmail())
				&& fax.equals(contact.getFax())) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		try {
			String split = "---contact---";
			StringBuilder sb = new StringBuilder();
			Field[] fields = this.getClass().getDeclaredFields();
			for (Field f : fields) {
				f.setAccessible(true);
				if (f.getType().getName().equals(String.class.getName())) {
					sb.append(f.getName()).append(":").append(f.get(this))
							.append(split);
				}
			}
			for (String s : phoneUrls) {
				sb.append("phoneUrl:").append(s).append(split);
			}
			return sb.toString();
		} catch (IllegalAccessException e) {
			LOG.error("toString failed, ", e);
			return "";
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(name);
		out.writeUTF(phone);
		out.writeUTF(duty);
		out.writeUTF(gender);
		out.writeUTF(email);
		out.writeUTF(fax);

		out.writeInt(names.size());
		for (String s : names) {
			out.writeUTF(s);
		}

		out.writeInt(mobs.size());
		for (String s : mobs) {
			out.writeUTF(s);
		}

		out.writeInt(tels.size());
		for (String s : tels) {
			out.writeUTF(s);
		}

		out.writeInt(hots.size());
		for (String s : hots) {
			out.writeUTF(s);
		}

		out.writeInt(phones.size());
		for (String s : phones) {
			out.writeUTF(s);
		}

		out.writeInt(phoneUrls.size());
		for (String s : phoneUrls) {
			out.writeUTF(s);
		}
	}

	@Override
	public void readFields(DataInput in) throws IOException {

		name = in.readUTF();
		phone = in.readUTF();
		duty = in.readUTF();
		gender = in.readUTF();
		email = in.readUTF();
		fax = in.readUTF();

		int len = in.readInt();
		for (int i = 0; i < len; i++) {
			names.add(in.readUTF());
		}

		len = in.readInt();
		for (int i = 0; i < len; i++) {
			mobs.add(in.readUTF());
		}

		len = in.readInt();
		for (int i = 0; i < len; i++) {
			tels.add(in.readUTF());
		}

		len = in.readInt();
		for (int i = 0; i < len; i++) {
			hots.add(in.readUTF());
		}

		len = in.readInt();
		for (int i = 0; i < len; i++) {
			phones.add(in.readUTF());
		}

		len = in.readInt();
		for (int i = 0; i < len; i++) {
			phoneUrls.add(in.readUTF());
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Contact ret = (Contact) super.clone();
		Set<String> tmpPhoneUrls = new HashSet<String>();
		if (phoneUrls != null) {
			tmpPhoneUrls.addAll(phoneUrls);
		}
		ret.setPhoneUrls(tmpPhoneUrls);

		Set<String> tmpNames = new HashSet<String>();
		if (names != null) {
			tmpNames.addAll(names);
		}
		ret.setNames(tmpNames);

		Set<String> tmpMobs = new HashSet<String>();
		if (mobs != null) {
			tmpMobs.addAll(mobs);
		}
		ret.setMobs(tmpMobs);

		Set<String> tmpTels = new HashSet<String>();
		if (tels != null) {
			tmpTels.addAll(tels);
		}
		ret.setTels(tmpTels);

		Set<String> tmpHots = new HashSet<String>();
		if (hots != null) {
			tmpHots.addAll(hots);
		}
		ret.setHots(tmpHots);

		Set<String> tmpPhones = new HashSet<String>();
		if (phones != null) {
			tmpPhones.addAll(phones);
		}
		ret.setPhones(tmpPhones);

		return ret;
	}

	public Set<String> getPhoneUrls() {
		return phoneUrls;
	}

	public void setPhoneUrls(Set<String> phoneUrls) {
		this.phoneUrls = phoneUrls;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public Set<String> getMobs() {
		return mobs;
	}

	public void setMobs(Set<String> mobs) {
		this.mobs = mobs;
	}

	public Set<String> getTels() {
		return tels;
	}

	public void setTels(Set<String> tels) {
		this.tels = tels;
	}

	public Set<String> getHots() {
		return hots;
	}

	public void setHots(Set<String> hots) {
		this.hots = hots;
	}

	public Set<String> getPhones() {
		return phones;
	}

	public void setPhones(Set<String> phones) {
		this.phones = phones;
	}

	public Set<String> getNames() {
		return names;
	}

	public void setNames(Set<String> names) {
		this.names = names;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public static void main(String[] args) throws Exception {
		Contact a = new Contact();
		a.setDuty("sdfs");
		a.getPhones().add("123456789");
		Contact b = (Contact) a.clone();
		b.getPhones().add("34567");
		b.setDuty("12345");
		if (a.getPhone() == b.getPhone()) {
			System.out.println("equal");
		}
		System.out.println(a.getDuty());
		System.out.println(b.getDuty());
		for (String s : a.getPhones()) {
			System.out.println(s);
		}
	}
}
