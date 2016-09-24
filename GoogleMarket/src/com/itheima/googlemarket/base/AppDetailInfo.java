package com.itheima.googlemarket.base;

import java.util.ArrayList;

public class AppDetailInfo {

	/** 应用id */
	public String id;
	/** 应用名称 */
	public String name;
	/** 应用包名 */
	public String packageName;
	/** 应用图标地址 */
	public String iconUrl;
	/** 应用评分 */
	public float stars;
	/** 应用下载量 */
	public String downloadNum;
	/** 应用版本号 */
	public String version;
	/** 应用日期 */
	public String date;
	/** 应用大小 */
	public long size;
	/** 应用下载地址 */
	public String downloadUrl;
	/** 应用简介 */
	public String des;
	/** 应用作者 */
	public String author;
	
	/** 应用的界面截图 */
	public ArrayList<String> screen;
	
	/** 官网安全无广告信息 */
	public ArrayList<Safe> safe;
	
	public static class Safe {
		/** 图标网址 */
		public String safeUrl;
		/** 图标对应的文字描述 */
		public String safeDes;
	}
}
