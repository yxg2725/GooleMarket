package com.itheima.googlemarket.bean;

import java.util.List;

public class CategoryInfo {

	public String title;
	public List<Info> infos;
	
	public static class Info {
		public String url1;
		public String url2;
		public String url3;
		public String name1;
		public String name2;
		public String name3;
	}
}
