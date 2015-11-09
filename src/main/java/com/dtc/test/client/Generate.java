package com.dtc.test.client;

import java.util.Arrays;


public class Generate {
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
