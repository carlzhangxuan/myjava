package com.baidu.models;

/**
 * 线上在线上库(轩辕，老盘古，新盘古)的状态
 * 
 * @author work
 * 
 */
public enum OnlineStatus {

	ADD, // 新增，
	MODIFY, // 修改
	DEL, // 删除,需要将流失系统中的重复线索的对应线上库来源清掉
	NULL; // 默认无状态

	public static OnlineStatus getStatus(String status) {
		if (ADD.name().equals(status)) {
			return ADD;
		} else if (MODIFY.name().equals(status)) {
			return MODIFY;
		} else if (DEL.name().equals(status)) {
			return DEL;
		} else if (NULL.name().equals(status)) {
			return NULL;
		}

		return NULL;
	}
}
