package com.itheima.googlemarket.utils;

import android.util.Log;

public class Logger {

	public static boolean  showLog = true;
	public static void i(Object tagObj, Object msgObj){
		
		if (showLog) {
			String tag = convertStringTag(tagObj);
			String msg = convertStringMsg(msgObj);
			Log.i(tag, msg);
		}
	}
	
	public static void e(Object objTag, Object objMsg, Throwable e) {
		if (showLog) {
			Log.e(convertStringTag(objTag), convertStringMsg(objMsg), e);
		}
	}
	
	
	
	private static String convertStringTag(Object tagObj) {
		
		String tag;
		if (tagObj == null) {
			tag = "null";
		}else if(tagObj instanceof String){
			 tag = (String) tagObj;
		}else if(tagObj instanceof Class){
			tag = ((Class<?>) tagObj).getSimpleName();
		}else {
			tag = tagObj.getClass().getSimpleName();
		}
		
		return tag;
	}
	private static String convertStringMsg(Object msgObj) {
		String msg;
		
		if (msgObj == null) {
			msg = "null";
		}else {
			msg = msgObj.toString();
		}
		return msg;
	}
}
