package com.itheima.googlemarket.http;

public interface JsonRequestCallback {

	//跑出一个接口方法  获取数据完毕后 执行的方法
	void onRequestFinish(String json);
}
