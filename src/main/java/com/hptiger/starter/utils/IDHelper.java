package com.hptiger.starter.utils;

import java.util.UUID;

import org.apache.http.util.Asserts;

public class IDHelper {
	
	public static String generateUUID() {
		String uuid = UUID.randomUUID().toString();
		return uuid.replaceAll("-", "");
	}
	
	public static String maskUUID(String uuid) {
		Asserts.check(uuid != null && uuid.length() == 32, "UUID should have no -");		
		String head = uuid.substring(0, 4);
		String tailer = uuid.substring(uuid.length()-4,uuid.length());
		String stars = stars(uuid.length() - 8);
		return head + stars + tailer;
	}
	
	public static String stars(int starLength) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < starLength; i++) {
			builder.append("*");
		}
		return builder.toString();		
	}
}
