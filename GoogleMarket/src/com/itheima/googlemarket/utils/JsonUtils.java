package com.itheima.googlemarket.utils;

import java.lang.reflect.Type;

import android.util.Log;

import com.google.gson.Gson;

public class JsonUtils {

	public static Gson gson = new Gson();
	public static <T> T json2Bean(String json, Class<T> beanClass){
		T bean = null;
		try {
			bean = gson.fromJson(json, beanClass);
		} catch (Exception e) {
			Log.i("JsonUtil", "解析json数据时出现异常\njson = " + json, e);
		}
		return bean;
	}
	
	public static <T> T json2Collection(String json, Type type) {
		T bean = null;
		try {
			bean = gson.fromJson(json, type);
		} catch (Exception e) {
			Log.i("JsonUtil", "解析json数据时出现异常\njson = " + json, e);
		}
		return bean;
	}
}
