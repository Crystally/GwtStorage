package com.dtc.test.client;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.storage.client.Storage;

public class TestStorage {
	Storage storage = Storage.getLocalStorageIfSupported();

	public void test() {
		String data = Generate.byMB(5);
		
		try {
			storage.setItem("1", data);
		} catch (JavaScriptException e) {
			if ("QuotaExceededError".equals(e.getName())) {
				GWT.log("超出大小：" + data.getBytes().length);
				return;
			}
			
			//其他未知的 JAvaScriptException
			GWT.log("[" + e.getName() + "] : " + e.getMessage());
		} catch (Exception e) {
			//不知道有沒有可能炸到這邊來，預防萬一還是 catch 一下
			GWT.log("[" + e.getClass().getName() + "] : " + e.getMessage());
		}
	}

	// chrome value存入极限值测试,设1的时候是超过容量上限
	public void testSetValue(boolean over) {
		build(4, 1023, 1023, over, false);
	}

	// chrome value读取极限值测试,设1的时候是超过容量上限
	public void testReadValue(boolean over) {
		build(4, 1023, 1001, over, false);
	}

	// chrome key存入极限值测试,设1的时候是超过容量上限
	public void testSetKey(boolean over) {
		build(4, 1023, 1024, over, true);
	}

	// chrome key读取极限值测试,设1的时候是超过容量上限
	public void testReadKey(boolean over) {
		build(4, 1023, 1002, over, true);
	}
	
	// 测试容量上限是不是包含key和value
	public void testKeyValue(){
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append(Generate.byKB(1023));
		stringBuilder.append(Generate.byByte(1023));
		for (int i = 0; i < 5; i++) {
			storage.setItem(String.valueOf(i), stringBuilder.toString());
		}
	}
	private void build(int mb,int kb, int b, boolean over, boolean keyOrData) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(Generate.byMB(mb));
		stringBuilder.append(Generate.byKB(kb));
		stringBuilder.append(Generate.byByte(b));
		if (over) {
			stringBuilder.append("1");
		}
		if (keyOrData) {
			storage.setItem(stringBuilder.toString(), "");
		} else {
			storage.setItem("1", stringBuilder.toString());
		}
	}
}
