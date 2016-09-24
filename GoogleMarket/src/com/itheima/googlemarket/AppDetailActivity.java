package com.itheima.googlemarket;

import java.io.File;
import java.util.TreeMap;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Formatter;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.googlemarket.base.AppDetailInfo;
import com.itheima.googlemarket.base.AppDetailInfo.Safe;
import com.itheima.googlemarket.http.JsonRequestCallback;
import com.itheima.googlemarket.http.NetUtil;
import com.itheima.googlemarket.http.Urls;
import com.itheima.googlemarket.httpDown.DownloadInfo;
import com.itheima.googlemarket.httpDown.DownloadManager;
import com.itheima.googlemarket.httpDown.DownloadService;
import com.itheima.googlemarket.interfaces.Keys;
import com.itheima.googlemarket.utils.ApkUtils;
import com.itheima.googlemarket.utils.JsonUtils;
import com.itheima.googlemarket.utils.UiUtils;
import com.itheima.googlemarket.view.StateFrameLayout;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AppDetailActivity extends ActionBarActivity implements JsonRequestCallback, OnClickListener {

	private StateFrameLayout  stateLayout;
	private ImageView iv_icon;
	private TextView tv_name;
	private RatingBar rating_bar;
	private TextView tv_download_num;
	private TextView tv_version;
	private TextView tv_date;
	private TextView tv_size;
	private AppDetailInfo appDetailInfo;
	private LinearLayout ll_safe_root;
	private LinearLayout ll_safe_icon;
	private LinearLayout ll_safe_desc;
	private ImageView iv_safe_arrow;
	private boolean safeIsOpen;
	private boolean descIsOpen;
	private LinearLayout ll_screen;
	private ScrollView scroll_view;
	private LinearLayout ll_desc;
	private TextView tv_desc;
	private ImageView iv_desc_arrow;
	private int descDefaultHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//创建一个actionBar
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		//三中状态布局
		stateLayout = (StateFrameLayout) View.inflate(this, R.layout.state_layout, null);
		//第四种正常布局
		stateLayout.setContentView(R.layout.activity_app_details);

		//显示
		setContentView(stateLayout);

		initView();
		initListener();
		initData();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	//主要是执行findveiwById()
	private void initView() {
		
		//获取下载管理者对象  同时开启了下载服务
		downloadManager = DownloadService.getDownloadManager(this);

		//第一块内容应用详情
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		tv_name = (TextView) findViewById(R.id.tv_name);
		rating_bar = (RatingBar) findViewById(R.id.rating_bar);
		tv_download_num = (TextView) findViewById(R.id.tv_download_num);
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_date = (TextView) findViewById(R.id.tv_date);
		tv_size = (TextView) findViewById(R.id.tv_size);

		//第二块内容官方  安全 无广告  和文字描述
		ll_safe_root = (LinearLayout) findViewById(R.id.ll_safe_root);
		ll_safe_icon = (LinearLayout) findViewById(R.id.ll_safe_icon);
		ll_safe_desc = (LinearLayout) findViewById(R.id.ll_safe_desc);
		iv_safe_arrow = (ImageView) findViewById(R.id.iv_safe_arrow);

		//默认隐藏 官方 安全 无广告 和描述  获取完数据后 根据 数据展示
		for (int i = 0; i < ll_safe_desc.getChildCount(); i++) {
			ll_safe_icon.getChildAt(i).setVisibility(View.GONE);
			ll_safe_desc.getChildAt(i).setVisibility(View.GONE);
		}
		//默认描述的高为0
		ll_safe_desc.getLayoutParams().height = 0;
		//通知参数刷新参数
		ll_safe_desc.requestLayout();


		//第三块内容  图片截图
		ll_screen = (LinearLayout) findViewById(R.id.ll_screen);

		//第四块内容  应用简介
		scroll_view = (ScrollView) findViewById(R.id.scroll_view);
		ll_desc = (LinearLayout) findViewById(R.id.ll_desc);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
		iv_desc_arrow = (ImageView) findViewById(R.id.iv_desc_arrow);


		//底部视图
		pb_download = (ProgressBar) findViewById(R.id.pb_download);
		tv_downLoad = (TextView) findViewById(R.id.tv_downLoad);


		//给desc设置布局改变监听器   当onLayout方法执行的时候就会执行这个监听
		tv_desc.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				//获取图片的高度
				descDefaultHeight = tv_desc.getHeight();
				System.out.println("--------------" +descDefaultHeight);
				if (descDefaultHeight > 0) {
					tv_desc.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}

			}
		});



	}

	//各种点击监听 滚动监听 等等
	private void initListener() {
		ll_safe_root.setOnClickListener(this);
		ll_desc.setOnClickListener(this);
		pb_download.setOnClickListener(this);
	}

	//设置数据   主要是联网获取数据
	private void initData() {
		TreeMap<String, String> params = new TreeMap<String, String>();
		params.put("packageName", getIntent().getStringExtra(Keys.PACKAGENAME));//参数二为获取intent传递过来的数据
		NetUtil.requestData(Urls.DETAIL, params, this);
		
	}

	@Override//数据请求完毕后 返回
	public void onRequestFinish(String json) {

		//解析json
		appDetailInfo = JsonUtils.json2Bean(json, AppDetailInfo.class);
		//判断数据有效性
		if (appDetailInfo == null) {
			//显示失败界面
			stateLayout.showFailView();
		}else {
			//显示正常界面
			stateLayout.showContentView();
			showAppInfo();
			showAppSafe();
			showScreen();
			showDesc();
			refreshUI();//显示下当前下载的状态

		}
	}

	//展示应用详细信息
	private void showAppInfo() {

		//展示应用图标
		String iconUrl = Urls.IMAGE + "?name=" + appDetailInfo.iconUrl;
		ImageLoader.getInstance().displayImage(iconUrl, iv_icon);
		//展示应用的其他信息
		tv_name.setText(appDetailInfo.name);				// 显示名称
		tv_download_num.setText(appDetailInfo.downloadNum);	// 显示下载量
		tv_version.setText(appDetailInfo.version);			// 显示版本号
		tv_date.setText(appDetailInfo.date);				// 显示发布日期
		tv_size.setText(Formatter.formatFileSize(this, appDetailInfo.size));	// 显示应用大小
		rating_bar.setRating(appDetailInfo.stars);			// 显示评分
	}

	//展示官方 安全 无广告
	private void showAppSafe() {
		//判断数据  并遍历数据集合 
		if (appDetailInfo.safe == null || appDetailInfo.safe.isEmpty()) {
			return;
		}

		//遍历数据
		for (int i = 0; i < appDetailInfo.safe.size(); i++) {
			//获取每一条数据
			Safe safe = appDetailInfo.safe.get(i);
			//获取ImageView控件
			ImageView imageView = (ImageView) ll_safe_icon.getChildAt(i);
			//展示图片 先设置可见
			ll_safe_icon.getChildAt(i).setVisibility(View.VISIBLE);
			String iconUrl = Urls.IMAGE + "?name=" + safe.safeUrl;
			ImageLoader.getInstance().displayImage(iconUrl, imageView);

			//显示描述信息
			TextView appDesc = (TextView) ll_safe_desc.getChildAt(i);
			appDesc.setVisibility(View.VISIBLE);
			appDesc.setText(safe.safeDes);

		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_safe_root:
			//开关官方安全无广告描述
			safeDisplayToggle();
			break;
		case R.id.ll_desc:
			//开关官方安全无广告描述
			descDisplayToggle();
			break;
		case R.id.pb_download:
			//进度条点击事件
			progressBarClick();
			break;
		}

	}

	private void safeDisplayToggle() {
		//测量下描述d
		ll_safe_desc.measure(0, 0);
		int original =ll_safe_desc.getMeasuredHeight();//获取测量的高


		ValueAnimator valueAniator;
		if (safeIsOpen) {//是打开状态 ---->关闭
			valueAniator = ValueAnimator.ofInt(original,0);
			//箭头变换
			iv_safe_arrow.setImageResource(R.drawable.arrow_down);
		}else {//关闭状态 ------>打开
			valueAniator = ValueAnimator.ofInt(0,original);
			//箭头变换
			iv_safe_arrow.setImageResource(R.drawable.arrow_up);
		}

		safeIsOpen = !safeIsOpen;
		//
		valueAniator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				//将value设置给desc的高的
				ll_safe_desc.getLayoutParams().height = (Integer) animation.getAnimatedValue();
				//属性layout
				ll_safe_desc.requestLayout();
			}
		});

		valueAniator.setDuration(200);
		valueAniator.start();

	}

	//应用简介的缩放开关
	private void descDisplayToggle() {
		//获取展开后的总高度
		int totalHeight = getTotalHeight();

		final ValueAnimator value;
		if (descIsOpen) {//打开状态 ----->关闭
			value = ValueAnimator.ofInt(totalHeight,descDefaultHeight);
			iv_desc_arrow.setImageResource(R.drawable.arrow_down);
		}else {//关闭状态 ------>打开
			value = ValueAnimator.ofInt(descDefaultHeight,totalHeight);
			iv_desc_arrow.setImageResource(R.drawable.arrow_up);
		}

		descIsOpen = !descIsOpen;

		//刷新value
		value.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				tv_desc.getLayoutParams().height = (Integer) animation.getAnimatedValue();
				//更新layout
				tv_desc.requestLayout();

				//ScrollView直接滚动value的数字
				scroll_view.scrollBy(0, (Integer) animation.getAnimatedValue());

			}
		});

		value.setDuration(200);
		value.start();

	}

	//展示图片截图
	private void showScreen() {
		if (appDetailInfo.screen == null || appDetailInfo.screen.isEmpty()) {
			return;
		}

		for (int i = 0; i < appDetailInfo.screen.size(); i++) {

			ImageView imageView = new ImageView(AppDetailActivity.this);

			int width = UiUtils.dp2px(90);
			int height = UiUtils.dp2px(150);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

			if (i != 0) {
				params.leftMargin = UiUtils.dp2px(6);
			}
			ll_screen.addView(imageView, params );

			//下载图片并展示
			String picUrl = Urls.IMAGE + "?name=" + appDetailInfo.screen.get(i);
			ImageLoader.getInstance().displayImage(picUrl, imageView);

			imageView.setTag(i);
			//添加点击事件
			imageView.setOnClickListener(imageListener);
		}

	}

	OnClickListener imageListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			//点击图片跳转到大图的activity  并把position 和图片地址传递过去
			Intent intent = new Intent(AppDetailActivity.this, BigPictureActivity.class);
			intent.putExtra(Keys.POSITION, position);
			intent.putStringArrayListExtra(Keys.SCREEN_URLS, appDetailInfo.screen);
			startActivity(intent);
		}
	};
	private ProgressBar pb_download;
	private DownloadManager downloadManager;

	//展示应用简介
	private void showDesc() {
		tv_desc.setText(appDetailInfo.des);//布局里设置了只显示7行
	}

	//获取应用简介的总高度
	private int getTotalHeight() {
		TextView text = new TextView(this);
		text.setText(tv_desc.getText());//设置和简介上的文字一样
		text.setTextSize(14);//设置和简介上的文字一样大
		//System.out.println(tv_desc.getText());
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(tv_desc.getWidth(), MeasureSpec.EXACTLY );
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED );
		//测量下
		text.measure(widthMeasureSpec, heightMeasureSpec);

		//获取高度
		int totalHeight = text.getMeasuredHeight();
		//System.out.println(totalHeight);
		return totalHeight;
	}


	//进度条点击事件
	private void progressBarClick() {
		
		
		if (appDetailInfo == null) {
			return;
		}
		
		String appUrl = Urls.DOWNLOAD + "?name=" + appDetailInfo.downloadUrl;
		//获取下应用id
    	DownloadInfo downLoadInfo = downloadManager.getDownloadInfoByAppId(appDetailInfo.id);
    	if (downLoadInfo == null) {//说明没有下载过
    		String target = "/sdcard/GoogleMarket/"+appDetailInfo.name+".apk";
    		try {
    			downloadManager.addNewDownload(appDetailInfo.id,appUrl,
    					appDetailInfo.name,
    					target,
    					true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
    					false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
    					downloadStateListener);
    		} catch (DbException e) {
    			LogUtils.e(e.getMessage(), e);
    		}
    	}else {//不是第一次下载  先获取下状态
			
		   HttpHandler.State state = downLoadInfo.getState();
           switch (state) {
               case WAITING:
               case STARTED:
               case LOADING:
			try {
				downloadManager.stopDownload(downLoadInfo);
			} catch (DbException e) {
				e.printStackTrace();
			}
                   break;
               case CANCELLED:
               case FAILURE:
			try {
				downloadManager.resumeDownload(downLoadInfo, downloadStateListener);
			} catch (DbException e) {
				e.printStackTrace();
			}
                   break;
               case SUCCESS:
            	  //安装应用
            	   ApkUtils.install(downLoadInfo.getFileSavePath());
            	   break;
           }
    	
    	}
	}


	
	 public RequestCallBack<File> downloadStateListener = new RequestCallBack<File>(){

	    	/**下载取消*/
			@Override
			public void onCancelled() {
				refreshUI();
			}

			/**正在下载*/
			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				refreshUI();
			}

			/**开始下载*/
			@Override
			public void onStart() {
				refreshUI();
			}


			/**下载失败*/
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				refreshUI();
			}

			/**下载成功*/
			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				refreshUI();
			}
	    	
	    };
	private TextView tv_downLoad;

	//显示下载的进度和状态
	protected void refreshUI() {
		//先获取下载的应用信息
		DownloadInfo downloadInfo = downloadManager.getDownloadInfoByAppId(appDetailInfo.id);
		//这一步必不可少
		if (downloadInfo == null) {
			tv_downLoad.setText("下载");
			return;
		}
		
    	if (downloadInfo.getFileLength() > 0) {
           int percent = (int) (downloadInfo.getProgress() * 100 / downloadInfo.getFileLength());
           tv_downLoad.setText(percent + "%");
           pb_download.setProgress(percent);
        } else {
        	pb_download.setProgress(0);
        	tv_downLoad.setText(0 + "%");
        }
    	
    	HttpHandler.State state = downloadInfo.getState();
        switch (state) {
            case CANCELLED:	// 当前状态是暂停
            	tv_downLoad.setText("暂停");
                break;
            case SUCCESS:	// 当前状态是下载完成
            	tv_downLoad.setText("安装");
                break;
            case FAILURE:	// 当前状态是下载失败
            	tv_downLoad.setText("重试");
                break;
        }
	}


}
