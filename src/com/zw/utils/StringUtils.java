package com.zw.utils;


public class StringUtils {
	public static Boolean isNotEmpty(String str){
		return str!=null&&str.length()>0;
	}
	
	public static Boolean isEmpty(String str){
		return !isNotEmpty(str);
	}
	
	public static Boolean bothAreEmpty(String a,String b){
		return isNotEmpty(a)&&isNotEmpty(b);
	}
	
	public static Boolean bothAreNotEmpty(String a,String b){
		return isNotEmpty(a)&&isNotEmpty(b);
	}
	
	public static void main(String[] args) {
		System.out.println(StringUtils.isEmpty(null));
		System.out.println(StringUtils.isEmpty(""));
		System.out.println(StringUtils.isEmpty(" "));
		System.out.println(StringUtils.isEmpty("a"));
	}
}
