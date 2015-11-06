package com.dtc.test.client;


public class Generate {
	public static StringBuilder generateString() {
		StringBuilder stringBuilder=new StringBuilder();
		for (int i = 0; i < 128; i++) {
			stringBuilder.append("12345678");
		}
		return stringBuilder;
	}
}
