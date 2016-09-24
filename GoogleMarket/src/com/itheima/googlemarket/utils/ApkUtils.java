package com.itheima.googlemarket.utils;

import com.itheima.googlemarket.MyApp;

import android.content.Intent;
import android.net.Uri;


public class ApkUtils {

	/**
	 * 安装apk
	 * @param apkFilePath apk所在路径
	 */
	public static void install(String apkFilePath){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//指定apk文件路径,安装apk
		intent.setDataAndType(Uri.parse("file://"+apkFilePath),"application/vnd.android.package-archive");
		MyApp.getContext().startActivity(intent);
	}
}
