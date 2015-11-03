package com.dtc.test.shared.vo;

import com.google.gwt.user.client.rpc.IsSerializable;

public class StorageVO implements IsSerializable {
	private String key;
	private String data;
	public StorageVO() {
	}
	public StorageVO(String key, String data) {
		this.key=key;
		this.data=data;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}
