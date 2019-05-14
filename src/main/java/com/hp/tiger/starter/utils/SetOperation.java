package com.hp.tiger.starter.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetOperation {
	
	// A - B
	public static<T> List<T> minus(List<T> A, List<T> B) {
		List<T> result = new ArrayList<T>();
		for(T a : A) {
			boolean flag = false;
			for(T b : B) {
				if(a.equals(b)) flag = true;
			}
			if(!flag) result.add(a);
		}
		return result;
	}
	
	public static void main(String args[]) {
		List<Integer> A = null;
		List<Integer> B = null;
		
		A = Arrays.asList(1,2);		
		B = Arrays.asList(2,3);		
		System.out.println(minus(A,B));		
		System.out.println(minus(B,A));
		
		A = Arrays.asList(1,2);		
		B = Arrays.asList(4,3);		
		System.out.println(minus(A,B));		
		System.out.println(minus(B,A));
		
		A = new ArrayList<>();		
		B = Arrays.asList(4,3);	
		System.out.println(minus(A,B));		
		System.out.println(minus(B,A));
		
	}
}
