package com.itheima.googlemarket;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApp extends Application {

	private static Context context;
	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		
		initImageLoader(context);
		
	}
	
	public static Context getContext() {
		return context;
	}
	
	public static void initImageLoader(Context context) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_default)	// 正在加载图片时显示的默认图片
				.showImageForEmptyUri(R.drawable.ic_default)	// 图片Url为空时显示的默认图片
				.showImageOnFail(R.drawable.ic_default)		// 图片加载失败时显示的默认图片
				.cacheInMemory(true)		// 缓存到内存
				.cacheOnDisk(true)		// 缓存到磁盘
				.considerExifParams(true)	//考虑exif参数
//		.displayer(new RoundedBitmapDisplayer(20))	// 显示圆角
				.build();
			
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)		// 线程优先级
				.denyCacheImageMultipleSizesInMemory()		// 禁止在内存中把一张图片缓存多种尺寸
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())	// 缓存文件名的生成器
				.tasksProcessingOrder(QueueProcessingType.LIFO)	// 下载图片的任务的顺序
				.writeDebugLogs() // Remove for release app	// 写出调试信息，发布apk时注释掉
				.defaultDisplayImageOptions(options)		// 默认显示图片显示选项
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);				// 初始化ImageLoader
	}
	
	
}
