package com.dtc.test.client;

import java.util.Arrays;


public class Generate {
	@Deprecated
	public static StringBuilder generateString() {
		StringBuilder stringBuilder=new StringBuilder();
		for (int i = 0; i < 128; i++) {
			stringBuilder.append("12345678");
		}
		return stringBuilder;
	}
	
	public static String byByte(int amount) {
		char[] charArray = new char[amount];
		Arrays.fill(charArray, '1');
		return new String(charArray);
	}
	
	public static String byKB(int kb) {
		return byByte(kb * 1024);
	}
	
	public static String byMB(int mb) {
		return byKB(mb * 1024);
	}
}
