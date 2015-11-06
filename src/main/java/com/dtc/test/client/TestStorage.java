package com.dtc.test.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.storage.client.Storage;

public class TestStorage {
	Storage storage = Storage.getLocalStorageIfSupported();

	public void test() {
		boolean b = true;
		StringBuilder stringBuilder = Generate.generateString();
		while (b) {
			try {
				storage.setItem("1", stringBuilder.append("1").toString());
			} catch (Exception e) {
				if (e.getMessage().contains("QuotaExceededError")) {
					GWT.log(String.valueOf(stringBuilder.toString().getBytes().length));
					b = false;
				}
			}
		}
	}

	// chrome value存入极限值测试,设1的时候是超过容量上限
	public void testSetValue(int over) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < 5119; i++) {
			stringBuilder.append(Generate.generateString());
		}
		for (int i = 0; i < 1023; i++) {
			stringBuilder.append("1");
		}
		if (over == 1) {
			stringBuilder.append("1");
		}
		storage.setItem("1", stringBuilder.toString());
	}

	// chrome value读取极限值测试,设1的时候是超过容量上限
	public void testReadValue(int over) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < 5119; i++) {
			stringBuilder.append(Generate.generateString());
		}
		for (int i = 0; i < 1001; i++) {
			stringBuilder.append("1");
		}
		if (over == 1) {
			stringBuilder.append("1");
		}
		storage.setItem("1", stringBuilder.toString());
	}

	// chrome key存入极限值测试,设1的时候是超过容量上限
	public void testSetKey(int over) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < 5119; i++) {
			stringBuilder.append(Generate.generateString());
		}
		for (int i = 0; i < 1024; i++) {
			stringBuilder.append("1");
		}
		if (over == 1) {
			stringBuilder.append("1");
		}
		storage.setItem(stringBuilder.toString(), "");
	}

	// chrome key读取极限值测试,设1的时候是超过容量上限
	public void testReadKey(int over) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < 5119; i++) {
			stringBuilder.append(Generate.generateString());
		}
		for (int i = 0; i < 1002; i++) {
			stringBuilder.append("1");
		}
		if (over == 1) {
			stringBuilder.append("1");
		}
		storage.setItem(stringBuilder.toString(), "");
	}
}
