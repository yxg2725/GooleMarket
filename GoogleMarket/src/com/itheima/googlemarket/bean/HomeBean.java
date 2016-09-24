package com.itheima.googlemarket.bean;

import java.util.ArrayList;

public class HomeBean {


	/** 首页轮播图的url */
	public ArrayList<String> picture;
	/** App信息 */
	public ArrayList<AppInfo> list;
	
	/** 应用信息 */
	public static class AppInfo {
		/** 应用id */
		public String id;
		/** 应用名称 */
		public String name;
		/** 应用包名 */
		public String packageName;
		/** 应用iocn */
		public String iconUrl;
		/** 应用评分 */
		public float stars;
		/** 应用大小 */
		public long size;
		/** 应用下载地址 */
		public String downloadUrl;
		/** 应用描述 */
		public String des;
	}
}
