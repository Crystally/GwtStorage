package com.dtc.test.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.storage.client.Storage;

public class TestStorage {
	Storage storage = Storage.getLocalStorageIfSupported();

	public void test() {
		StringBuilder stringBuilder = Generate.generateString();
		while (true) {
			try {
				storage.setItem("1", stringBuilder.append("1").toString());
			} catch (Exception e) {
				if (e.getMessage().contains("QuotaExceededError")) {
					GWT.log(String.valueOf(stringBuilder.toString().getBytes().length));
					break;
				}
			}
		}
	}

	// chrome value存入极限值测试,设1的时候是超过容量上限
	public void testSetValue(int over) {
		build(5119, 1023, over, false);
	}
	
	// chrome value读取极限值测试,设1的时候是超过容量上限
	public void testReadValue(int over) {
		build(5119, 1001, over, false);
	}

	// chrome key存入极限值测试,设1的时候是超过容量上限
	public void testSetKey(int over) {
		build(5119, 1024, over, true);
	}

	// chrome key读取极限值测试,设1的时候是超过容量上限
	public void testReadKey(int over) {
		build(5119, 1002, over, true);
	}
	
	private void build(int i1, int i2, int over, boolean keyOrData) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < i1; i++) {
			stringBuilder.append(Generate.generateString());
		}
		for (int i = 0; i < i2; i++) {
			stringBuilder.append("1");
		}
		if (over == 1) {
			stringBuilder.append("1");
		}
		if (keyOrData) {
			storage.setItem(stringBuilder.toString(), "");
		} else {
			storage.setItem("1", stringBuilder.toString());
		}
	}
}
