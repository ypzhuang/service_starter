/**
 * created Feb 19, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.hp.tiger.starter.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;

public class NumberHelper {
	public static Integer ifNullToMIN(Integer value) {
		if(value == null) return Integer.MIN_VALUE;
		return value;
	}
	
	public static Integer ifNullToMAX(Integer value) {
		if(value == null) return Integer.MAX_VALUE;
		return value;
	}
	
	public static boolean randomBoolean() {
		int i = RandomUtils.nextInt(0, 2);
		if(i == 0) return true;
		else return false;
	}
	
	public static List<Integer> randomIntegers(int startInclusive, int endExclusive, int size) {
		List<Integer> randoms = new ArrayList<>();		
		Integer index = null;
		while(randoms.size() < size ) {
			index = RandomUtils.nextInt(startInclusive, endExclusive);
			if(!randoms.contains(index)) {
				randoms.add(index);
			}			
		}
		return randoms;
	}
	
	public static Integer randomInteger(int startInclusive, int endExclusive) {
		return RandomUtils.nextInt(startInclusive, endExclusive);
	}
	
	public static Integer randomInteger() {
		return RandomUtils.nextInt(100000, 999999);
	}
	
	public static String randomCode() {
		return  String.valueOf(RandomUtils.nextInt(100000, 999999));
	}
}
